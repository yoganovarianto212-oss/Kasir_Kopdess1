package id.koperasidesa.ui;

import id.koperasidesa.dao.ProductDAO;
import id.koperasidesa.model.Product;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.math.BigDecimal;
import java.text.NumberFormat;
import java.util.Locale;

public class FormBarang extends JFrame {
    private final ProductDAO dao=new ProductDAO();
    private final NumberFormat money=NumberFormat.getCurrencyInstance(new Locale("id","ID"));
    private final DefaultTableModel model=new DefaultTableModel(new Object[]{"ID","Kode","Nama","Kategori","Harga","Stok"},0){
        public boolean isCellEditable(int r,int c){return false;}
    };
    private final JTable table=new JTable(model);
    private final JTextField search=new JTextField(16),code=new JTextField(),name=new JTextField(),category=new JTextField(),price=new JTextField(),stock=new JTextField();
    private long selectedId=0;
    public FormBarang(){
        setTitle("Data Barang");setSize(1050,650);setLocationRelativeTo(null);
        JPanel root=base();root.setLayout(new BorderLayout(12,12));
        JPanel top=card("Pencarian Barang");JButton find=btn("Cari",true),reset=btn("Reset",false);
        JPanel sr=new JPanel(new FlowLayout(FlowLayout.LEFT));sr.setOpaque(false);sr.add(search);sr.add(find);sr.add(reset);top.add(sr,BorderLayout.CENTER);root.add(top,BorderLayout.NORTH);
        styleTable(table);JPanel list=card("Katalog Barang");list.add(new JScrollPane(table),BorderLayout.CENTER);root.add(list,BorderLayout.CENTER);
        JPanel right=card("Detail Barang");JPanel fields=new JPanel(new GridLayout(5,2,8,8));fields.setOpaque(false);
        addField(fields,"Kode",code);addField(fields,"Nama",name);addField(fields,"Kategori",category);addField(fields,"Harga",price);addField(fields,"Stok",stock);
        JPanel actions=new JPanel(new GridLayout(2,2,7,7));actions.setOpaque(false);
        JButton save=btn("Simpan / Ubah",true),clear=btn("Form Baru",false),del=btn("Hapus",false),reload=btn("Muat Ulang",false);
        actions.add(save);actions.add(clear);actions.add(del);actions.add(reload);
        JPanel south=new JPanel(new BorderLayout(0,12));south.setOpaque(false);south.add(fields);south.add(actions,BorderLayout.SOUTH);right.add(south,BorderLayout.CENTER);right.setPreferredSize(new Dimension(300,400));root.add(right,BorderLayout.EAST);
        setContentPane(root);find.addActionListener(e->load());search.addActionListener(e->load());reset.addActionListener(e->{search.setText("");load();});
        table.getSelectionModel().addListSelectionListener(e->{if(!e.getValueIsAdjusting())select();});
        save.addActionListener(e->save());clear.addActionListener(e->clear());del.addActionListener(e->delete());reload.addActionListener(e->load());load();
    }
    private JPanel base(){JPanel p=new JPanel();p.setBackground(Theme.BG);p.setBorder(BorderFactory.createEmptyBorder(16,18,18,18));return p;}
    private JPanel card(String title){JPanel p=new JPanel(new BorderLayout(8,12));p.setBackground(Theme.PANEL);p.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createLineBorder(new Color(75,64,42),1,true),BorderFactory.createEmptyBorder(14,14,14,14)));JLabel l=new JLabel(title);l.setForeground(Theme.GOLD_LIGHT);l.setFont(new Font("SansSerif",Font.BOLD,15));p.add(l,BorderLayout.NORTH);return p;}
    private JButton btn(String s,boolean primary){JButton b=new JButton(s);Theme.styleButton(b,primary);return b;}
    private void addField(JPanel p,String s,JTextField f){JLabel l=new JLabel(s);l.setForeground(Theme.MUTED);p.add(l);p.add(f);}
    private void styleTable(JTable t){t.setRowHeight(29);t.setBackground(new Color(31,32,37));t.setForeground(Theme.TEXT);t.setGridColor(new Color(58,58,62));t.setSelectionBackground(new Color(92,73,36));t.getTableHeader().setBackground(new Color(43,41,35));t.getTableHeader().setForeground(Theme.GOLD_LIGHT);}
    private void load(){try{model.setRowCount(0);for(Product p:dao.search(search.getText()))model.addRow(new Object[]{p.getId(),p.getCode(),p.getName(),p.getCategory(),money.format(p.getPrice()),p.getStock()});}catch(Exception e){error(e);}}
    private void select(){int r=table.getSelectedRow();if(r<0)return;selectedId=Long.parseLong(model.getValueAt(r,0).toString());code.setText(""+model.getValueAt(r,1));name.setText(""+model.getValueAt(r,2));category.setText(""+model.getValueAt(r,3));price.setText(model.getValueAt(r,4).toString().replaceAll("[^0-9,.-]","").replace(".","").replace(",", "."));stock.setText(""+model.getValueAt(r,5));}
    private void save(){try{if(code.getText().isBlank()||name.getText().isBlank())throw new IllegalArgumentException("Kode dan nama wajib diisi.");BigDecimal v=new BigDecimal(price.getText().trim().replace(".","").replace(",", "."));int s=Integer.parseInt(stock.getText().trim());if(v.signum()<0||s<0)throw new IllegalArgumentException("Harga/stok tidak boleh negatif.");dao.save(new Product(selectedId,code.getText().trim(),name.getText().trim(),category.getText().isBlank()?"Umum":category.getText().trim(),v,s,true));clear();load();}catch(Exception e){error(e);}}
    private void delete(){if(selectedId==0){error(new Exception("Pilih barang dahulu."));return;}if(JOptionPane.showConfirmDialog(this,"Hapus barang dari katalog? Riwayat tetap aman.","Konfirmasi",JOptionPane.YES_NO_OPTION)==JOptionPane.YES_OPTION)try{dao.softDelete(selectedId);clear();load();}catch(Exception e){error(e);}}
    private void clear(){selectedId=0;table.clearSelection();code.setText("");name.setText("");category.setText("");price.setText("");stock.setText("");}
    private void error(Exception e){JOptionPane.showMessageDialog(this,e.getMessage(),"Perhatian",JOptionPane.WARNING_MESSAGE);}
}
