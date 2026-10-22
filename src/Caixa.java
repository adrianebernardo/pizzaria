package pizzaria;

import javax.swing.JOptionPane;
import java.util.List;

public class Caixa {

    public double calcularTotal(Pedido pedido, CatalogoPizzas catalogo) {
        return pedido.calcularTotal(catalogo);
    }

    private String dinheiro(double valor) {
        return "R$ " + String.format("%.2f", valor);
    }

    private double arredondar(double valor) {
        return Math.round(valor * 100.0) / 100.0;
    }

    public void finalizarCompra(String nomeCliente, double total) {

        List<String> metodos = TelaPagamento.escolher(nomeCliente, total);

        if (metodos == null) {
            JOptionPane.showMessageDialog(null, "Pagamento cancelado.");
            return;
        }

        StringBuilder recibo = new StringBuilder();
        recibo.append("=== PAGAMENTO APROVADO ===\n\n")
                .append("Obrigada pela preferência, ").append(nomeCliente).append("!\n")
                .append("Total: ").append(dinheiro(total)).append("\n\n");

        double restante = total;
        double troco = 0.0;

        for (int i = 0; i < metodos.size(); i++) {
            String metodo = metodos.get(i);
            double parte;

            if (i == metodos.size() - 1) {
                // a última forma paga o que restou
                parte = arredondar(restante);
            } else {
                parte = pedirParte(metodo, restante);
                if (parte < 0) {
                    JOptionPane.showMessageDialog(null, "Pagamento cancelado.");
                    return;
                }
            }

            if (metodo.equals("Dinheiro")) {
                double recebido = pedirDinheiro(parte);
                if (recebido < 0) {
                    JOptionPane.showMessageDialog(null, "Pagamento cancelado.");
                    return;
                }
                troco += recebido - parte;
                recibo.append(metodo).append(": ").append(dinheiro(parte))
                        .append(" (recebido ").append(dinheiro(recebido)).append(")\n");
            } else {
                recibo.append(metodo).append(": ").append(dinheiro(parte)).append("\n");
            }

            restante = arredondar(restante - parte);
        }

        if (troco > 0) {
            recibo.append("\nTroco: ").append(dinheiro(troco));
        }

        JOptionPane.showMessageDialog(null, recibo.toString(),
                "Pagamento", JOptionPane.INFORMATION_MESSAGE);
    }

    // Pergunta quanto será pago em uma forma (quando há mais de uma marcada).
    // Retorna -1 se cancelar.
    private double pedirParte(String metodo, double restante) {
        while (true) {
            String texto = JOptionPane.showInputDialog(
                    null,
                    "Quanto será pago em " + metodo + "?\n"
                    + "Restante a pagar: " + dinheiro(restante));

            if (texto == null) {
                return -1;
            }

            try {
                double valor = arredondar(
                        Double.parseDouble(texto.trim().replace(",", ".")));

                if (valor <= 0 || valor >= restante) {
                    JOptionPane.showMessageDialog(null,
                            "Informe um valor maior que zero e menor que "
                            + dinheiro(restante)
                            + ", pois ainda há outra forma marcada.");
                    continue;
                }

                return valor;

            } catch (NumberFormatException erro) {
                JOptionPane.showMessageDialog(null,
                        "Digite um valor válido. Exemplo: 50,00");
            }
        }
    }

    // Pergunta quanto o cliente entregou em dinheiro. Retorna -1 se cancelar.
    private double pedirDinheiro(double parte) {
        while (true) {
            String texto = JOptionPane.showInputDialog(
                    null,
                    "Parte em dinheiro: " + dinheiro(parte)
                    + "\n\nDigite o valor recebido em dinheiro:");

            if (texto == null) {
                return -1;
            }

            try {
                double recebido = Double.parseDouble(
                        texto.trim().replace(",", "."));

                if (recebido < parte) {
                    JOptionPane.showMessageDialog(null,
                            "Valor insuficiente!\nAinda faltam "
                            + dinheiro(parte - recebido));
                    continue;
                }

                return recebido;

            } catch (NumberFormatException erro) {
                JOptionPane.showMessageDialog(null,
                        "Digite um valor válido. Exemplo: 50,00");
            }
        }
    }
}