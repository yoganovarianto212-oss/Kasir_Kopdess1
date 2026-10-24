package id.koperasidesa.ui;

import id.koperasidesa.dao.*;
import id.koperasidesa.model.*;
import id.koperasidesa.service.CheckoutService;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.math.BigDecimal;
import java.text.NumberFormat;
import java.util.*;
import java.util.List;
import java.util.Locale;

public class FormKasir extends JFrame {
    private final ProductDAO products=new ProductDAO();private final SaleDAO sales=new SaleDAO();private final CheckoutService service=new CheckoutService(sales);
    private final List<CartItem> cart=new ArrayList<>();private final NumberFormat money=NumberFormat.getCurrencyInstance(new Locale("id","ID"));
    private final DefaultTableModel catalogModel=model("ID","Kode","Nama","Harga","Stok"),cartModel=model("Kode","Nama","Harga","Jumlah","Subtotal");
    private final JTable catalog=new JTable(catalogModel),cartTable=new JTable(cartModel);
    private final JTextField search=new JTextField(14),qty=new JTextField("1",4),buyer=new JTextField(20);
    private final JLabel total=new JLabel("TOTAL Rp0");
    public FormKasir(){setTitle("Kasir & Keranjang");setSize(1100,680);setLocationRelativeTo(null);
        JPanel root=new JPanel(new GridLayout(1,2,14,14));root.setBackground(Theme.BG);root.setBorder(BorderFactory.createEmptyBorder(18,18,18,18));
        JPanel left=card("Pilih Barang");JPanel sr=new JPanel(new FlowLayout(FlowLayout.LEFT));sr.setOpaque(false);JButton find=btn("Cari",true);sr.add(search);sr.add(find);left.add(sr,BorderLayout.NORTH);style(catalog);left.add(new JScrollPane(catalog),BorderLayout.CENTER);
        JPanel ar=new JPanel(new FlowLayout(FlowLayout.LEFT));ar.setOpaque(false);JButton add=btn("Tambah ke Keranjang",true);ar.add(new JLabel("Jumlah"));ar.add(qty);ar.add(add);left.add(ar,BorderLayout.SOUTH);
        JPanel right=card("Keranjang Belanja");style(cartTable);right.add(new JScrollPane(cartTable),BorderLayout.CENTER);
        JPanel bottom=new JPanel();bottom.setOpaque(false);bottom.setLayout(new BoxLayout(bottom,BoxLayout.Y_AXIS));JLabel bl=new JLabel("Nama Pembeli");bl.setForeground(Theme.MUTED);bottom.add(bl);bottom.add(buyer);total.setForeground(Theme.GOLD_LIGHT);total.setFont(new Font("SansSerif",Font.BOLD,20));bottom.add(Box.createVerticalStrut(10));bottom.add(total);
        JPanel actions=new JPanel(new FlowLayout(FlowLayout.RIGHT));actions.setOpaque(false);JButton remove=btn("Hapus Item",false),checkout=btn("Checkout",true);actions.add(remove);actions.add(checkout);bottom.add(actions);right.add(bottom,BorderLayout.SOUTH);
        root.add(left);root.add(right);setContentPane(root);find.addActionListener(e->load());search.addActionListener(e->load());add.addActionListener(e->addItem());remove.addActionListener(e->removeItem());checkout.addActionListener(e->checkout());load();
    }
    private DefaultTableModel model(String...c){return new DefaultTableModel(c,0){public boolean isCellEditable(int r,int c){return false;}};}
    private JPanel card(String title){JPanel p=new JPanel(new BorderLayout(8,12));p.setBackground(Theme.PANEL);p.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createLineBorder(new Color(75,64,42),1,true),BorderFactory.createEmptyBorder(14,14,14,14)));JLabel l=new JLabel(title);l.setForeground(Theme.GOLD_LIGHT);l.setFont(new Font("SansSerif",Font.BOLD,15));p.add(l,BorderLayout.NORTH);return p;}
    private JButton btn(String s,boolean primary){JButton b=new JButton(s);Theme.styleButton(b,primary);return b;}
    private void style(JTable t){t.setRowHeight(29);t.setBackground(new Color(31,32,37));t.setForeground(Theme.TEXT);t.setGridColor(new Color(58,58,62));t.setSelectionBackground(new Color(92,73,36));t.getTableHeader().setBackground(new Color(43,41,35));t.getTableHeader().setForeground(Theme.GOLD_LIGHT);}
    private void load(){try{catalogModel.setRowCount(0);for(Product p:products.search(search.getText()))catalogModel.addRow(new Object[]{p.getId(),p.getCode(),p.getName(),money.format(p.getPrice()),p.getStock()});}catch(Exception e){error(e);}}
    private void addItem(){int r=catalog.getSelectedRow();if(r<0){error(new Exception("Pilih barang terlebih dahulu."));return;}try{long id=Long.parseLong(catalogModel.getValueAt(r,0).toString());int n=Integer.parseInt(qty.getText().trim());if(n<1)throw new Exception("Jumlah minimal 1.");Product p=null;for(Product x:products.search(catalogModel.getValueAt(r,1).toString()))if(x.getId()==id)p=x;if(p==null)throw new Exception("Barang tidak tersedia.");CartItem old=cart.stream().filter(x->x.getProductId()==id).findFirst().orElse(null);int newQty=n+(old==null?0:old.getQuantity());if(newQty>p.getStock())throw new Exception("Jumlah melebihi stok.");if(old==null)cart.add(new CartItem(p,n));else old.setQuantity(newQty);refreshCart();}catch(Exception e){error(e);}}
    private void refreshCart(){cartModel.setRowCount(0);BigDecimal sum=BigDecimal.ZERO;for(CartItem i:cart){cartModel.addRow(new Object[]{i.getCodeSnapshot(),i.getNameSnapshot(),money.format(i.getUnitPriceSnapshot()),i.getQuantity(),money.format(i.getSubtotal())});sum=sum.add(i.getSubtotal());}total.setText("TOTAL "+money.format(sum));}
    private void removeItem(){int r=cartTable.getSelectedRow();if(r<0){error(new Exception("Pilih item keranjang."));return;}cart.remove(r);refreshCart();}
    private void checkout(){try{long id=service.checkout(buyer.getText(),cart);JOptionPane.showMessageDialog(this,"Transaksi berhasil disimpan. ID: "+id);cart.clear();buyer.setText("");refreshCart();load();}catch(Exception e){error(e);}}
    private void error(Exception e){JOptionPane.showMessageDialog(this,e.getMessage(),"Perhatian",JOptionPane.WARNING_MESSAGE);}
}
