package com.example.saveToXML.entity;

import com.example.saveToXML.dto.ElementDto;
import java.util.Map;

/**
 * @author Ayrat Zagretdinov
 * created on 15.06.2021
 */
public interface XmlType {
    Map<String, String> getRoot();
    String getRootName();
    ElementDto getElement();
}
