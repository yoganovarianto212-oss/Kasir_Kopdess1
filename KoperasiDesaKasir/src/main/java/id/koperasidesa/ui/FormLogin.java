package id.koperasidesa.ui;

import javax.swing.*;
import java.awt.*;

public class FormLogin extends JFrame {
    public FormLogin() {
        setTitle("Koperasi Desa • Selamat Datang");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(600, 400); setLocationRelativeTo(null);
        getContentPane().setBackground(Theme.BG);
        JPanel outer = new JPanel(new GridBagLayout()); outer.setBackground(Theme.BG);
        JPanel card = new JPanel(); card.setBackground(Theme.PANEL);
        card.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(110,85,39),1,true),
            BorderFactory.createEmptyBorder(35,40,35,40)));
        card.setLayout(new BoxLayout(card,BoxLayout.Y_AXIS));
        JLabel title=new JLabel("KOPERASI DESA"); title.setForeground(Theme.GOLD_LIGHT);
        title.setFont(new Font("SansSerif",Font.BOLD,30)); title.setAlignmentX(Component.CENTER_ALIGNMENT);
        JLabel sub=new JLabel("GOLDLINE • SISTEM KASIR"); sub.setForeground(Theme.MUTED);
        sub.setAlignmentX(Component.CENTER_ALIGNMENT);
        JButton enter=new JButton("MASUK KE APLIKASI"); Theme.styleButton(enter,true); enter.setAlignmentX(Component.CENTER_ALIGNMENT);
        enter.addActionListener(e->{new FormDashboard().setVisible(true);dispose();});
        card.add(title);card.add(Box.createVerticalStrut(10));card.add(sub);card.add(Box.createVerticalStrut(35));card.add(enter);
        outer.add(card);setContentPane(outer);
    }
    public static void main(String[] args) { SwingUtilities.invokeLater(()->new FormLogin().setVisible(true)); }
}
