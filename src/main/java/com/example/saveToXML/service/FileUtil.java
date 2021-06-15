package com.example.saveToXML.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.w3c.dom.Document;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

@Service
@Slf4j
public class FileUtil {

    public void writeDocument(Document document, String fileName) {
        try {
            Transformer transformer = TransformerFactory.newInstance().newTransformer();
            DOMSource domSource = new DOMSource(document);
            FileOutputStream fileOutputStream = new FileOutputStream(fileName);
            StreamResult streamResult = new StreamResult(fileOutputStream);
            transformer.transform(domSource, streamResult);
        } catch (TransformerException | IOException e) {
            log.error("{}", e);
        }
    }

    public File openFile(String fileName) {
        return new File(fileName);
    }
}
