
package pizzaria;

import java.util.ArrayList;
import java.util.List;

public class Pedido {

    private static class ItemPizza {
        int indiceSabor;
        String tamanho;
        int quantidade;

        ItemPizza(int indiceSabor, String tamanho, int quantidade) {
            this.indiceSabor = indiceSabor;
            this.tamanho = tamanho;
            this.quantidade = quantidade;
        }
    }

    private final List<ItemPizza> pizzas = new ArrayList<>();

    private final int[] quantidadesBebidas = new int[3];
    private final int[] quantidadesGuloseimas = new int[3];

    public void adicionarPizza(
            int indiceSabor, String tamanho, int quantidade) {

        if (quantidade <= 0) {
            return;
        }

        pizzas.add(new ItemPizza(indiceSabor, tamanho, quantidade));
    }

    public void adicionarBebida(int indice, int quantidade) {
        if (indice >= 0 && indice < quantidadesBebidas.length
                && quantidade > 0) {
            quantidadesBebidas[indice] += quantidade;
        }
    }

    public void adicionarGuloseima(int indice, int quantidade) {
        if (indice >= 0 && indice < quantidadesGuloseimas.length
                && quantidade > 0) {
            quantidadesGuloseimas[indice] += quantidade;
        }
    }

    public double calcularTotal(CatalogoPizzas catalogo) {
        double total = 0.0;

        for (ItemPizza pizza : pizzas) {
            total += catalogo.getPrecoPizza(
                    pizza.indiceSabor, pizza.tamanho)
                    * pizza.quantidade;
        }

        for (int i = 0; i < quantidadesBebidas.length; i++) {
            total += catalogo.getPrecoBebida(i)
                    * quantidadesBebidas[i];
        }

        for (int i = 0; i < quantidadesGuloseimas.length; i++) {
            total += catalogo.getPrecoGuloseima(i)
                    * quantidadesGuloseimas[i];
        }

        return total;
    }

    public String resumo(CatalogoPizzas catalogo) {
        StringBuilder texto = new StringBuilder();

        texto.append("=== RESUMO DO PEDIDO ===\n\n");

        if (pizzas.isEmpty()) {
            texto.append("Nenhuma pizza escolhida.\n");
        }

        for (ItemPizza pizza : pizzas) {
            double preco = catalogo.getPrecoPizza(
                    pizza.indiceSabor, pizza.tamanho);

            texto.append(catalogo.getSabor(pizza.indiceSabor))
                    .append(" - tamanho ").append(pizza.tamanho)
                    .append(" | Quantidade: ").append(pizza.quantidade)
                    .append(" | Subtotal: R$ ")
                    .append(String.format("%.2f", preco * pizza.quantidade))
                    .append("\n");
        }

        texto.append("\n--- Bebidas ---\n");

        for (int i = 0; i < quantidadesBebidas.length; i++) {
            if (quantidadesBebidas[i] > 0) {
                texto.append(catalogo.getBebida(i))
                        .append(" x ").append(quantidadesBebidas[i])
                        .append("\n");
            }
        }

        texto.append("\n--- Guloseimas ---\n");

        for (int i = 0; i < quantidadesGuloseimas.length; i++) {
            if (quantidadesGuloseimas[i] > 0) {
                texto.append(catalogo.getGuloseima(i))
                        .append(" x ").append(quantidadesGuloseimas[i])
                        .append("\n");
            }
        }

        return texto.toString();
    }
}
