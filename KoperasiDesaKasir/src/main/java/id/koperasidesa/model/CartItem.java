package id.koperasidesa.model;

import java.math.BigDecimal;

public class CartItem {
    private final long productId;
    private final String codeSnapshot, nameSnapshot;
    private final BigDecimal unitPriceSnapshot;
    private int quantity;

    public CartItem(Product p, int quantity) {
        this.productId=p.getId(); this.codeSnapshot=p.getCode(); this.nameSnapshot=p.getName();
        this.unitPriceSnapshot=p.getPrice(); this.quantity=quantity;
    }
    public long getProductId(){return productId;} public String getCodeSnapshot(){return codeSnapshot;}
    public String getNameSnapshot(){return nameSnapshot;} public BigDecimal getUnitPriceSnapshot(){return unitPriceSnapshot;}
    public int getQuantity(){return quantity;} public void setQuantity(int q){quantity=q;}
    public BigDecimal getSubtotal(){return unitPriceSnapshot.multiply(BigDecimal.valueOf(quantity));}
}
