package pizzaria;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.*;
import java.awt.datatransfer.StringSelection;
import java.util.Random;

// Telas de pagamento FICTÍCIAS (simulação): dinheiro, Pix e cartões.
public class Telametodos {

    private static String dinheiro(double valor) {
        return "R$ " + String.format("%.2f", valor);
    }

    private static DocumentListener ouvinte(Runnable acao) {
        return new DocumentListener() {
            public void insertUpdate(DocumentEvent e) { acao.run(); }
            public void removeUpdate(DocumentEvent e) { acao.run(); }
            public void changedUpdate(DocumentEvent e) { acao.run(); }
        };
    }

    private static void abrir(JDialog dialogo, JPanel raiz, JButton padrao) {
        dialogo.setContentPane(raiz);
        if (padrao != null) {
            dialogo.getRootPane().setDefaultButton(padrao);
        }
        dialogo.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        dialogo.pack();
        dialogo.setLocationRelativeTo(null);
        dialogo.setVisible(true); // modal: espera fechar
    }

    // ---------------------------------------------------------------
    // DINHEIRO: digita o valor recebido e vê o troco na hora.
    // Retorna o valor recebido ou null se cancelar.
    // ---------------------------------------------------------------
    public static Double pagarDinheiro(String nomeCliente, double valor) {

        JLabel labelValor = new JLabel("Valor a pagar em dinheiro: " + dinheiro(valor));
        labelValor.setFont(new Font("SansSerif", Font.BOLD, 16));

        JTextField campo = new JTextField(10);
        JLabel labelTroco = new JLabel(" ");

        JButton confirmar = new JButton("Confirmar pagamento");
        JButton cancelar = new JButton("Cancelar");
        confirmar.setEnabled(false);

        Double[] resultado = {null};

        JDialog dialogo = new JDialog((Frame) null,
                "Pagamento em dinheiro - " + nomeCliente, true);

        Runnable atualizar = () -> {
            try {
                double recebido = Double.parseDouble(
                        campo.getText().trim().replace(",", "."));

                if (recebido >= valor - 0.001) {
                    double troco = Math.max(0.0, recebido - valor);
                    labelTroco.setText("Troco: " + dinheiro(troco));
                    confirmar.setEnabled(true);
                } else {
                    labelTroco.setText("Faltam " + dinheiro(valor - recebido));
                    confirmar.setEnabled(false);
                }
            } catch (NumberFormatException erro) {
                labelTroco.setText("Digite o valor recebido. Exemplo: 50,00");
                confirmar.setEnabled(false);
            }
        };

        campo.getDocument().addDocumentListener(ouvinte(atualizar));
        atualizar.run();

        confirmar.addActionListener(e -> {
            resultado[0] = Double.parseDouble(
                    campo.getText().trim().replace(",", "."));
            dialogo.dispose();
        });
        cancelar.addActionListener(e -> dialogo.dispose());

        JPanel centro = new JPanel(new GridLayout(0, 1, 5, 5));
        centro.add(new JLabel("Valor recebido do cliente:"));
        centro.add(campo);
        centro.add(labelTroco);

        JPanel botoes = new JPanel(new GridLayout(1, 2, 10, 0));
        botoes.add(cancelar);
        botoes.add(confirmar);

        JPanel raiz = new JPanel(new BorderLayout(10, 15));
        raiz.setBorder(BorderFactory.createEmptyBorder(20, 25, 20, 25));
        raiz.add(labelValor, BorderLayout.NORTH);
        raiz.add(centro, BorderLayout.CENTER);
        raiz.add(botoes, BorderLayout.SOUTH);

        abrir(dialogo, raiz, confirmar);

        return resultado[0];
    }

