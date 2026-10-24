package id.koperasidesa.ui;

import id.koperasidesa.dao.SaleDAO;
import id.koperasidesa.model.Sale;
import id.koperasidesa.model.SaleItem;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.text.NumberFormat;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

public class FormRiwayat extends JFrame {
    private final SaleDAO dao=new SaleDAO();private final NumberFormat money=NumberFormat.getCurrencyInstance(new Locale("id","ID"));
    private final DefaultTableModel model=new DefaultTableModel(new Object[]{"ID","Nomor Transaksi","Nama Pembeli","Total","Waktu"},0){public boolean isCellEditable(int r,int c){return false;}};
    private final JTable table=new JTable(model);private final JTextField search=new JTextField(20);
    public FormRiwayat(){setTitle("Riwayat Transaksi");setSize(950,600);setLocationRelativeTo(null);JPanel root=new JPanel(new BorderLayout(12,12));root.setBackground(Theme.BG);root.setBorder(BorderFactory.createEmptyBorder(18,18,18,18));
        JPanel top=card("Pencarian Riwayat");JPanel sr=new JPanel(new FlowLayout(FlowLayout.LEFT));sr.setOpaque(false);JButton find=btn("Cari",true),reset=btn("Reset",false);sr.add(search);sr.add(find);sr.add(reset);top.add(sr,BorderLayout.CENTER);root.add(top,BorderLayout.NORTH);
        style(table);JPanel list=card("Transaksi");list.add(new JScrollPane(table),BorderLayout.CENTER);root.add(list,BorderLayout.CENTER);JButton detail=btn("Lihat Detail Snapshot",true);JPanel south=new JPanel(new FlowLayout(FlowLayout.RIGHT));south.setOpaque(false);south.add(detail);root.add(south,BorderLayout.SOUTH);setContentPane(root);
        find.addActionListener(e->load());search.addActionListener(e->load());reset.addActionListener(e->{search.setText("");load();});detail.addActionListener(e->detail());load();}
    private JPanel card(String title){JPanel p=new JPanel(new BorderLayout(8,12));p.setBackground(Theme.PANEL);p.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createLineBorder(new Color(75,64,42),1,true),BorderFactory.createEmptyBorder(14,14,14,14)));JLabel l=new JLabel(title);l.setForeground(Theme.GOLD_LIGHT);l.setFont(new Font("SansSerif",Font.BOLD,15));p.add(l,BorderLayout.NORTH);return p;}
    private JButton btn(String s,boolean primary){JButton b=new JButton(s);Theme.styleButton(b,primary);return b;}
    private void style(JTable t){t.setRowHeight(29);t.setBackground(new Color(31,32,37));t.setForeground(Theme.TEXT);t.setGridColor(new Color(58,58,62));t.setSelectionBackground(new Color(92,73,36));t.getTableHeader().setBackground(new Color(43,41,35));t.getTableHeader().setForeground(Theme.GOLD_LIGHT);}
    private void load(){try{model.setRowCount(0);for(Sale s:dao.search(search.getText()))model.addRow(new Object[]{s.getId(),s.getInvoiceNo(),s.getBuyerName(),money.format(s.getTotal()),s.getCreatedAt().format(DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm"))});}catch(Exception e){error(e);}}
    private void detail(){int r=table.getSelectedRow();if(r<0){error(new Exception("Pilih transaksi dahulu."));return;}try{long id=Long.parseLong(model.getValueAt(r,0).toString());StringBuilder b=new StringBuilder("DETAIL ITEM — SNAPSHOT\\n\\n");for(SaleItem i:dao.getItems(id))b.append(i.getCodeSnapshot()).append(" • ").append(i.getNameSnapshot()).append("\\n").append(i.getQuantity()).append(" x ").append(money.format(i.getUnitPriceSnapshot())).append(" = ").append(money.format(i.getSubtotal())).append("\\n\\n");JOptionPane.showMessageDialog(this,b.toString(),"Detail Transaksi",JOptionPane.INFORMATION_MESSAGE);}catch(Exception e){error(e);}}
    private void error(Exception e){JOptionPane.showMessageDialog(this,e.getMessage(),"Perhatian",JOptionPane.WARNING_MESSAGE);}
}
