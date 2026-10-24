package id.koperasidesa.service;

import id.koperasidesa.dao.SaleDAO;
import id.koperasidesa.model.CartItem;
import java.sql.SQLException;
import java.util.List;

public class CheckoutService {
    private final SaleDAO saleDAO;
    public CheckoutService(SaleDAO saleDAO){this.saleDAO=saleDAO;}
    public long checkout(String buyerName,List<CartItem> items)throws SQLException{
        if(buyerName==null||buyerName.trim().isEmpty())throw new IllegalArgumentException("Nama pembeli wajib diisi.");
        if(items==null||items.isEmpty())throw new IllegalArgumentException("Keranjang masih kosong.");
        return saleDAO.checkout(buyerName.trim(),items);
    }
}
