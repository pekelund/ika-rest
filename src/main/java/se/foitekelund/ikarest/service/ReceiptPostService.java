package se.foitekelund.ikarest.service;

import java.io.*;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;
import org.springframework.web.multipart.MultipartFile;
import se.foitekelund.ikarest.pojos.Receipt;
import se.foitekelund.ikarest.utils.ReceiptParser;

public class ReceiptPostService {
    public static Receipt saveFile(String fileName, MultipartFile multipartFile)
            throws IOException {
        Receipt receipt;
        PDDocument doc = null;

       try (InputStream inputStream = multipartFile.getInputStream()) {
            doc = PDDocument.load(inputStream);
            System.out.println("doc = " + doc);
            PDFTextStripper stripper = new PDFTextStripper();

            String result = stripper.getText(doc);
            String parts[] = result.split(stripper.getLineSeparator());
            ReceiptParser receiptParser = new ReceiptParser(result, stripper.getLineSeparator());
            receipt = receiptParser.parse();

        } catch (IOException ioe) {
            throw new IOException("Could not save the file: " + fileName, ioe);
        }
        finally {
            if ( doc != null ) {
                doc.close();
            }
        }

        return receipt;
    }
}
