package com.example.saveToXML.service;

import com.example.saveToXML.dto.ElementDto;
import com.example.saveToXML.entity.Department;
import com.example.saveToXML.repository.DepartmentRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationArguments;
import org.springframework.stereotype.Component;
import org.w3c.dom.Document;
import org.xml.sax.SAXException;
import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Component
@Slf4j
public class Executor implements InitializingBean {

    @Value("${com.example.saveToXML.fileExtension}")
    private String FILE_EXTENSION;

    @Value("${com.example.saveToXML.errorInvalidCommand}")
    private String ERROR_INVALID_COMMAND;

    @Value("${com.example.saveToXML.errorUniqueId}")
    private String ERROR_UNIQUE_ID;

    @Value("${com.example.saveToXML.commandSave}")
    private String COMMAND_SAVE;

    @Value("${com.example.saveToXML.commandSync}")
    private String COMMAND_SYNC;

    @Value("${com.example.saveToXML.messageSave}")
    private String MESSAGE_SAVE;

    @Value("${com.example.saveToXML.messageSync}")
    private String MESSAGE_SYNC;

    @Value("${com.example.saveToXML.errorParameters}")
    private String ERROR_PARAMETERS;

    @Autowired
    private ApplicationArguments applicationArguments;

    @Autowired
    private DepartmentRepository departmentRepository;

    @Autowired
    private FileUtil fileUtil;

    @Autowired
    private DtoUtil dtoUtil;

    @Autowired
    XmlUtil xmlUtil;

    @Autowired
    Synchronization synchronization;

    @Override
    public void afterPropertiesSet() throws ParserConfigurationException, IOException, SAXException {
        log.trace("start method: public void afterPropertiesSet()");
        String[] parameters = applicationArguments.getSourceArgs();
        if (parameters.length < 2) {
            log.error(ERROR_PARAMETERS);
        } else {
            if (parameters[0].equals(COMMAND_SAVE)) {
                log.info(MESSAGE_SAVE + " " + parameters[1] + FILE_EXTENSION);
                List<Department> departmentList = departmentRepository.findAll();
                Document document = xmlUtil.marshall(departmentList);
                fileUtil.writeDocument(document, parameters[1] + FILE_EXTENSION);
            } else {
                if (parameters[0].equals(COMMAND_SYNC)) {
                    log.info(MESSAGE_SYNC + " " + parameters[1] + FILE_EXTENSION);
                    List<ElementDto> elementDtoList = xmlUtil.
                            unmarshall(fileUtil.openFile(parameters[1] + FILE_EXTENSION), new Department());
                    Set<Department> departmentList = new HashSet<>();
                    elementDtoList.forEach(elementDto -> {
                        Department department = dtoUtil.element2Department(elementDto);
                        boolean add = departmentList.add(department);
                        if (!add) {
                            try {
                                throw new Exception(ERROR_UNIQUE_ID);
                            } catch (Exception e) {
                                log.error("{}", e);
                            }
                        }
                    });

                    synchronization.synchronize(departmentList);

                } else {
                    IllegalArgumentException exception = new IllegalArgumentException(ERROR_INVALID_COMMAND);
                    log.error("{}", exception.getMessage());
                    throw exception;
                }
            }
        }
    }
}