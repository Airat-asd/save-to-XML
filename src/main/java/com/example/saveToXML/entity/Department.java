package com.example.saveToXML.entity;

import com.example.saveToXML.dto.ElementDto;
import lombok.*;
import javax.persistence.*;
import java.util.HashMap;
import java.util.Map;

@Getter
@Setter
@NoArgsConstructor
@ToString
@Entity
public class Department extends HashEquals implements XmlType {
    public final static String ROOT_NAME = "Departments";
    public final static String ELEMENT_NAME = "Department";
    public final static String ATTRIBUTE_DEP_CODE_NAME = "depCode";
    public final static String ATTRIBUTE_DEP_JOB_NAME = "depJob";
    public final static String ATTRIBUTE_DESCRIPTION_NAME = "description";

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(length = 20)
    private String depCode;

    @Column(length = 100)
    private String depJob;

    @Column(length = 255)
    private String description;

    @Override
    public Map<String, String> getRoot() {
        return new HashMap<>();
    }

    @Override
    public String getRootName() {
        return ROOT_NAME;
    }

    @Override
    public ElementDto getElement() {
        Map<String, String> attributes = new HashMap<>();
        attributes.put(ATTRIBUTE_DEP_CODE_NAME, depCode);
        attributes.put(ATTRIBUTE_DEP_JOB_NAME, depJob);
        attributes.put(ATTRIBUTE_DESCRIPTION_NAME, description);
        return new ElementDto(ELEMENT_NAME, attributes);
    }

    @Override
    public int hashCode() {
        return super.hashCodeDIY(this);
    }

    @Override
    public boolean equals(Object obj) {
        return super.equalsDIY(this, obj);
    }
}