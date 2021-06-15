package com.example.saveToXML.service;

import com.example.saveToXML.dto.ElementDto;
import com.example.saveToXML.entity.Department;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * @author Ayrat Zagretdinov
 * created on 15.06.2021
 */
@Service
@Slf4j
public class DtoUtil {

    public Department element2Department(ElementDto elementDto) {
        log.trace("starting method: public Department element2Department(ElementDto elementDto)");
        Department department = new Department();
        elementDto.getAttributes().forEach((nameAttribute, value) -> {
            if (nameAttribute.equals(Department.ATTRIBUTE_DEP_CODE_NAME)) {
                department.setDepCode(value);
            };
            if (nameAttribute.equals(Department.ATTRIBUTE_DEP_JOB_NAME)) {
                department.setDepJob(value);
            };
            if (nameAttribute.equals(Department.ATTRIBUTE_DESCRIPTION_NAME)) {
                department.setDescription(value);
            };
        });
        return department;
    }
}