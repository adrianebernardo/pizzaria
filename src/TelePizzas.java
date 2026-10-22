package pizzaria;

import javax.swing.*;
import java.awt.*;

public class TelePizzas {

    // Mostra a tabela de sabores x tamanhos (modal).
    // Cada célula tem um checkbox com o preço fixo e a quantidade.
    // Retorna true se o cliente clicou em "Seguinte".
    public static boolean mostrar(CatalogoPizzas catalogo, Pedido pedido, String nomeCliente) {

        int nSabores = catalogo.getQuantidadeSabores();
        String[] tamanhos = catalogo.getTamanhos();
        String[] nomesTamanhos = catalogo.getNomesTamanhos();
        int nTamanhos = tamanhos.length;

        JCheckBox[][] checks = new JCheckBox[nSabores][nTamanhos];
        JSpinner[][] quantidades = new JSpinner[nSabores][nTamanhos];

        JLabel labelTotal = new JLabel("Total das pizzas: R$ 0,00", SwingConstants.RIGHT);
        labelTotal.setFont(new Font("SansSerif", Font.BOLD, 16));

        // Recalcula o total sempre que algo muda
        Runnable atualizar = () -> {
            double total = 0.0;
            for (int i = 0; i < nSabores; i++) {
                for (int j = 0; j < nTamanhos; j++) {
                    if (checks[i][j].isSelected()) {
                        total += catalogo.getPrecoPizza(i, tamanhos[j])
                                * (Integer) quantidades[i][j].getValue();
                    }
                }
            }
            labelTotal.setText("Total das pizzas: R$ " + String.format("%.2f", total));
        };

        // Grade: 1ª linha = cabeçalho com os tamanhos; 1ª coluna = sabores
        JPanel grade = new JPanel(new GridLayout(nSabores + 1, nTamanhos + 1, 8, 8));
        grade.setBorder(BorderFactory.createTitledBorder(
                "Marque os sabores e tamanhos (pode marcar o mesmo sabor em tamanhos diferentes)"));

        grade.add(new JLabel("Sabor"));
        for (int j = 0; j < nTamanhos; j++) {
            JLabel cab = new JLabel(nomesTamanhos[j]);
            cab.setFont(cab.getFont().deriveFont(Font.BOLD));
            grade.add(cab);
        }

        for (int i = 0; i < nSabores; i++) {
            grade.add(new JLabel(catalogo.getSabor(i)));

            for (int j = 0; j < nTamanhos; j++) {
                final int fi = i;
                final int fj = j;

                double preco = catalogo.getPrecoPizza(i, tamanhos[j]);

                checks[i][j] = new JCheckBox("R$ " + String.format("%.2f", preco));
                quantidades[i][j] = new JSpinner(new SpinnerNumberModel(1, 1, Integer.MAX_VALUE, 1));
                quantidades[i][j].setPreferredSize(new Dimension(60, 24));
                quantidades[i][j].setEnabled(false);

                // Permite digitar a quantidade: o total atualiza enquanto digita
                JFormattedTextField campoQtd =
                        ((JSpinner.DefaultEditor) quantidades[i][j].getEditor()).getTextField();
                ((javax.swing.text.NumberFormatter) campoQtd.getFormatter())
                        .setCommitsOnValidEdit(true);

                checks[i][j].addActionListener(e -> {
                    quantidades[fi][fj].setEnabled(checks[fi][fj].isSelected());
                    atualizar.run();
                });
                quantidades[i][j].addChangeListener(e -> atualizar.run());

                JPanel celula = new JPanel(new FlowLayout(FlowLayout.LEFT, 4, 0));
                celula.add(checks[i][j]);
                celula.add(quantidades[i][j]);
                grade.add(celula);
            }
        }

        JLabel dica = new JLabel(
                "<html>Os preços são fixos por tamanho. O campo ao lado de cada preço é a quantidade.</html>");

        JButton botaoSeguinte = new JButton("Seguinte");

        boolean[] confirmou = {false};

        JDialog dialogo = new JDialog((Frame) null,
                "Cardápio de pizzas - " + nomeCliente, true);

        botaoSeguinte.addActionListener(e -> {
            confirmou[0] = true;
            for (int i = 0; i < nSabores; i++) {
                for (int j = 0; j < nTamanhos; j++) {
                    if (checks[i][j].isSelected()) {
                        pedido.adicionarPizza(
                                i, tamanhos[j],
                                (Integer) quantidades[i][j].getValue());
                    }
                }
            }
            dialogo.dispose();
        });

        JPanel rodape = new JPanel(new BorderLayout(10, 10));
        rodape.add(labelTotal, BorderLayout.CENTER);
        rodape.add(botaoSeguinte, BorderLayout.EAST);

        JPanel raiz = new JPanel(new BorderLayout(10, 10));
        raiz.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
        raiz.add(dica, BorderLayout.NORTH);
        raiz.add(grade, BorderLayout.CENTER);
        raiz.add(rodape, BorderLayout.SOUTH);

        dialogo.setContentPane(raiz);
        dialogo.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        dialogo.pack();
        dialogo.setLocationRelativeTo(null);
        dialogo.setVisible(true); // bloqueia até fechar (modal)

        return confirmou[0];
    }
}