package pizzaria;

import javax.swing.*;
import java.awt.*;
import java.util.concurrent.CountDownLatch;

public class Pizzaria {

    public static void main(String[] args) throws InterruptedException {

        CatalogoPizzas catalogo = new CatalogoPizzas();
        Pedido pedido = new Pedido();
        Forno forno = new Forno();
        Caixa caixa = new Caixa();

        // NOME DO CLIENTE
        JTextField campoNome = new JTextField();

        Object[] mensagemInicial = {
            "🍕 BEM-VINDO À PIZZARIA BERNARDES XD! 🍕",
            "Qual é o seu nome?",
            campoNome
        };

        int opcaoNome = JOptionPane.showOptionDialog(
                null,
                mensagemInicial,
                "Bem-vindo!",
                JOptionPane.DEFAULT_OPTION,
                JOptionPane.QUESTION_MESSAGE,
                null,
                new Object[]{"Seguinte"},
                "Seguinte"
        );

        if (opcaoNome == -1) {
            return;
        }

        String nomeCliente = campoNome.getText().trim();

        if (nomeCliente.isEmpty()) {
            nomeCliente = "Cliente";
        }

        // JANELA INICIAL COM BOTÃO
        JFrame janela = new JFrame("Pizzaria Bernardes XD");
        janela.setSize(400, 200);
        janela.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        janela.setLocationRelativeTo(null);

        JLabel mensagem = new JLabel(
                "Olá, " + nomeCliente + "! Vamos pedir uma pizza?",
                SwingConstants.CENTER
        );

        JButton botao = new JButton("Fazer pedido");

        JPanel painelInicial = new JPanel(new BorderLayout(10, 10));
        painelInicial.setBorder(
                BorderFactory.createEmptyBorder(20, 20, 20, 20)
        );
        painelInicial.add(mensagem, BorderLayout.CENTER);
        painelInicial.add(botao, BorderLayout.SOUTH);

        janela.add(painelInicial);

        CountDownLatch esperaClique = new CountDownLatch(1);

        botao.addActionListener(e -> {
            janela.dispose();
            esperaClique.countDown();
        });

        janela.setVisible(true);
        esperaClique.await();

        // COMER AQUI OU PARA LEVAR
        String[] opcoes = {"Comer aqui", "Para levar"};

        int escolha = JOptionPane.showOptionDialog(
                null,
                "Como deseja receber seu pedido?\n\n"
                + "Comer aqui: sem frete\n"
                + "Para levar: frete de R$ 5,00",
                "Pizzaria Bernardes XD",
                JOptionPane.DEFAULT_OPTION,
                JOptionPane.QUESTION_MESSAGE,
                null,
                opcoes,
                opcoes[0]
        );

        if (escolha == -1) {
            return;
        }

        double frete = 0.0;

        if (escolha == 1) {
            frete = 5.00;

            JOptionPane.showMessageDialog(
                    null,
                    "Seu pedido será para levar, "
                    + nomeCliente + ", perfeito!"
            );
        } else {
            JOptionPane.showMessageDialog(
                    null,
                    "Seu pedido será servido na pizzaria, "
                    + nomeCliente + "!"
            );
        }

        // CARDÁPIO DE PIZZAS (checkbox por sabor/tamanho, preços fixos)
        if (!TelePizzas.mostrar(catalogo, pedido, nomeCliente)) {
            return;
        }

        // CARDÁPIO DE BEBIDAS (checkbox)
        int[] qtdBebidas = TelaItens.mostrar(
                "Cardápio de bebidas",
                catalogo.getBebidas(),
                catalogo.getPrecosBebidas(),
                nomeCliente,
                pedido.calcularTotal(catalogo),
                "Seguinte"
        );

        if (qtdBebidas == null) {
            return;
        }

        for (int i = 0; i < qtdBebidas.length; i++) {
            pedido.adicionarBebida(i, qtdBebidas[i]);
        }

        // CARDÁPIO DE GULOSEIMAS (checkbox)
        int[] qtdGuloseimas = TelaItens.mostrar(
                "Cardápio de guloseimas",
                catalogo.getGuloseimas(),
                catalogo.getPrecosGuloseimas(),
                nomeCliente,
                pedido.calcularTotal(catalogo),
                "Finalizar pedido"
        );

        if (qtdGuloseimas == null) {
            return;
        }

        for (int i = 0; i < qtdGuloseimas.length; i++) {
            pedido.adicionarGuloseima(i, qtdGuloseimas[i]);
        }

        // CALCULAR TOTAL
        double subtotal = caixa.calcularTotal(pedido, catalogo);

        if (subtotal <= 0) {
            JOptionPane.showMessageDialog(
                    null,
                    "Você não selecionou nenhum item, "
                    + nomeCliente + ". Até a próxima!",
                    "Pedido vazio",
                    JOptionPane.INFORMATION_MESSAGE
            );
            return;
        }

        double total = subtotal + frete;

        // RESUMO DO PEDIDO
        String resumo = pedido.resumo(catalogo)
                + "\nSubtotal dos produtos: R$ "
                + String.format("%.2f", subtotal)
                + "\nFrete: R$ "
                + String.format("%.2f", frete)
                + "\n--------------------------"
                + "\nTOTAL: R$ "
                + String.format("%.2f", total);

        JOptionPane.showMessageDialog(
                null,
                resumo,
                "Resumo do pedido de " + nomeCliente,
                JOptionPane.INFORMATION_MESSAGE
        );

        // FORNO
        forno.prepararPedido(nomeCliente);

        // CAIXA E PAGAMENTO (checkbox de formas de pagamento)
        caixa.finalizarCompra(nomeCliente, total);

        // AGRADECIMENTO
        JOptionPane.showMessageDialog(
                null,
                "A Pizzaria Bernardes XD agradece seu pedido! 🍕",
                "Pedido finalizado",
                JOptionPane.INFORMATION_MESSAGE
        );
    }
}