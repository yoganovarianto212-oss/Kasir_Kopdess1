package id.koperasidesa.model;

import java.math.BigDecimal;

public class SaleItem {
    private final String codeSnapshot, nameSnapshot;
    private final BigDecimal unitPriceSnapshot, subtotal;
    private final int quantity;
    public SaleItem(String code,String name,BigDecimal price,int quantity,BigDecimal subtotal){
        this.codeSnapshot=code;this.nameSnapshot=name;this.unitPriceSnapshot=price;this.quantity=quantity;this.subtotal=subtotal;
    }
    public String getCodeSnapshot(){return codeSnapshot;} public String getNameSnapshot(){return nameSnapshot;}
    public BigDecimal getUnitPriceSnapshot(){return unitPriceSnapshot;} public int getQuantity(){return quantity;}
    public BigDecimal getSubtotal(){return subtotal;}
}
