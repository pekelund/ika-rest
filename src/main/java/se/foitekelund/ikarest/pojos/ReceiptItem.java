package se.foitekelund.ikarest.pojos;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Arrays;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ReceiptItem {
    private String ean;
    private String item;
    private String description;
    private String aPrice;
    private String price;
    private String amount;
    private String discount;
    private String discountPercent;

    public void setFirstLine(String line) {
        String[] items = line.split(" ");

        setEan(items[items.length-5]);
        setAPrice(items[items.length-4]);
        setPrice(items[items.length-1]);
        setAmount(items[items.length-3] + " " + items[items.length-2]);

        String[] names = Arrays.copyOfRange(items, 0, items.length-5);
        for (String s : names) {
            System.out.println("s = " + s);
        }
        setItem(String.join(" ", names));
        System.out.println("getAPrice() = " + getAPrice());
    }

    public void setSecondLine(String line) {
        String[] items = line.split(" ");

        setDiscount(items[items.length-1]);
        int descriptionEnd = items.length - 2;
        if (items[items.length-3].contains("%")) {
            setDiscountPercent(items[items.length-3]);
            descriptionEnd = items.length-3;
        }
        String[] names = Arrays.copyOfRange(items, 0, descriptionEnd);
        setDescription(String.join(" ", names));

    }
}
