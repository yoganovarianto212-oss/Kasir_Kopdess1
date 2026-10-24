package id.koperasidesa.model;

import java.math.BigDecimal;

public class Product {
    private long id;
    private String code, name, category;
    private BigDecimal price;
    private int stock;
    private boolean active;

    public Product(long id, String code, String name, String category, BigDecimal price, int stock, boolean active) {
        this.id=id; this.code=code; this.name=name; this.category=category;
        this.price=price; this.stock=stock; this.active=active;
    }
    public Product(String code, String name, String category, BigDecimal price, int stock) {
        this(0, code, name, category, price, stock, true);
    }
    public long getId(){return id;} public String getCode(){return code;} public String getName(){return name;}
    public String getCategory(){return category;} public BigDecimal getPrice(){return price;}
    public int getStock(){return stock;} public boolean isActive(){return active;}
    public void setId(long v){id=v;} public void setCode(String v){code=v;} public void setName(String v){name=v;}
    public void setCategory(String v){category=v;} public void setPrice(BigDecimal v){price=v;}
    public void setStock(int v){stock=v;} public void setActive(boolean v){active=v;}
}
