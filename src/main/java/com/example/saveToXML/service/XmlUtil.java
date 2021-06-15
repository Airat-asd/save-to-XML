package com.example.saveToXML.service;

import com.example.saveToXML.dto.ElementDto;
import com.example.saveToXML.entity.XmlType;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.w3c.dom.*;
import javax.xml.parsers.DocumentBuilder;
import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Ayrat Zagretdinov
 * created on 15.06.2021
 */
@Service
@Slf4j
public class XmlUtil<T extends XmlType>{
    private DocumentBuilderXML documentBuilderXML = new DocumentBuilderXML();

    public Document marshall(List<? extends XmlType> xmlTypeList) {
        log.trace("start method: public Document marshall(List<? extends XmlType> xmlTypeList)");
        DocumentBuilder documentBuilder = documentBuilderXML.newDocumentBuilder();
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

    public List<ElementDto> unmarshall(File file, XmlType xmlType) {
        log.trace("start method: public void unmarshall(File file)");
        Document document = documentBuilderXML.parse(file);

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