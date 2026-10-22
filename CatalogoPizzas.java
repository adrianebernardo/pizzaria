
package pizzaria;

public class CatalogoPizzas {

    private final String[] sabores = {
        "Frango com Catupiri",
        "Seis queijos",
        "Calabresa",
        "Vegana Especial Carlos Tadeu",
        "Portuguesa",
        "Marguerita",
        "Nutella com Morango"
    };
    
    public String montarCardapioBebidas() {
    StringBuilder cardapio = new StringBuilder();

    for (int i = 0; i < getQuantidadeBebidas(); i++) {
        cardapio.append(i + 1)
                .append(" - ")
                .append(getBebida(i))
                .append(" - R$ ")
                .append(String.format("%.2f", getPrecoBebida(i)))
                .append("\n");
    }

    return cardapio.toString();
}

public String montarCardapioGuloseimas() {
    StringBuilder cardapio = new StringBuilder();

    for (int i = 0; i < getQuantidadeGuloseimas(); i++) {
        cardapio.append(i + 1)
                .append(" - ")
                .append(getGuloseima(i))
                .append(" - R$ ")
                .append(String.format("%.2f", getPrecoGuloseima(i)))
                .append("\n");
    }

    return cardapio.toString();
}


    private final double[] precosBase = {
        40.00, 45.00, 35.00, 42.00, 38.00, 36.00, 45.00
    };

    private final String[] tamanhos = {"B", "P", "M", "G", "GG"};

   
    private final double[] multiplicadores = {
        0.65, 0.80, 1.00, 1.25, 1.50
    };

    private final String[] bebidas = {
        "Coca-Cola Zero",
        "Água SEM gás",
        "Suco de Maracujá Geladinho"
    };

    private final double[] precosBebidas = {8.00, 4.00, 7.00};

    private final String[] guloseimas = {
        "KitKat",
        "Snicker de Avelã",
        "Bala Fini Minhoquinha"
    };

    private final double[] precosGuloseimas = {6.00, 7.00, 4.00};

    public int getQuantidadeSabores() {
        return sabores.length;
    }

    public String getSabor(int indice) {
        return sabores[indice];
    }

    public String[] getSabores() {
        return sabores.clone();
    }

    public String[] getTamanhos() {
        return tamanhos.clone();
    }

    public double getPrecoPizza(int indice, String tamanho) {
        for (int i = 0; i < tamanhos.length; i++) {
            if (tamanhos[i].equals(tamanho)) {
                return precosBase[indice] * multiplicadores[i];
            }
        }

        return 0.0;
    }

    public int getQuantidadeBebidas() {
        return bebidas.length;
    }

    public String getBebida(int indice) {
        return bebidas[indice];
    }

    public double getPrecoBebida(int indice) {
        return precosBebidas[indice];
    }

    public int getQuantidadeGuloseimas() {
        return guloseimas.length;
    }

    public String getGuloseima(int indice) {
        return guloseimas[indice];
    }

    public double getPrecoGuloseima(int indice) {
        return precosGuloseimas[indice];
    }
}
