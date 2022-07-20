package se.foitekelund.ikarest;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;
import org.springframework.web.multipart.MultipartFile;

public class FileUploadUtil {
    public static Receipt saveFile(String fileName, MultipartFile multipartFile)
            throws IOException {
        Receipt receipt = null;
        Path uploadPath = Paths.get("Files-Upload");

        if (!Files.exists(uploadPath)) {
            Files.createDirectories(uploadPath);
        }

        String fileCode = "ABC"; //RandomStringUtils.randomAlphanumeric(8);
        PDDocument doc = null;

        try (InputStream inputStream = multipartFile.getInputStream()) {
            doc = PDDocument.load(inputStream);
            System.out.println("doc = " + doc);
            System.out.println("doc.getDocumentInformation() = " + doc.getDocumentInformation());
            System.out.println("doc.getDocumentInformation().getAuthor() = " + doc.getDocumentInformation().getAuthor());
            PDFTextStripper stripper = new PDFTextStripper();

 /*           stripper.setSortByPosition( true );
            stripper.setStartPage( 0 );
            stripper.setEndPage( doc.getNumberOfPages() );

            Writer dummy = new OutputStreamWriter(new ByteArrayOutputStream());
            stripper.writeText(doc, dummy);

            for(String line: ((PDFLineStripper) stripper).getLines()){
                System.out.println(line);
            }*/

            String result = stripper.getText(doc);
            String parts[] = result.split(stripper.getLineSeparator());
            ReceiptParser receiptParser = new ReceiptParser(result, stripper.getLineSeparator());
            receipt = receiptParser.parse();

            //System.out.println("result = " + result);

            Path filePath = uploadPath.resolve(fileCode + "-" + fileName);
            Files.copy(inputStream, filePath, StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException ioe) {
            throw new IOException("Could not save file: " + fileName, ioe);
        }
        finally {
            if( doc != null ) {
                doc.close();
            }
        }

        return receipt;
    }
}
