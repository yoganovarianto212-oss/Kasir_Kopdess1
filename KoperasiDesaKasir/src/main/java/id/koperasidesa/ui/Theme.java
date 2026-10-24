package id.koperasidesa.ui;

import java.awt.*;

public final class Theme {
    public static final Color BG=new Color(15,16,19), PANEL=new Color(27,28,33), GOLD=new Color(218,174,75),
            GOLD_LIGHT=new Color(245,211,135), TEXT=new Color(242,238,226), MUTED=new Color(166,164,155),
            RED=new Color(188,72,67);
    private Theme(){}
    public static void styleButton(javax.swing.JButton b, boolean primary){
        b.setFocusPainted(false);b.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        b.setBackground(primary?GOLD:PANEL);b.setForeground(primary?BG:TEXT);
        b.setFont(new Font("SansSerif",Font.BOLD,13));b.setBorder(javax.swing.BorderFactory.createEmptyBorder(10,16,10,16));
    }
}
