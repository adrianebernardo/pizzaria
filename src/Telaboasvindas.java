package pizzaria;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.net.URL;

public class Telaboasvindas {

    // Aceita a logo como .jpg, .jpeg ou .png
    private static final String[] NOMES_LOGO = {
        "pizzariabernardes.jpg", "pizzariabernardes.jpeg", "pizzariabernardes.png"
    };

    // Resultado da tela de nome + modalidade
    public static class Dados {
        public final String nome;
        public final boolean paraLevar;

        Dados(String nome, boolean paraLevar) {
            this.nome = nome;
            this.paraLevar = paraLevar;
        }

        public String textoModalidade() {
            return paraLevar ? "Para levar" : "Comer aqui";
        }
    }

    // Procura a logo na pasta do projeto / classpath. Retorna null se não achar.
    static ImageIcon carregarLogo(int larguraMaxima) {
        for (String nome : NOMES_LOGO) {
            ImageIcon icone = carregarLogo(larguraMaxima, nome);
            if (icone != null) {
                return icone;
            }
        }
        return null;
    }

    private static ImageIcon carregarLogo(int larguraMaxima, String arquivoLogo) {
        BufferedImage imagem = null;

        try {
            URL url = Telaboasvindas.class.getResource("/" + arquivoLogo);
            if (url == null) {
                url = Telaboasvindas.class.getResource(arquivoLogo);
            }
            if (url != null) {
                imagem = ImageIO.read(url);
            }
        } catch (Exception erro) {
            imagem = null;
        }

        if (imagem == null) {
            String[] caminhos = {
                arquivoLogo,
                "src/" + arquivoLogo,
                "src/pizzaria/" + arquivoLogo,
                "imagens/" + arquivoLogo,
                "images/" + arquivoLogo
            };

            for (String caminho : caminhos) {
                File arquivo = new File(caminho);
                if (arquivo.exists()) {
                    try {
                        imagem = ImageIO.read(arquivo);
                    } catch (Exception erro) {
                        imagem = null;
                    }
                    if (imagem != null) {
                        break;
                    }
                }
            }
        }

        if (imagem == null) {
            return null;
        }

        if (imagem.getWidth() > larguraMaxima) {
            Image reduzida = imagem.getScaledInstance(
                    larguraMaxima, -1, Image.SCALE_SMOOTH);
            return new ImageIcon(reduzida);
        }

        return new ImageIcon(imagem);
    }

    // 1ª caixa: "Bem-vindo à Pizzaria Bernardes" + logo embaixo + botão Seguinte.
    // Retorna false se o cliente fechar a janela.
    public static boolean mostrarBoasVindas() {

        JLabel titulo = new JLabel("Bem-vindo à Pizzaria Bernardes!", SwingConstants.CENTER);
        titulo.setFont(new Font("SansSerif", Font.BOLD, 22));

        JLabel logo = new JLabel("", SwingConstants.CENTER);
        ImageIcon icone = carregarLogo(360);
        if (icone != null) {
            logo.setIcon(icone);
        }

        JButton seguinte = new JButton("Seguinte");

        boolean[] confirmou = {false};

        JDialog dialogo = new JDialog((Frame) null, "Pizzaria Bernardes XD", true);

        seguinte.addActionListener(e -> {
            confirmou[0] = true;
            dialogo.dispose();
        });

        JPanel raiz = new JPanel(new BorderLayout(10, 15));
        raiz.setBorder(BorderFactory.createEmptyBorder(20, 30, 20, 30));
        raiz.setBackground(Color.WHITE); // combina com o fundo branco da logo
        raiz.add(titulo, BorderLayout.NORTH);
        raiz.add(logo, BorderLayout.CENTER);
        raiz.add(seguinte, BorderLayout.SOUTH);

        dialogo.setContentPane(raiz);
        dialogo.getRootPane().setDefaultButton(seguinte); // Enter aciona "Seguinte"
        dialogo.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        dialogo.pack();
        dialogo.setLocationRelativeTo(null);
        dialogo.setVisible(true);

        return confirmou[0];
    }

    // 2ª caixa: pede o nome e, dentro da mesma caixa, outra com a pergunta
    // "para levar ou comer aqui?" (duas opções). Retorna null se fechar a janela.
    public static Dados pedirDados() {

        JTextField campoNome = new JTextField(20);

        JPanel painelNome = new JPanel(new GridLayout(0, 1, 5, 5));
        painelNome.add(new JLabel("Qual é o seu nome?"));
        painelNome.add(campoNome);

        JRadioButton comerAqui = new JRadioButton("Comer aqui", true);
        JRadioButton paraLevar = new JRadioButton("Para levar");

        ButtonGroup grupo = new ButtonGroup();
        grupo.add(comerAqui);
        grupo.add(paraLevar);

        JPanel painelOpcoes = new JPanel(new GridLayout(0, 1, 5, 5));
        painelOpcoes.setBorder(BorderFactory.createTitledBorder(
                "Vai ser para levar ou comer aqui?"));
        painelOpcoes.add(comerAqui);
        painelOpcoes.add(paraLevar);

        JButton seguinte = new JButton("Seguinte");

        Dados[] resultado = {null};

        JDialog dialogo = new JDialog((Frame) null, "Seu pedido", true);

        seguinte.addActionListener(e -> {
            String nome = campoNome.getText().trim();
            if (nome.isEmpty()) {
                nome = "Cliente";
            }
            resultado[0] = new Dados(nome, paraLevar.isSelected());
            dialogo.dispose();
        });

        JPanel raiz = new JPanel(new BorderLayout(10, 15));
        raiz.setBorder(BorderFactory.createEmptyBorder(20, 25, 20, 25));
        raiz.add(painelNome, BorderLayout.NORTH);
        raiz.add(painelOpcoes, BorderLayout.CENTER);
        raiz.add(seguinte, BorderLayout.SOUTH);

        dialogo.setContentPane(raiz);
        dialogo.getRootPane().setDefaultButton(seguinte); // Enter aciona "Seguinte"
        dialogo.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        dialogo.pack();
        dialogo.setLocationRelativeTo(null);
        dialogo.setVisible(true);

        return resultado[0];
    }
}