    // ---------------------------------------------------------------
    // PIX: QR Code e código "copia e cola" FALSOS.
    // Retorna o texto da transação ou null se cancelar.
    // ---------------------------------------------------------------
    public static String pagarPix(String nomeCliente, double valor) {

        long idNumero = 10000000L + (long) (new Random().nextDouble() * 89999999L);
        String id = String.valueOf(idNumero);

        String codigo = "00020126FICTICIO-PIZZARIA-BERNARDES-" + id
                + "-VALOR-" + String.format("%.2f", valor).replace(",", ".")
                + "-6304ABCD";

        JLabel titulo = new JLabel("Pague " + dinheiro(valor) + " com Pix",
                SwingConstants.CENTER);
        titulo.setFont(new Font("SansSerif", Font.BOLD, 16));

        QrFalso qr = new QrFalso(idNumero);
        JPanel painelQr = new JPanel(new FlowLayout(FlowLayout.CENTER));
        painelQr.add(qr);

        JLabel aviso = new JLabel(
                "QR Code e código FICTÍCIOS (simulação, não funcionam de verdade)",
                SwingConstants.CENTER);
        aviso.setFont(aviso.getFont().deriveFont(Font.ITALIC));

        JTextField campoCodigo = new JTextField(codigo);
        campoCodigo.setEditable(false);
        campoCodigo.setCaretPosition(0);

        JPanel rodapeCodigo = new JPanel(new GridLayout(0, 1, 5, 5));
        rodapeCodigo.add(aviso);
        rodapeCodigo.add(new JLabel("Pix copia e cola:"));
        rodapeCodigo.add(campoCodigo);

        JButton copiar = new JButton("Copiar código");
        JButton paguei = new JButton("Já paguei (simular)");
        JButton cancelar = new JButton("Cancelar");

        String[] resultado = {null};

        JDialog dialogo = new JDialog((Frame) null,
                "Pagamento via Pix - " + nomeCliente, true);

        copiar.addActionListener(e -> {
            try {
                Toolkit.getDefaultToolkit().getSystemClipboard()
                        .setContents(new StringSelection(codigo), null);
                copiar.setText("Copiado!");
            } catch (Exception erro) {
                copiar.setText("Não foi possível copiar");
            }
        });
        paguei.addActionListener(e -> {
            resultado[0] = "Pix, transação " + id;
            dialogo.dispose();
        });
        cancelar.addActionListener(e -> dialogo.dispose());

        JPanel botoes = new JPanel(new GridLayout(1, 3, 10, 0));
        botoes.add(cancelar);
        botoes.add(copiar);
        botoes.add(paguei);

        JPanel sul = new JPanel(new BorderLayout(0, 10));
        sul.add(rodapeCodigo, BorderLayout.CENTER);
        sul.add(botoes, BorderLayout.SOUTH);

        JPanel raiz = new JPanel(new BorderLayout(10, 10));
        raiz.setBorder(BorderFactory.createEmptyBorder(20, 25, 20, 25));
        raiz.add(titulo, BorderLayout.NORTH);
        raiz.add(painelQr, BorderLayout.CENTER);
        raiz.add(sul, BorderLayout.SOUTH);

        abrir(dialogo, raiz, paguei);

        return resultado[0];
    }

