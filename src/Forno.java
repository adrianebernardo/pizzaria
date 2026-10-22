package pizzaria;

import javax.swing.JOptionPane;

public class Forno {
    public void prepararPedido(String nomeCliente) {
        JOptionPane.showMessageDialog(null,
            "No forno, " + nomeCliente + "!\nSua pizza está sendo preparada com carinho. 🍕",
            "Pizzaria Bernardes XD",
            JOptionPane.INFORMATION_MESSAGE);
    }
}