package com.example.saveToXML.service;

import com.example.saveToXML.dto.ElementDto;
import com.example.saveToXML.entity.XmlType;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.w3c.dom.*;
import org.xml.sax.SAXException;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@Slf4j
public class XmlUtil<T extends XmlType>{

    public Document marshall(List<? extends XmlType> xmlTypeList) throws ParserConfigurationException { //сделать проверку на null
        log.trace("start method: public Document marshall(List<? extends XmlType> xmlTypeList)");
        DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();
        Document document = documentBuilder.newDocument();

        XmlType rootXmlType = xmlTypeList.get(0);
        Element root = document.createElement(rootXmlType.getRootName());
        if (rootXmlType.getRoot() != null) {
            if (!rootXmlType.getRoot().isEmpty()) {
                rootXmlType.getRoot()
                        .forEach((k, v) -> root.setAttribute(k, v));
            }
        }

        xmlTypeList.forEach(xmlType -> {
            var elementXmlType = xmlType.getElement();
            Element element = document.createElement(elementXmlType.getName());
            if (elementXmlType.getTextValue() != null) {
                element.setTextContent(elementXmlType.getTextValue());
            }
            elementXmlType.getAttributes().forEach((nameAttribute, valueAttribute) -> element.setAttribute(nameAttribute, valueAttribute));
            root.appendChild(element);
        });

        document.appendChild(root);
        return document;
    }

    public List<ElementDto> unmarshall(File file, XmlType xmlType) throws ParserConfigurationException, IOException, SAXException {
        log.trace("start method: public void unmarshall(File file)");
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();
        Document document = builder.parse(file);

        List<ElementDto> elementDtoList = new ArrayList<>();
        Element documentElement = document.getDocumentElement();
        if (documentElement.getNodeName().equals(xmlType.getRootName())) {
            NodeList childNodes = documentElement.getChildNodes();
            for (int i = 0; i < childNodes.getLength(); i++) {
                Map<String, String> mapAttributes = new HashMap<>();
                NamedNodeMap attributes = childNodes.item(i).getAttributes();
                for (int j = 0; j < attributes.getLength(); j++) {
                    mapAttributes.put(attributes.item(j).getNodeName(), attributes.item(j).getNodeValue());
                }
                ElementDto elementDto = new ElementDto(mapAttributes);
                elementDtoList.add(elementDto);
            }
        }

        return elementDtoList;
    }
}