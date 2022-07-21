package se.foitekelund.ikarest.utils;

import lombok.Getter;
import lombok.Setter;
import se.foitekelund.ikarest.pojos.Receipt;
import se.foitekelund.ikarest.pojos.ReceiptItem;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class ReceiptParser {
    private final String receiptString;
    private State state;
    private final String lineSeparator;

    private Receipt receipt;

    public ReceiptParser(String receiptString, String lineSeparator) {
        this.receiptString = receiptString;
        this.lineSeparator = lineSeparator;
        state = State.INIT;
    }

    public Receipt parse() {
        String parts[] = receiptString.split(this.lineSeparator);
        ReceiptItem receiptItem = null;
        List<ReceiptItem> receiptItemList = new ArrayList<>();

        for (String line : parts) {

            switch (state) {
                case INIT -> {
                    if ("KVITTO".equals(line)) {
                        receipt = new Receipt();
                        state = State.STORE_INFO;
                    }
                    break;
                }
                case STORE_INFO -> {
                    if (receipt.getStore() == null) {
                        receipt.setStore(line);
                    } else if (line.contains("Datum")) {
                        receipt.setDate(line.substring(line.lastIndexOf(':') + 2));
                    } else if (line.startsWith("Beskrivning")) {
                        state = State.ITEMS;
                    }
                    break;
                }
                case ITEMS -> {
                    if (!line.contains("Total:")) {
                        receiptItem = new ReceiptItem();

                        receiptItem.setFirstLine(line);
                        if (line.startsWith("*")) {
                            state = State.ITEM_DETAILS;
                        } else {
                            receiptItemList.add(receiptItem);
                        }
                        break;
                    }
                    else {
                        state = State.PAYMENT_INFO;
                        receipt.setReceiptItemList(receiptItemList);
                        receipt.setTotalAmount(line.substring(line.lastIndexOf(':')+2));
                    }
                }
                case ITEM_DETAILS -> {
                    if (line.matches(".+[0-9]{7}.+")) {
                        System.out.println("line with ean = " + line);
                        receiptItem.setFirstLine(line);
                        if (!line.startsWith(("*"))) {
                            receiptItemList.add(receiptItem);
                            state = State.ITEMS;
                        }
                    } else {
                        receiptItem.setSecondLine(line);
                        receiptItemList.add(receiptItem);
                        state = State.ITEMS;
                    }
                    break;
                }
                case PAYMENT_INFO -> {
                    if (line.contains("Erhållen rabatt")) {
                        receipt.setDiscount(line.substring(line.lastIndexOf(':')+2));
                    } else if (line.contains("Lojalitetspoäng")) {
                        receipt.setLoyaltyPoints(line.substring(line.lastIndexOf(':')+2));
                    } else if (line.matches("[0-9]+")) {
                        receipt.setId(line);
                    }
                    break;
                }
            }
        }
        return receipt;
    }
}
