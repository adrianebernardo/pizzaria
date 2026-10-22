
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

        // CARDÁPIO DE PIZZAS
        boolean continuar = true;

        while (continuar) {

            JComboBox<String> comboSabores =
                    new JComboBox<>(catalogo.getSabores());

            JComboBox<String> comboTamanho =
                    new JComboBox<>(catalogo.getTamanhos());

            JTextField campoQuantidade = new JTextField("1");

            JPanel painelPizza = new JPanel(
                    new GridLayout(0, 1, 5, 5)
            );

            painelPizza.add(new JLabel("Escolha o sabor da pizza:"));
            painelPizza.add(comboSabores);
            painelPizza.add(new JLabel("Tamanho da pizza:"));
            painelPizza.add(comboTamanho);
            painelPizza.add(new JLabel("Quantidade:"));
            painelPizza.add(campoQuantidade);

            Object[] botoesPizza = {
                "Adicionar pizza", "Seguinte"
            };

            int resultado = JOptionPane.showOptionDialog(
                    null,
                    painelPizza,
                    "Cardápio de pizzas",
                    JOptionPane.DEFAULT_OPTION,
                    JOptionPane.QUESTION_MESSAGE,
                    null,
                    botoesPizza,
                    botoesPizza[0]
            );

            if (resultado != 0) {
                continuar = false;
                continue;
            }

            try {
                String saborEscolhido =
                        (String) comboSabores.getSelectedItem();

                String tamanho =
                        (String) comboTamanho.getSelectedItem();

                int indiceSabor = comboSabores.getSelectedIndex();

                int quantidade = Integer.parseInt(
                        campoQuantidade.getText().trim()
                );

                if (quantidade <= 0) {
                    JOptionPane.showMessageDialog(
                            null,
                            "Informe uma quantidade maior que zero."
                    );
                    continue;
                }

                double precoUnitario =
                        catalogo.getPrecoPizza(indiceSabor, tamanho);

                pedido.adicionarPizza(
                        indiceSabor, tamanho, quantidade
                );

                JOptionPane.showMessageDialog(
                        null,
                        "Pizza adicionada, " + nomeCliente + "!\n"
                        + saborEscolhido
                        + " - tamanho " + tamanho
                        + "\nQuantidade: " + quantidade
                        + "\nPreço unitário: R$ "
                        + String.format("%.2f", precoUnitario)
                );

            } catch (NumberFormatException erro) {
                JOptionPane.showMessageDialog(
                        null,
                        "Digite uma quantidade válida."
                );
            }
        }

        // CARDÁPIO DE BEBIDAS
        continuar = true;

        while (continuar) {

            String[] bebidas =
                    new String[catalogo.getQuantidadeBebidas()];

            for (int i = 0; i < bebidas.length; i++) {
                bebidas[i] = catalogo.getBebida(i);
            }

            JComboBox<String> comboBebidas =
                    new JComboBox<>(bebidas);

            JTextField campoQuantidade = new JTextField("1");

            JPanel painelBebidas = new JPanel(
                    new GridLayout(0, 1, 5, 5)
            );

            painelBebidas.add(new JLabel("Escolha sua bebida:"));
            painelBebidas.add(comboBebidas);
            painelBebidas.add(new JLabel("Quantidade:"));
            painelBebidas.add(campoQuantidade);

            Object[] botoesBebidas = {
                "Adicionar bebida", "Próximo"
            };

            int resultado = JOptionPane.showOptionDialog(
                    null,
                    painelBebidas,
                    "Cardápio de bebidas",
                    JOptionPane.DEFAULT_OPTION,
                    JOptionPane.QUESTION_MESSAGE,
                    null,
                    botoesBebidas,
                    botoesBebidas[0]
            );

            if (resultado != 0) {
                continuar = false;
                continue;
            }

            try {
                int indice = comboBebidas.getSelectedIndex();

                int quantidade = Integer.parseInt(
                        campoQuantidade.getText().trim()
                );

                if (quantidade <= 0) {
                    JOptionPane.showMessageDialog(
                            null,
                            "Informe uma quantidade maior que zero."
                    );
                    continue;
                }

                pedido.adicionarBebida(indice, quantidade);

                JOptionPane.showMessageDialog(
                        null,
                        "Bebida adicionada, " + nomeCliente + "!\n"
                        + bebidas[indice]
                        + "\nQuantidade: " + quantidade
                        + "\nPreço unitário: R$ "
                        + String.format(
                                "%.2f",
                                catalogo.getPrecoBebida(indice)
                        )
                );

            } catch (NumberFormatException erro) {
                JOptionPane.showMessageDialog(
                        null,
                        "Digite uma quantidade válida."
                );
            }
        }

        // CARDÁPIO DE GULOSEIMAS
        continuar = true;

        while (continuar) {

            String[] guloseimas =
                    new String[catalogo.getQuantidadeGuloseimas()];

            for (int i = 0; i < guloseimas.length; i++) {
                guloseimas[i] = catalogo.getGuloseima(i);
            }

            JComboBox<String> comboGuloseimas =
                    new JComboBox<>(guloseimas);

            JTextField campoQuantidade = new JTextField("1");

            JPanel painelGuloseimas = new JPanel(
                    new GridLayout(0, 1, 5, 5)
            );

            painelGuloseimas.add(
                    new JLabel("Escolha sua guloseima:")
            );
            painelGuloseimas.add(comboGuloseimas);
            painelGuloseimas.add(new JLabel("Quantidade:"));
            painelGuloseimas.add(campoQuantidade);

            Object[] botoesGuloseimas = {
                "Adicionar guloseima", "Finalizar pedido"
            };

            int resultado = JOptionPane.showOptionDialog(
                    null,
                    painelGuloseimas,
                    "Cardápio de guloseimas",
                    JOptionPane.DEFAULT_OPTION,
                    JOptionPane.QUESTION_MESSAGE,
                    null,
                    botoesGuloseimas,
                    botoesGuloseimas[0]
            );

            if (resultado != 0) {
                continuar = false;
                continue;
            }

            try {
                int indice = comboGuloseimas.getSelectedIndex();

                int quantidade = Integer.parseInt(
                        campoQuantidade.getText().trim()
                );

                if (quantidade <= 0) {
                    JOptionPane.showMessageDialog(
                            null,
                            "Informe uma quantidade maior que zero."
                    );
                    continue;
                }

                pedido.adicionarGuloseima(indice, quantidade);

                JOptionPane.showMessageDialog(
                        null,
                        "Guloseima adicionada, " + nomeCliente + "!\n"
                        + guloseimas[indice]
                        + "\nQuantidade: " + quantidade
                        + "\nPreço unitário: R$ "
                        + String.format(
                                "%.2f",
                                catalogo.getPrecoGuloseima(indice)
                        )
                );

            } catch (NumberFormatException erro) {
                JOptionPane.showMessageDialog(
                        null,
                        "Digite uma quantidade válida."
                );
            }
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

        // CAIXA E PAGAMENTO
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
