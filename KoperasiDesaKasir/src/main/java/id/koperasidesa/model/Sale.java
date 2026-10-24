package id.koperasidesa.model;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class Sale {
    private final long id;
    private final String invoiceNo, buyerName;
    private final BigDecimal total;
    private final LocalDateTime createdAt;
    public Sale(long id,String invoiceNo,String buyerName,BigDecimal total,LocalDateTime createdAt){
        this.id=id;this.invoiceNo=invoiceNo;this.buyerName=buyerName;this.total=total;this.createdAt=createdAt;
    }
    public long getId(){return id;} public String getInvoiceNo(){return invoiceNo;}
    public String getBuyerName(){return buyerName;} public BigDecimal getTotal(){return total;}
    public LocalDateTime getCreatedAt(){return createdAt;}
}
