package com.example.saveToXML.service;

import lombok.extern.slf4j.Slf4j;
import org.w3c.dom.Document;
import org.xml.sax.SAXException;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.IOException;

/**
 * @author Ayrat Zagretdinov
 * created on 15.06.2021
 */
@Slf4j
public class DocumentBuilderXML {
    private DocumentBuilder documentBuilder;
    private Document document;

    public DocumentBuilder newDocumentBuilder() {
        try {
            DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();
            return documentBuilder;
        } catch (ParserConfigurationException e) {
            log.error("{}", e);
            throw new RuntimeException(e);
        }
    }

    public Document parse(File file) {
        DocumentBuilder builder = newDocumentBuilder();
        try {
            Document document = builder.parse(file);
            return document;
        } catch (SAXException | IOException e) {
            log.error("{}", e);
            throw new RuntimeException(e);
        }
    }
}
