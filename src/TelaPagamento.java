package pizzaria;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class TelaPagamento {

    // Mostra as formas de pagamento em checkbox.
    // Retorna a lista das formas marcadas ou null se o cliente fechou a janela.
    public static List<String> escolher(String nomeCliente, double total) {

        String[] formas = {
            "Dinheiro",
            "Pix",
            "Cartão de débito",
            "Cartão de crédito",
            "Vale-refeição"
        };

        JCheckBox[] checks = new JCheckBox[formas.length];

        JPanel painelFormas = new JPanel(new GridLayout(formas.length, 1, 0, 6));
        painelFormas.setBorder(BorderFactory.createTitledBorder("Formas de pagamento"));

        for (int i = 0; i < formas.length; i++) {
            checks[i] = new JCheckBox(formas[i]);
            painelFormas.add(checks[i]);
        }

        JLabel labelTotal = new JLabel(
                "Total a pagar: R$ " + String.format("%.2f", total));
        labelTotal.setFont(new Font("SansSerif", Font.BOLD, 16));

        JLabel dica = new JLabel(
                "<html>Marque uma ou mais formas. Se marcar mais de uma, "
                + "você informa quanto vai pagar em cada.</html>");

        JButton botaoPagar = new JButton("Pagar");

        List<String> escolhidas = new ArrayList<>();
        boolean[] confirmou = {false};

        JDialog dialogo = new JDialog((Frame) null, "Pagamento - " + nomeCliente, true);

        botaoPagar.addActionListener(e -> {
            escolhidas.clear();
            for (int i = 0; i < formas.length; i++) {
                if (checks[i].isSelected()) {
                    escolhidas.add(formas[i]);
                }
            }

            if (escolhidas.isEmpty()) {
                JOptionPane.showMessageDialog(dialogo,
                        "Marque pelo menos uma forma de pagamento.");
                return;
            }

            confirmou[0] = true;
            dialogo.dispose();
        });

        JPanel topo = new JPanel(new GridLayout(2, 1, 0, 6));
        topo.add(labelTotal);
        topo.add(dica);

        JPanel raiz = new JPanel(new BorderLayout(10, 10));
        raiz.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
        raiz.add(topo, BorderLayout.NORTH);
        raiz.add(painelFormas, BorderLayout.CENTER);
        raiz.add(botaoPagar, BorderLayout.SOUTH);

        dialogo.setContentPane(raiz);
        dialogo.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        dialogo.pack();
        dialogo.setLocationRelativeTo(null);
        dialogo.setVisible(true);

        return confirmou[0] ? escolhidas : null;
    }
}