    // ---------------------------------------------------------------
    // CARTÕES (débito, crédito, vale-refeição): maquininha de mentirinha.
    // Não pede número de cartão. Crédito permite escolher as parcelas.
    // Retorna o texto da transação ou null se cancelar.
    // ---------------------------------------------------------------
    public static String pagarCartao(String metodo, String nomeCliente, double valor) {

        boolean credito = metodo.toLowerCase().contains("crédito");

        JLabel titulo = new JLabel(metodo + ": " + dinheiro(valor),
                SwingConstants.CENTER);
        titulo.setFont(new Font("SansSerif", Font.BOLD, 16));

        JLabel maquininha = new JLabel(
                "<html><div style='text-align:center'>Insira, aproxime ou passe o cartão<br>"
                + "na maquininha (simulação)</div></html>",
                SwingConstants.CENTER);
        maquininha.setOpaque(true);
        maquininha.setBackground(new Color(40, 60, 40));
        maquininha.setForeground(new Color(170, 255, 170));
        maquininha.setFont(new Font("Monospaced", Font.BOLD, 14));
        maquininha.setBorder(BorderFactory.createEmptyBorder(18, 18, 18, 18));

        JComboBox<String> comboParcelas = new JComboBox<>();
        for (int p = 1; p <= 12; p++) {
            comboParcelas.addItem(p + "x de " + dinheiro(valor / p) + " sem juros");
        }

        JPanel centro = new JPanel(new GridLayout(0, 1, 8, 8));
        centro.add(maquininha);
        if (credito) {
            JPanel linhaParcelas = new JPanel(new BorderLayout(8, 0));
            linhaParcelas.add(new JLabel("Parcelas:"), BorderLayout.WEST);
            linhaParcelas.add(comboParcelas, BorderLayout.CENTER);
            centro.add(linhaParcelas);
        }

        JButton aprovar = new JButton("Simular aprovação");
        JButton cancelar = new JButton("Cancelar");

        String[] resultado = {null};

        JDialog dialogo = new JDialog((Frame) null,
                "Pagamento - " + metodo + " - " + nomeCliente, true);

        aprovar.addActionListener(e -> {
            int autorizacao = 100000 + new Random().nextInt(900000);
            if (credito) {
                resultado[0] = metodo + " em " + (comboParcelas.getSelectedIndex() + 1)
                        + "x sem juros, aut. " + autorizacao;
            } else {
                resultado[0] = metodo + ", aut. " + autorizacao;
            }
            dialogo.dispose();
        });
        cancelar.addActionListener(e -> dialogo.dispose());

        JPanel botoes = new JPanel(new GridLayout(1, 2, 10, 0));
        botoes.add(cancelar);
        botoes.add(aprovar);

        JPanel raiz = new JPanel(new BorderLayout(10, 15));
        raiz.setBorder(BorderFactory.createEmptyBorder(20, 25, 20, 25));
        raiz.add(titulo, BorderLayout.NORTH);
        raiz.add(centro, BorderLayout.CENTER);
        raiz.add(botoes, BorderLayout.SOUTH);

        abrir(dialogo, raiz, aprovar);

        return resultado[0];
    }

    // Desenha um "QR Code" falso (só um desenho, não lê nada).
    private static class QrFalso extends JPanel {

        private static final int MODULOS = 25;
        private static final int TAM = 8;
        private final boolean[][] celulas = new boolean[MODULOS][MODULOS];

        QrFalso(long semente) {
            Random sorteio = new Random(semente);

            for (int y = 0; y < MODULOS; y++) {
                for (int x = 0; x < MODULOS; x++) {
                    celulas[y][x] = sorteio.nextBoolean();
                }
            }

            marcarCanto(0, 0);
            marcarCanto(MODULOS - 7, 0);
            marcarCanto(0, MODULOS - 7);

            setPreferredSize(new Dimension(MODULOS * TAM + 20, MODULOS * TAM + 20));
            setBackground(Color.WHITE);
            setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY));
        }

        // Quadrados de posição nos cantos, como num QR Code de verdade
        private void marcarCanto(int x0, int y0) {
            for (int y = -1; y <= 7; y++) {
                for (int x = -1; x <= 7; x++) {
                    int cx = x0 + x;
                    int cy = y0 + y;
                    if (cx < 0 || cy < 0 || cx >= MODULOS || cy >= MODULOS) {
                        continue;
                    }
                    boolean dentro = x >= 0 && x <= 6 && y >= 0 && y <= 6;
                    boolean anel = x == 0 || x == 6 || y == 0 || y == 6;
                    boolean miolo = x >= 2 && x <= 4 && y >= 2 && y <= 4;
                    celulas[cy][cx] = dentro && (anel || miolo);
                }
            }
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            g.setColor(Color.BLACK);
            for (int y = 0; y < MODULOS; y++) {
                for (int x = 0; x < MODULOS; x++) {
                    if (celulas[y][x]) {
                        g.fillRect(10 + x * TAM, 10 + y * TAM, TAM, TAM);
                    }
                }
            }
        }
    }
}