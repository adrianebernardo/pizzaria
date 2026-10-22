package pizzaria;

import javax.swing.*;
import java.awt.*;

// Tela genérica com checkbox + preço + quantidade (serve para bebidas e guloseimas)
public class TelaItens {

    // Retorna as quantidades de cada item (0 = não marcado) ou null se o cliente fechou a janela.
    public static int[] mostrar(String titulo, String[] nomes, double[] precos,
            String nomeCliente, double totalAnterior, String textoBotao) {

        int n = nomes.length;

        JCheckBox[] checks = new JCheckBox[n];
        JSpinner[] quantidades = new JSpinner[n];

        JLabel labelEtapa = new JLabel("Total desta etapa: R$ 0,00", SwingConstants.RIGHT);
        labelEtapa.setFont(new Font("SansSerif", Font.BOLD, 16));

        JLabel labelPedido = new JLabel(
                "Total do pedido até agora (sem frete): R$ " + String.format("%.2f", totalAnterior),
                SwingConstants.RIGHT);

        Runnable atualizar = () -> {
            double etapa = 0.0;
            for (int i = 0; i < n; i++) {
                if (checks[i].isSelected()) {
                    etapa += precos[i] * (Integer) quantidades[i].getValue();
                }
            }
            labelEtapa.setText("Total desta etapa: R$ " + String.format("%.2f", etapa));
            labelPedido.setText("Total do pedido até agora (sem frete): R$ "
                    + String.format("%.2f", totalAnterior + etapa));
        };

        JPanel grade = new JPanel(new GridLayout(n + 1, 3, 10, 8));
        grade.setBorder(BorderFactory.createTitledBorder("Marque o que deseja"));

        grade.add(new JLabel("Item"));
        grade.add(new JLabel("Preço unitário"));
        grade.add(new JLabel("Quantidade"));

        for (int i = 0; i < n; i++) {
            final int fi = i;

            checks[i] = new JCheckBox(nomes[i]);
            quantidades[i] = new JSpinner(new SpinnerNumberModel(1, 1, 50, 1));
            quantidades[i].setEnabled(false);

            checks[i].addActionListener(e -> {
                quantidades[fi].setEnabled(checks[fi].isSelected());
                atualizar.run();
            });
            quantidades[i].addChangeListener(e -> atualizar.run());

            grade.add(checks[i]);
            grade.add(new JLabel("R$ " + String.format("%.2f", precos[i])));
            grade.add(quantidades[i]);
        }

        JButton botao = new JButton(textoBotao);

        int[] resultado = new int[n];
        boolean[] confirmou = {false};

        JDialog dialogo = new JDialog((Frame) null, titulo + " - " + nomeCliente, true);

        botao.addActionListener(e -> {
            confirmou[0] = true;
            for (int i = 0; i < n; i++) {
                resultado[i] = checks[i].isSelected()
                        ? (Integer) quantidades[i].getValue() : 0;
            }
            dialogo.dispose();
        });

        JPanel totais = new JPanel(new GridLayout(2, 1, 0, 4));
        totais.add(labelEtapa);
        totais.add(labelPedido);

        JPanel rodape = new JPanel(new BorderLayout(10, 10));
        rodape.add(totais, BorderLayout.CENTER);
        rodape.add(botao, BorderLayout.EAST);

        JPanel raiz = new JPanel(new BorderLayout(10, 10));
        raiz.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
        raiz.add(grade, BorderLayout.CENTER);
        raiz.add(rodape, BorderLayout.SOUTH);

        dialogo.setContentPane(raiz);
        dialogo.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        dialogo.pack();
        dialogo.setLocationRelativeTo(null);
        dialogo.setVisible(true);

        return confirmou[0] ? resultado : null;
    }
}