package com.example.saveToXML.repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import com.example.saveToXML.entity.Department;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;
import java.util.List;
import javax.transaction.Transactional;

@Repository
@Slf4j
public class DepartmentRepository {

    @PersistenceContext
    private EntityManager entityManager;

    public List<Department> findAll() {
        log.trace("starting method: find(String messageId)");
        Query query = entityManager.createQuery("from Department");
        List<Department> departmentList = query.getResultList();
        return departmentList;
    }

    @Transactional
    public void databaseSynchronization(List<Department> deleteList, List<Department> updateList, List<Department> insertList) {
        log.trace("starting method: public void saveList(List<Department> departmentList)");
        if (!deleteList.isEmpty()) {
            entityManager.createNativeQuery(buildDeleteQuery(deleteList)).executeUpdate();
        }
        if (!updateList.isEmpty()) {
            entityManager.createNativeQuery(buildUpdateQuery(updateList)).executeUpdate();
        }
        if (!insertList.isEmpty()) {
            entityManager.createNativeQuery(buildInsertQuery(insertList)).executeUpdate();
        }
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