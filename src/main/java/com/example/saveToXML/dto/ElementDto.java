package com.example.saveToXML.dto;

import lombok.Data;
import java.util.Map;

/**
 * Класс хранения имени элементов XML файлов
 * @author Ayrat Zagretdinov
 * created on 15.06.2021
 */
@Data
public class ElementDto {
    private String name;
    private Map<String, String> attributes;
    private String textValue;

    public ElementDto(String name, Map<String, String> attributes, String textValue) {
        this.name = name;
        this.attributes = attributes;
        this.textValue = textValue;
    }

    public ElementDto(String name, Map<String, String> attributes) {
        this.name = name;
        this.attributes = attributes;
    }

    public ElementDto(Map<String, String> attributes) {
        this.attributes = attributes;
    }

    public ElementDto(String name) {
        this.name = name;
    }
}