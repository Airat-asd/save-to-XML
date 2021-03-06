package com.example.saveToXML.entity;

import com.example.saveToXML.dto.ElementDto;
import java.util.Map;

public interface XmlType {
    Map<String, String> getRoot();
    String getRootName();
    ElementDto getElement();
}
