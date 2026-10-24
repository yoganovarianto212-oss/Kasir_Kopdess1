package id.koperasidesa.ui;

import javax.swing.*;
import java.awt.*;

public class FormDashboard extends JFrame {
    public FormDashboard() {
        setTitle("Koperasi Desa • Dashboard"); setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(850,560);setLocationRelativeTo(null);getContentPane().setBackground(Theme.BG);
        JPanel root=new JPanel(new BorderLayout(20,20));root.setBackground(Theme.BG);root.setBorder(BorderFactory.createEmptyBorder(30,35,30,35));
        JPanel heading=new JPanel(new GridLayout(2,1,0,6));heading.setOpaque(false);
        JLabel title=new JLabel("DASHBOARD KOPERASI DESA");title.setForeground(Theme.GOLD_LIGHT);title.setFont(new Font("SansSerif",Font.BOLD,28));
        JLabel sub=new JLabel("Pilih modul yang ingin dibuka");sub.setForeground(Theme.MUTED);heading.add(title);heading.add(sub);root.add(heading,BorderLayout.NORTH);
        JPanel cards=new JPanel(new GridLayout(2,2,18,18));cards.setOpaque(false);
        cards.add(module("01  •  DATA BARANG","Kelola stok, harga, kategori, dan pencarian",()->new FormBarang().setVisible(true)));
        cards.add(module("02  •  KASIR","Keranjang belanja dan checkout transaksi",()->new FormKasir().setVisible(true)));
        cards.add(module("03  •  RIWAYAT","Cari transaksi dan lihat detail snapshot",()->new FormRiwayat().setVisible(true)));
        cards.add(module("04  •  KELUAR","Tutup aplikasi kasir",()->System.exit(0)));
        root.add(cards,BorderLayout.CENTER);setContentPane(root);
    }
    private JPanel module(String title,String desc,Runnable action){
        JPanel p=new JPanel();p.setBackground(Theme.PANEL);p.setLayout(new BoxLayout(p,BoxLayout.Y_AXIS));
        p.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createLineBorder(new Color(100,78,39),1,true),BorderFactory.createEmptyBorder(20,22,20,22)));
        JLabel t=new JLabel(title);t.setForeground(Theme.GOLD_LIGHT);t.setFont(new Font("SansSerif",Font.BOLD,17));
        JLabel d=new JLabel("<html><div style='width:260px;color:#aaa69b'>"+desc+"</div></html>");
        JButton b=new JButton("BUKA MODUL  →");Theme.styleButton(b,true);b.setAlignmentX(Component.LEFT_ALIGNMENT);b.addActionListener(e->action.run());
        p.add(t);p.add(Box.createVerticalStrut(10));p.add(d);p.add(Box.createVerticalGlue());p.add(b);return p;
    }
}
