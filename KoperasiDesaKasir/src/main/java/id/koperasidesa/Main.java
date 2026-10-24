package id.koperasidesa;

import com.formdev.flatlaf.FlatDarkLaf;
import id.koperasidesa.ui.FormLogin;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.JOptionPane;

public final class Main {
    private Main() {}
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            try {
                FlatDarkLaf.setup();
                UIManager.put("Component.arc", 14);
                UIManager.put("Button.arc", 14);
                UIManager.put("TextComponent.arc", 10);
                new FormLogin().setVisible(true);
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(null, "Aplikasi gagal dibuka: " + ex.getMessage(),
                        "Kesalahan", JOptionPane.ERROR_MESSAGE);
            }
        });
    }
}
