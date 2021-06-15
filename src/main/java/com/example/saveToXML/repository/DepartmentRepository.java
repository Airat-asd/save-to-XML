package com.example.saveToXML.repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import com.example.saveToXML.entity.Department;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;
import java.util.List;
import javax.transaction.Transactional;

/**
 * @author Ayrat Zagretdinov
 * created on 15.06.2021
 */
@Repository
@Slf4j
public class DepartmentRepository {

    @Value("${com.example.saveToXML.messageDelete}")
    private String MESSAGE_DELETE;

    @Value("${com.example.saveToXML.messageUpdate}")
    private String MESSAGE_UPDATE;

    @Value("${com.example.saveToXML.messageInsert}")
    private String MESSAGE_INSERT;

    @PersistenceContext
    private EntityManager entityManager;

    public List<Department> findAll() {
        log.trace("starting method: find(String messageId)");
        Query query = entityManager.createQuery("from Department");
        List<Department> departmentList = query.getResultList();
        return departmentList;
    }

    @Transactional
    public int databaseSynchronization(List<Department> deleteList, List<Department> updateList, List<Department> insertList) {
        log.trace("starting method: public void saveList(List<Department> departmentList)");
        int quantityDelete = 0, quantityUpdate = 0, quantityInsert = 0;
        if (!deleteList.isEmpty()) {
            quantityDelete = entityManager.createNativeQuery(buildDeleteQuery(deleteList)).executeUpdate();
            log.info("{} {}", MESSAGE_DELETE, quantityDelete);
        }
        if (!updateList.isEmpty()) {
            quantityUpdate = entityManager.createNativeQuery(buildUpdateQuery(updateList)).executeUpdate();
            log.info("{} {}", MESSAGE_UPDATE, quantityUpdate);
        }
        if (!insertList.isEmpty()) {
            log.trace("insert = {}", insertList);
            quantityInsert = entityManager.createNativeQuery(buildInsertQuery(insertList)).executeUpdate();
            log.info("{} {}", MESSAGE_INSERT, quantityInsert);
        }
        return quantityDelete + quantityUpdate + quantityInsert;
    }

    private String buildDeleteQuery(List<Department> departmentList) {
        log.trace("starting method: private String buildDeleteQuery(List<Department> departmentList)");
        String idList = departmentList.stream()
                .map(department -> String.valueOf(department.getId()))
                .reduce((id1, id2) -> (id1 + "," + id2)).get();
        return String.format("delete from Department where id in (%s)", idList);
    }

    private String buildUpdateQuery(List<Department> departmentList) {
        log.trace("starting method: private String buildUpdateQuery(List<Department> departmentList)");
        StringBuilder updateQuery = new StringBuilder();
        departmentList.forEach(department -> updateQuery
                .append(String.format("update Department set description = '%s' where id = %s; ", department.getDescription(), department.getId())));
        return updateQuery.toString();
    }

    private String buildInsertQuery(List<Department> departmentList) {
        log.trace("starting method: private String buildInsertQuery(List<Department> departmentList)");
        String values = departmentList.stream()
                .map(department -> String.format("('%s','%s','%s')", department.getDepCode(), department.getDepJob(), department.getDescription()))
                .reduce((value1, value2) -> (value1 + "," + value2)).get();
        return String.format("insert into Department(dep_code, dep_job, description) values %s", values);
    }
}