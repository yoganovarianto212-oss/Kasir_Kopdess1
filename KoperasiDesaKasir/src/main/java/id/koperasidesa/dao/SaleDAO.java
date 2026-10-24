package id.koperasidesa.dao;

import id.koperasidesa.config.Database;
import id.koperasidesa.model.*;
import java.sql.*;
import java.time.LocalDateTime;
import java.util.*;

public class SaleDAO {
    public long checkout(String buyer, List<CartItem> items) throws SQLException {
        if(items.isEmpty()) throw new SQLException("Keranjang masih kosong.");
        try(Connection c=Database.getConnection()){
            c.setAutoCommit(false);
            try {
                // Validasi stok dan kurangi stok di transaksi yang sama.
                for(CartItem item:items){
                    try(PreparedStatement ps=c.prepareStatement("SELECT stock FROM products WHERE id=? AND active=1 FOR UPDATE")){
                        ps.setLong(1,item.getProductId());
                        try(ResultSet rs=ps.executeQuery()){
                            if(!rs.next()) throw new SQLException("Barang "+item.getNameSnapshot()+" tidak lagi tersedia.");
                            if(rs.getInt(1)<item.getQuantity()) throw new SQLException("Stok tidak cukup untuk "+item.getNameSnapshot()+".");
                        }
                    }
                    try(PreparedStatement ps=c.prepareStatement("UPDATE products SET stock=stock-? WHERE id=? AND active=1")){
                        ps.setInt(1,item.getQuantity());ps.setLong(2,item.getProductId());ps.executeUpdate();
                    }
                }
                java.math.BigDecimal total=items.stream().map(CartItem::getSubtotal).reduce(java.math.BigDecimal.ZERO,java.math.BigDecimal::add);
                String invoice="KOP-"+System.currentTimeMillis();
                long saleId;
                try(PreparedStatement ps=c.prepareStatement("INSERT INTO sales(invoice_no,buyer_name,total) VALUES(?,?,?)",Statement.RETURN_GENERATED_KEYS)){
                    ps.setString(1,invoice);ps.setString(2,buyer);ps.setBigDecimal(3,total);ps.executeUpdate();
                    try(ResultSet keys=ps.getGeneratedKeys()){if(!keys.next())throw new SQLException("Gagal membuat transaksi.");saleId=keys.getLong(1);}
                }
                String itemSql="INSERT INTO sale_items(sale_id,product_id,product_code_snapshot,product_name_snapshot,unit_price_snapshot,quantity,subtotal) VALUES(?,?,?,?,?,?,?)";
                try(PreparedStatement ps=c.prepareStatement(itemSql)){
                    for(CartItem item:items){
                        ps.setLong(1,saleId);ps.setLong(2,item.getProductId());
                        ps.setString(3,item.getCodeSnapshot());ps.setString(4,item.getNameSnapshot());
                        ps.setBigDecimal(5,item.getUnitPriceSnapshot());ps.setInt(6,item.getQuantity());ps.setBigDecimal(7,item.getSubtotal());ps.addBatch();
                    } ps.executeBatch();
                }
                c.commit();return saleId;
            } catch(SQLException ex){c.rollback();throw ex;}
            finally{c.setAutoCommit(true);}
        }
    }
    public List<Sale> search(String keyword)throws SQLException{
        String q="%"+(keyword==null?"":keyword.trim())+"%";
        List<Sale> out=new ArrayList<>();
        String sql="SELECT * FROM sales WHERE invoice_no LIKE ? OR buyer_name LIKE ? ORDER BY created_at DESC";
        try(Connection c=Database.getConnection();PreparedStatement ps=c.prepareStatement(sql)){
            ps.setString(1,q);ps.setString(2,q);
            try(ResultSet rs=ps.executeQuery()){while(rs.next())out.add(new Sale(rs.getLong("id"),rs.getString("invoice_no"),
                    rs.getString("buyer_name"),rs.getBigDecimal("total"),rs.getTimestamp("created_at").toLocalDateTime()));}
        }return out;
    }
    public List<SaleItem> getItems(long saleId)throws SQLException{
        List<SaleItem> out=new ArrayList<>();
        String sql="SELECT * FROM sale_items WHERE sale_id=? ORDER BY id";
        try(Connection c=Database.getConnection();PreparedStatement ps=c.prepareStatement(sql)){
            ps.setLong(1,saleId);
            try(ResultSet rs=ps.executeQuery()){while(rs.next())out.add(new SaleItem(rs.getString("product_code_snapshot"),
                    rs.getString("product_name_snapshot"),rs.getBigDecimal("unit_price_snapshot"),rs.getInt("quantity"),rs.getBigDecimal("subtotal")));}
        }return out;
    }
}
