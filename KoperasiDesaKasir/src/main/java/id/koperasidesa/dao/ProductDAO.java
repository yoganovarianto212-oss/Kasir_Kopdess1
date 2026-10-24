package id.koperasidesa.dao;

import id.koperasidesa.config.Database;
import id.koperasidesa.model.Product;
import java.sql.*;
import java.util.*;

public class ProductDAO {
    public List<Product> search(String keyword) throws SQLException {
        String q="%"+(keyword==null?"":keyword.trim())+"%";
        String sql="SELECT * FROM products WHERE active=1 AND (code LIKE ? OR name LIKE ? OR category LIKE ?) ORDER BY name";
        List<Product> list=new ArrayList<>();
        try(Connection c=Database.getConnection();PreparedStatement ps=c.prepareStatement(sql)){
            ps.setString(1,q);ps.setString(2,q);ps.setString(3,q);
            try(ResultSet rs=ps.executeQuery()){while(rs.next()) list.add(map(rs));}
        } return list;
    }
    public void save(Product p) throws SQLException {
        if(p.getId()==0){
            String sql="INSERT INTO products(code,name,category,price,stock,active) VALUES(?,?,?,?,?,1)";
            try(Connection c=Database.getConnection();PreparedStatement ps=c.prepareStatement(sql)){
                bind(ps,p);ps.executeUpdate();
            }
        } else {
            String sql="UPDATE products SET code=?,name=?,category=?,price=?,stock=? WHERE id=? AND active=1";
            try(Connection c=Database.getConnection();PreparedStatement ps=c.prepareStatement(sql)){
                bind(ps,p);ps.setLong(6,p.getId());ps.executeUpdate();
            }
        }
    }
    private void bind(PreparedStatement ps,Product p)throws SQLException{
        ps.setString(1,p.getCode());ps.setString(2,p.getName());ps.setString(3,p.getCategory());
        ps.setBigDecimal(4,p.getPrice());ps.setInt(5,p.getStock());
    }
    public void softDelete(long id)throws SQLException{
        try(Connection c=Database.getConnection();PreparedStatement ps=c.prepareStatement("UPDATE products SET active=0 WHERE id=?")){
            ps.setLong(1,id);ps.executeUpdate();
        }
    }
    private Product map(ResultSet rs)throws SQLException{
        return new Product(rs.getLong("id"),rs.getString("code"),rs.getString("name"),rs.getString("category"),
                rs.getBigDecimal("price"),rs.getInt("stock"),rs.getBoolean("active"));
    }
}
