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

    private final String[] tamanhos = {"B", "P", "M", "G", "GG"};

    private final String[] nomesTamanhos = {
        "Broto (B)", "Pequena (P)", "Média (M)", "Grande (G)", "Gigante (GG)"
    };

    // PREÇOS FIXOS: cada linha é um sabor (mesma ordem de "sabores"),
    // cada coluna é um tamanho (mesma ordem de "tamanhos": B, P, M, G, GG)
    private final double[][] precosPizzas = {
        {26.00, 32.00, 40.00, 50.00, 60.00},   // Frango com Catupiri
        {29.90, 36.00, 45.00, 56.00, 67.00},   // Seis queijos
        {22.90, 28.00, 35.00, 44.00, 52.00},   // Calabresa
        {27.90, 33.00, 42.00, 52.00, 63.00},   // Vegana Especial Carlos Tadeu
        {24.90, 30.00, 38.00, 47.00, 57.00},   // Portuguesa
        {23.90, 29.00, 36.00, 45.00, 54.00},   // Marguerita
        {29.90, 36.00, 45.00, 56.00, 67.00}    // Nutella com Morango
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

    // ---------- PIZZAS ----------
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

    public String[] getNomesTamanhos() {
        return nomesTamanhos.clone();
    }

    public double getPrecoPizza(int indice, String tamanho) {
        for (int i = 0; i < tamanhos.length; i++) {
            if (tamanhos[i].equals(tamanho)) {
                return precosPizzas[indice][i];
            }
        }
        return 0.0;
    }

    // ---------- BEBIDAS ----------
    public int getQuantidadeBebidas() {
        return bebidas.length;
    }

    public String getBebida(int indice) {
        return bebidas[indice];
    }

    public String[] getBebidas() {
        return bebidas.clone();
    }

    public double getPrecoBebida(int indice) {
        return precosBebidas[indice];
    }

    public double[] getPrecosBebidas() {
        return precosBebidas.clone();
    }

    // ---------- GULOSEIMAS ----------
    public int getQuantidadeGuloseimas() {
        return guloseimas.length;
    }

    public String getGuloseima(int indice) {
        return guloseimas[indice];
    }

    public String[] getGuloseimas() {
        return guloseimas.clone();
    }

    public double getPrecoGuloseima(int indice) {
        return precosGuloseimas[indice];
    }

    public double[] getPrecosGuloseimas() {
        return precosGuloseimas.clone();
    }
}