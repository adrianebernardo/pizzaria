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

    // Mesmo sabor em tamanhos diferentes vira linhas diferentes;
    // mesmo sabor e mesmo tamanho soma a quantidade.
    public void adicionarPizza(
            int indiceSabor, String tamanho, int quantidade) {

        if (quantidade <= 0) {
            return;
        }

        for (ItemPizza item : pizzas) {
            if (item.indiceSabor == indiceSabor
                    && item.tamanho.equals(tamanho)) {
                item.quantidade += quantidade;
                return;
            }
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

    private String dinheiro(double valor) {
        return "R$ " + String.format("%.2f", valor);
    }

    public String resumo(CatalogoPizzas catalogo) {
        StringBuilder texto = new StringBuilder();

        texto.append("=== RESUMO DO PEDIDO ===\n");

        // PIZZAS
        texto.append("\n--- Pizzas ---\n");
        double totalPizzas = 0.0;

        if (pizzas.isEmpty()) {
            texto.append("Nenhuma pizza escolhida.\n");
        }

        for (ItemPizza pizza : pizzas) {
            double unit = catalogo.getPrecoPizza(
                    pizza.indiceSabor, pizza.tamanho);
            double sub = unit * pizza.quantidade;
            totalPizzas += sub;

            texto.append(catalogo.getSabor(pizza.indiceSabor))
                    .append(" (").append(pizza.tamanho).append(")")
                    .append(" - ").append(pizza.quantidade)
                    .append(" x ").append(dinheiro(unit))
                    .append(" = ").append(dinheiro(sub))
                    .append("\n");
        }

        if (!pizzas.isEmpty()) {
            texto.append("Total em pizzas: ")
                    .append(dinheiro(totalPizzas)).append("\n");
        }

        // BEBIDAS
        texto.append("\n--- Bebidas ---\n");
        double totalBebidas = 0.0;
        boolean temBebida = false;

        for (int i = 0; i < quantidadesBebidas.length; i++) {
            if (quantidadesBebidas[i] > 0) {
                temBebida = true;
                double unit = catalogo.getPrecoBebida(i);
                double sub = unit * quantidadesBebidas[i];
                totalBebidas += sub;

                texto.append(catalogo.getBebida(i))
                        .append(" - ").append(quantidadesBebidas[i])
                        .append(" x ").append(dinheiro(unit))
                        .append(" = ").append(dinheiro(sub))
                        .append("\n");
            }
        }

        if (temBebida) {
            texto.append("Total em bebidas: ")
                    .append(dinheiro(totalBebidas)).append("\n");
        } else {
            texto.append("Nenhuma bebida escolhida.\n");
        }

        // GULOSEIMAS
        texto.append("\n--- Guloseimas ---\n");
        double totalGuloseimas = 0.0;
        boolean temGuloseima = false;

        for (int i = 0; i < quantidadesGuloseimas.length; i++) {
            if (quantidadesGuloseimas[i] > 0) {
                temGuloseima = true;
                double unit = catalogo.getPrecoGuloseima(i);
                double sub = unit * quantidadesGuloseimas[i];
                totalGuloseimas += sub;

                texto.append(catalogo.getGuloseima(i))
                        .append(" - ").append(quantidadesGuloseimas[i])
                        .append(" x ").append(dinheiro(unit))
                        .append(" = ").append(dinheiro(sub))
                        .append("\n");
            }
        }

        if (temGuloseima) {
            texto.append("Total em guloseimas: ")
                    .append(dinheiro(totalGuloseimas)).append("\n");
        } else {
            texto.append("Nenhuma guloseima escolhida.\n");
        }

        return texto.toString();
    }
}