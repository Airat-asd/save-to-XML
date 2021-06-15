package com.example.saveToXML.service;

import com.example.saveToXML.entity.Department;
import com.example.saveToXML.repository.DepartmentRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.*;

@Service
@Slf4j
public class Synchronization {

    @Autowired
    private DepartmentRepository repository;

    public void synchronize(Set<Department> departmentListFromXML) {
        log.trace("start method: public void synchronize(List<Department> departmentListFromXML)");
        List<Department> departmentListFromDB = repository.findAll();
        Map<Integer, Department> hashCodeMapFromXML = new HashMap<>();
        List<Department> departmentListForInsert = new ArrayList<>();
        List<Department> departmentListForUpdate = new ArrayList<>();
        List<Department> departmentListForDelete = new ArrayList<>();

        departmentListFromXML.forEach(department -> hashCodeMapFromXML.put(department.hashCode(), department));

        departmentListFromDB.forEach(departmentFromDB -> {
            int hashCodeFromDB = departmentFromDB.hashCode();
            if (hashCodeMapFromXML.containsKey(hashCodeFromDB)) {
                Department departmentFromXML = hashCodeMapFromXML.get(hashCodeFromDB);
                if (!departmentFromDB.getDescription().equals(departmentFromXML.getDescription())) {
                    departmentFromXML.setId(departmentFromDB.getId());
                    departmentListForUpdate.add(departmentFromXML);
                    hashCodeMapFromXML.remove(hashCodeFromDB);
                }
            } else {
                departmentListForDelete.add(departmentFromDB);
            }
        });

        hashCodeMapFromXML.forEach((hashCode, department) -> departmentListForInsert.add(department));

        log.trace("insert = {}", departmentListForInsert);//delete
        log.trace("delete = {}", departmentListForDelete);//delete
        log.trace("update = {}", departmentListForUpdate);//delete
        repository.databaseSynchronization(departmentListForDelete, departmentListForUpdate, departmentListForInsert);
    }
}