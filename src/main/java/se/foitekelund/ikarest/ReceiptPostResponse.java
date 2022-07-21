package se.foitekelund.ikarest;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ReceiptPostResponse {
    private String fileName;
    private String downloadUri;
    private long size;
}
