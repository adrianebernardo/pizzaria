
package pizzaria;

import javax.swing.JOptionPane;

public class Caixa {

    public double calcularTotal(Pedido pedido, CatalogoPizzas catalogo) {
        return pedido.calcularTotal(catalogo);
    }

    public void finalizarCompra(String nomeCliente, double total) {
        while (true) {
            String pagamento = JOptionPane.showInputDialog(
                    null,
                    "Cliente: " + nomeCliente
                    + "\nTotal da compra: R$ "
                    + String.format("%.2f", total)
                    + "\n\nDigite o valor recebido em dinheiro:");

            if (pagamento == null) {
                JOptionPane.showMessageDialog(
                        null, "Pagamento cancelado.");
                return;
            }

            try {
                double valorRecebido = Double.parseDouble(
                        pagamento.replace(",", "."));

                if (valorRecebido < total) {
                    JOptionPane.showMessageDialog(
                            null,
                            "Valor insuficiente!\n"
                            + "Ainda faltam R$ "
                            + String.format(
                                    "%.2f", total - valorRecebido));
                    continue;
                }

                double troco = valorRecebido - total;

                JOptionPane.showMessageDialog(
                        null,
                        "=== PAGAMENTO APROVADO ===\n\n"
                        + "Obrigada pela preferência, "
                        + nomeCliente + "!\n"
                        + "Total: R$ " + String.format("%.2f", total)
                        + "\nRecebido: R$ "
                        + String.format("%.2f", valorRecebido)
                        + "\nTroco: R$ "
                        + String.format("%.2f", troco));

                return;

            } catch (NumberFormatException erro) {
                JOptionPane.showMessageDialog(
                        null,
                        "Digite um valor válido. Exemplo: 50,00");
            }
        }
    }
}
