package se.foitekelund.ikarest.pojos;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class Receipt {

    private String id;
    private String date;
    private String store;
    private String totalAmount;
    private String discount;
    private String loyaltyPoints;

    private List<ReceiptItem> receiptItemList;

    public Receipt() {

    }
}
