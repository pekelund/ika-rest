package se.foitekelund.ikarest.controllers;

import java.io.IOException;

import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import se.foitekelund.ikarest.ReceiptPostResponse;
import se.foitekelund.ikarest.service.ReceiptPostService;
import se.foitekelund.ikarest.pojos.Receipt;

@RestController
public class ReceiptPostController {

    @PostMapping("/upload")
    public Receipt uploadFile(
            @RequestParam(value = "file") MultipartFile multipartFile)
            throws IOException {

        String fileName = StringUtils.cleanPath(multipartFile.getOriginalFilename());
        long size = multipartFile.getSize();

        System.out.println("fileName = " + fileName);
        System.out.println("size = " + size);

        Receipt receipt = ReceiptPostService.saveFile(fileName, multipartFile);

        ReceiptPostResponse response = new ReceiptPostResponse();
        response.setFileName(fileName);
        response.setSize(size);

        return receipt;
    }
}