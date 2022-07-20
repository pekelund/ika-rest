package se.foitekelund.ikarest;

import org.apache.pdfbox.text.PDFTextStripper;
import org.apache.pdfbox.text.TextPosition;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class PDFLineStripper extends PDFTextStripper {

    static List<String> lines = new ArrayList<String>();

    public PDFLineStripper() throws IOException {
        super();
    }

    /**
     * Override the default functionality of PDFTextStripper.writeString()
     */
    @Override
    protected void writeString(String str, List<TextPosition> textPositions) throws IOException {
        lines.add(str);
        // you may process the line here itself, as and when it is obtained
    }

    List<String> getLines() {
        return lines;
    }

}
