package id.koperasidesa.ui;

import id.koperasidesa.dao.*;
import id.koperasidesa.model.*;
import id.koperasidesa.service.CheckoutService;
import javax.swing.*;
import javax.swing.border.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.math.BigDecimal;
import java.sql.SQLException;
import java.text.NumberFormat;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.List;
import java.util.Locale;

public class MainFrame extends JFrame {
    private final ProductDAO productDAO=new ProductDAO();
    private final SaleDAO saleDAO=new SaleDAO();
    private final CheckoutService checkoutService=new CheckoutService(saleDAO);
    private final List<CartItem> cart=new ArrayList<>();
    private final NumberFormat money=NumberFormat.getCurrencyInstance(new Locale("id","ID"));
    private JTable productTable, cartTable, historyTable;
    private DefaultTableModel productModel,cartModel,historyModel;
    private JTextField productSearch,codeField,nameField,categoryField,priceField,stockField,qtyField,buyerField,historySearch;
    private JLabel totalLabel, statusLabel;
    private long selectedProductId=0;

    public MainFrame(){
        super("Koperasi Desa • Kasir");
        setDefaultCloseOperation(EXIT_ON_CLOSE);setMinimumSize(new Dimension(1120,720));setSize(1280,800);
        getContentPane().setBackground(Theme.BG);setLocationRelativeTo(null);
        setLayout(new BorderLayout(0,0));add(header(),BorderLayout.NORTH);
        JTabbedPane tabs=new JTabbedPane();tabs.setFont(new Font("SansSerif",Font.BOLD,14));
        tabs.addTab("  Katalog Barang  ",productPanel());tabs.addTab("  Kasir & Keranjang  ",cashierPanel());tabs.addTab("  Riwayat Transaksi  ",historyPanel());
        add(tabs,BorderLayout.CENTER);statusLabel=new JLabel("  Siap digunakan • Database koperasi_desa");
        statusLabel.setForeground(Theme.MUTED);statusLabel.setBorder(new EmptyBorder(8,12,8,12));add(statusLabel,BorderLayout.SOUTH);
        refreshProducts();refreshHistory();
    }
    private JPanel header(){
        JPanel p=new JPanel(new BorderLayout());p.setBackground(Theme.BG);p.setBorder(new EmptyBorder(20,26,18,26));
        JPanel brand=new JPanel(new GridLayout(2,1,0,3));brand.setOpaque(false);
        JLabel title=new JLabel("KOPERASI DESA");title.setForeground(Theme.GOLD_LIGHT);title.setFont(new Font("SansSerif",Font.BOLD,25));
        JLabel sub=new JLabel("GOLDLINE  /  SISTEM KASIR & INVENTORI");sub.setForeground(Theme.MUTED);sub.setFont(new Font("SansSerif",Font.PLAIN,11));
        brand.add(title);brand.add(sub);p.add(brand,BorderLayout.WEST);
        JLabel mark=new JLabel("●  RETAIL MANAGEMENT");mark.setForeground(Theme.GOLD);mark.setFont(new Font("SansSerif",Font.BOLD,12));p.add(mark,BorderLayout.EAST);
        return p;
    }
    private JPanel basePanel(){JPanel p=new JPanel(new BorderLayout(14,14));p.setBackground(Theme.BG);p.setBorder(new EmptyBorder(12,20,16,20));return p;}
    private JPanel card(String title){
        JPanel p=new JPanel(new BorderLayout(10,12));p.setBackground(Theme.PANEL);
        p.setBorder(new CompoundBorder(new LineBorder(new Color(75,64,42),1,true),new EmptyBorder(16,16,16,16)));
        JLabel h=new JLabel(title);h.setForeground(Theme.GOLD_LIGHT);h.setFont(new Font("SansSerif",Font.BOLD,15));p.add(h,BorderLayout.NORTH);return p;
    }
    private JTextField field(String hint){JTextField f=new JTextField();f.setToolTipText(hint);f.setPreferredSize(new Dimension(140,34));return f;}
    private JButton button(String text,boolean primary){JButton b=new JButton(text);Theme.styleButton(b,primary);return b;}
    private DefaultTableModel model(String... cols){return new DefaultTableModel(cols,0){@Override public boolean isCellEditable(int r,int c){return false;}};}
    private JTable table(DefaultTableModel m){
        JTable t=new JTable(m);t.setRowHeight(30);t.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        t.setBackground(new Color(31,32,37));t.setForeground(Theme.TEXT);t.setGridColor(new Color(58,58,62));
        t.setSelectionBackground(new Color(92,73,36));t.setSelectionForeground(Color.WHITE);t.getTableHeader().setBackground(new Color(43,41,35));
        t.getTableHeader().setForeground(Theme.GOLD_LIGHT);t.getTableHeader().setFont(new Font("SansSerif",Font.BOLD,12));return t;
    }
    private JPanel productPanel(){
        JPanel root=basePanel();JPanel top=card("Katalog & Pencarian");productSearch=field("Cari kode, nama, kategori");JButton find=button("Cari",true),reset=button("Reset",false);
        JPanel searchRow=new JPanel(new FlowLayout(FlowLayout.LEFT,8,0));searchRow.setOpaque(false);searchRow.add(productSearch);searchRow.add(find);searchRow.add(reset);
        top.add(searchRow,BorderLayout.CENTER);root.add(top,BorderLayout.NORTH);
        productModel=model("ID","Kode","Nama Barang","Kategori","Harga","Stok");productTable=table(productModel);
        JPanel list=card("Daftar Barang");list.add(new JScrollPane(productTable),BorderLayout.CENTER);root.add(list,BorderLayout.CENTER);
        JPanel form=card("Detail Barang");
        codeField=field("Kode barang");nameField=field("Nama barang");categoryField=field("Kategori");priceField=field("Harga");stockField=field("Stok");
        JPanel fields=new JPanel(new GridLayout(5,2,8,8));fields.setOpaque(false);
        addField(fields,"Kode",codeField);addField(fields,"Nama",nameField);addField(fields,"Kategori",categoryField);addField(fields,"Harga (Rp)",priceField);addField(fields,"Stok",stockField);
        JPanel actions=new JPanel(new GridLayout(2,2,8,8));actions.setOpaque(false);
        JButton save=button("Simpan / Ubah",true),clear=button("Form Baru",false),delete=button("Hapus Barang",false),refresh=button("Muat Ulang",false);
        actions.add(save);actions.add(clear);actions.add(delete);actions.add(refresh);
        JPanel south=new JPanel(new BorderLayout(0,12));south.setOpaque(false);south.add(fields,BorderLayout.CENTER);south.add(actions,BorderLayout.SOUTH);form.add(south,BorderLayout.CENTER);
        form.setPreferredSize(new Dimension(300,400));root.add(form,BorderLayout.EAST);
        find.addActionListener(e->refreshProducts());productSearch.addActionListener(e->refreshProducts());reset.addActionListener(e->{productSearch.setText("");refreshProducts();});
        productTable.getSelectionModel().addListSelectionListener(e->{if(!e.getValueIsAdjusting()&&productTable.getSelectedRow()>=0)selectProduct();});
        save.addActionListener(e->saveProduct());clear.addActionListener(e->clearProductForm());delete.addActionListener(e->deleteProduct());refresh.addActionListener(e->refreshProducts());
        return root;
    }
    private void addField(JPanel p,String label,JTextField f){JLabel l=new JLabel(label);l.setForeground(Theme.MUTED);p.add(l);p.add(f);}
    private void selectProduct(){
        int r=productTable.getSelectedRow();if(r<0)return;selectedProductId=Long.parseLong(productModel.getValueAt(r,0).toString());
        codeField.setText(productModel.getValueAt(r,1).toString());nameField.setText(productModel.getValueAt(r,2).toString());
        categoryField.setText(productModel.getValueAt(r,3).toString());priceField.setText(productModel.getValueAt(r,4).toString().replaceAll("[^0-9,.-]","").replace(",","."));
        stockField.setText(productModel.getValueAt(r,5).toString());
    }
    private void saveProduct(){
        try{
            String code=codeField.getText().trim(),name=nameField.getText().trim(),cat=categoryField.getText().trim();
            if(code.isEmpty()||name.isEmpty())throw new IllegalArgumentException("Kode dan nama barang wajib diisi.");
            BigDecimal price=new BigDecimal(priceField.getText().trim().replace(".","").replace(",","."));
            int stock=Integer.parseInt(stockField.getText().trim());
            if(price.signum()<0||stock<0)throw new IllegalArgumentException("Harga dan stok tidak boleh negatif.");
            Product p=new Product(selectedProductId,code,name,cat.isEmpty()?"Umum":cat,price,stock,true);productDAO.save(p);
            clearProductForm();refreshProducts();status("Data barang berhasil disimpan.");
        }catch(Exception ex){error(ex);}
    }
    private void deleteProduct(){
        if(selectedProductId==0){error("Pilih barang yang akan dihapus.");return;}
        if(JOptionPane.showConfirmDialog(this,"Sembunyikan barang ini dari katalog? Riwayat transaksi tetap aman.","Konfirmasi",JOptionPane.YES_NO_OPTION)!=JOptionPane.YES_OPTION)return;
        try{productDAO.softDelete(selectedProductId);clearProductForm();refreshProducts();status("Barang dihapus dari katalog.");}catch(Exception ex){error(ex);}
    }
    private void clearProductForm(){selectedProductId=0;productTable.clearSelection();codeField.setText("");nameField.setText("");categoryField.setText("");priceField.setText("");stockField.setText("");}
    private void refreshProducts(){
        if(productModel==null)return;try{productModel.setRowCount(0);for(Product p:productDAO.search(productSearch==null?"":productSearch.getText()))
            productModel.addRow(new Object[]{p.getId(),p.getCode(),p.getName(),p.getCategory(),money.format(p.getPrice()),p.getStock()});}
        catch(Exception ex){error(ex);}
    }
    private JPanel cashierPanel(){
        JPanel root=basePanel();JPanel left=card("Pilih Barang");JTextField search=field("Cari nama/kode");JButton find=button("Cari",true);
        JPanel searchRow=new JPanel(new FlowLayout(FlowLayout.LEFT));searchRow.setOpaque(false);searchRow.add(search);searchRow.add(find);left.add(searchRow,BorderLayout.NORTH);
        DefaultTableModel catalogModel=model("ID","Kode","Nama","Harga","Stok");JTable catalog=table(catalogModel);left.add(new JScrollPane(catalog),BorderLayout.CENTER);
        JPanel addRow=new JPanel(new FlowLayout(FlowLayout.LEFT));addRow.setOpaque(false);qtyField=field("Jumlah");qtyField.setText("1");JButton add=button("＋ Tambah ke Keranjang",true);addRow.add(new JLabel("Jumlah"));addRow.add(qtyField);addRow.add(add);left.add(addRow,BorderLayout.SOUTH);
        Runnable loadCatalog=()->{try{catalogModel.setRowCount(0);for(Product p:productDAO.search(search.getText()))catalogModel.addRow(new Object[]{p.getId(),p.getCode(),p.getName(),money.format(p.getPrice()),p.getStock()});}catch(Exception ex){error(ex);}};
        find.addActionListener(e->loadCatalog.run());search.addActionListener(e->loadCatalog.run());loadCatalog.run();
        add.addActionListener(e->{int r=catalog.getSelectedRow();if(r<0){error("Pilih barang pada daftar.");return;}try{
            long id=Long.parseLong(catalogModel.getValueAt(r,0).toString());int qty=Integer.parseInt(qtyField.getText().trim());
            if(qty<1)throw new IllegalArgumentException("Jumlah minimal 1.");
            Product p=null;for(Product x:productDAO.search(catalogModel.getValueAt(r,1).toString()))if(x.getId()==id){p=x;break;}
            if(p==null)throw new IllegalArgumentException("Barang tidak ditemukan.");
            CartItem existing=cart.stream().filter(x->x.getProductId()==id).findFirst().orElse(null);
            int newQty=qty+(existing==null?0:existing.getQuantity());if(newQty>p.getStock())throw new IllegalArgumentException("Jumlah melebihi stok.");
            if(existing==null)cart.add(new CartItem(p,qty));else existing.setQuantity(newQty);refreshCart();status("Barang masuk keranjang.");
        }catch(Exception ex){error(ex);}});
        JPanel right=card("Keranjang Belanja");cartModel=model("Kode","Nama Barang","Harga","Jumlah","Subtotal");cartTable=table(cartModel);right.add(new JScrollPane(cartTable),BorderLayout.CENTER);
        JPanel bottom=new JPanel(new GridLayout(0,1,8,8));bottom.setOpaque(false);buyerField=field("Nama lengkap pembeli");bottom.add(new JLabel("Nama Pembeli"));bottom.add(buyerField);
        totalLabel=new JLabel("TOTAL  Rp0");totalLabel.setForeground(Theme.GOLD_LIGHT);totalLabel.setFont(new Font("SansSerif",Font.BOLD,20));bottom.add(totalLabel);
        JPanel actions=new JPanel(new GridLayout(1,2,8,8));actions.setOpaque(false);JButton remove=button("Hapus Item",false),checkout=button("Proses Transaksi",true);actions.add(remove);actions.add(checkout);bottom.add(actions);right.add(bottom,BorderLayout.SOUTH);
        remove.addActionListener(e->{int r=cartTable.getSelectedRow();if(r>=0){cart.remove(r);refreshCart();}else error("Pilih item keranjang.");});
        checkout.addActionListener(e->{try{long id=checkoutService.checkout(buyerField.getText(),cart);JOptionPane.showMessageDialog(this,"Transaksi berhasil!\\nNomor internal: "+id,"Sukses",JOptionPane.INFORMATION_MESSAGE);
            cart.clear();buyerField.setText("");refreshCart();refreshProducts();refreshHistory();loadCatalog.run();status("Transaksi berhasil disimpan.");}catch(Exception ex){error(ex);}});
        left.setPreferredSize(new Dimension(520,500));root.add(left,BorderLayout.CENTER);root.add(right,BorderLayout.EAST);right.setPreferredSize(new Dimension(520,500));return root;
    }
    private void refreshCart(){
        if(cartModel==null)return;cartModel.setRowCount(0);BigDecimal total=BigDecimal.ZERO;
        for(CartItem i:cart){cartModel.addRow(new Object[]{i.getCodeSnapshot(),i.getNameSnapshot(),money.format(i.getUnitPriceSnapshot()),i.getQuantity(),money.format(i.getSubtotal())});total=total.add(i.getSubtotal());}
        totalLabel.setText("TOTAL  "+money.format(total));
    }
    private JPanel historyPanel(){
        JPanel root=basePanel();JPanel searchCard=card("Cari Riwayat");historySearch=field("Nomor transaksi atau nama pembeli");JButton find=button("Cari",true),reset=button("Reset",false);
        JPanel row=new JPanel(new FlowLayout(FlowLayout.LEFT));row.setOpaque(false);row.add(historySearch);row.add(find);row.add(reset);searchCard.add(row,BorderLayout.CENTER);root.add(searchCard,BorderLayout.NORTH);
        historyModel=model("ID","Nomor Transaksi","Nama Pembeli","Total","Waktu");historyTable=table(historyModel);JPanel list=card("Transaksi Tersimpan");list.add(new JScrollPane(historyTable),BorderLayout.CENTER);root.add(list,BorderLayout.CENTER);
        JButton detail=button("Lihat Detail Item",true);JPanel foot=new JPanel(new FlowLayout(FlowLayout.RIGHT));foot.setOpaque(false);foot.add(detail);root.add(foot,BorderLayout.SOUTH);
        find.addActionListener(e->refreshHistory());historySearch.addActionListener(e->refreshHistory());reset.addActionListener(e->{historySearch.setText("");refreshHistory();});
        detail.addActionListener(e->showSaleDetail());return root;
    }
    private void refreshHistory(){
        if(historyModel==null)return;try{historyModel.setRowCount(0);for(Sale s:saleDAO.search(historySearch==null?"":historySearch.getText()))
            historyModel.addRow(new Object[]{s.getId(),s.getInvoiceNo(),s.getBuyerName(),money.format(s.getTotal()),s.getCreatedAt().format(DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm"))});}
        catch(Exception ex){error(ex);}
    }
    private void showSaleDetail(){
        int r=historyTable.getSelectedRow();if(r<0){error("Pilih transaksi terlebih dahulu.");return;}
        try{long id=Long.parseLong(historyModel.getValueAt(r,0).toString());StringBuilder b=new StringBuilder("Detail item (snapshot):\\n\\n");
            for(SaleItem i:saleDAO.getItems(id))b.append(i.getCodeSnapshot()).append(" • ").append(i.getNameSnapshot()).append("\\n")
                .append(i.getQuantity()).append(" x ").append(money.format(i.getUnitPriceSnapshot())).append(" = ").append(money.format(i.getSubtotal())).append("\\n\\n");
            JOptionPane.showMessageDialog(this,b.toString(),"Detail Transaksi",JOptionPane.INFORMATION_MESSAGE);
        }catch(Exception ex){error(ex);}
    }
    private void status(String s){if(statusLabel!=null)statusLabel.setText("  "+s);}
    private void error(Exception ex){error(ex.getMessage()==null?ex.toString():ex.getMessage());}
    private void error(String message){JOptionPane.showMessageDialog(this,message,"Perhatian",JOptionPane.WARNING_MESSAGE);}
}
