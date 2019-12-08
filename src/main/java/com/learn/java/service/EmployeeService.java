package com.learn.java.service;

import com.learn.java.elasticsearch.document.Employee;
import com.learn.java.elasticsearch.repository.EmployeeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EmployeeService {

    // The dao repository will use the Elastic-Search-Repository to perform the database operations.
    @Autowired
    private EmployeeRepository edao;

    /* (non-Javadoc)
     * @see com.springboot.elasticsearch.service.Employeeserv#saveEmployee(java.util.List)
     */
    public void saveEmployee(List<Employee> employees) {
        edao.saveAll(employees);
    }

    /* (non-Javadoc)
     * @see com.springboot.elasticsearch.service.Employeeserv#findAllEmployees()
     */
    public Iterable<Employee> findAllEmployees() {
        return edao.findAll();
    }

    /* (non-Javadoc)
     * @see com.springboot.elasticsearch.service.Employeeserv#findByDesignation(java.lang.String)
     */
    public List<Employee> findByDesignation(String designation) {
        return edao.findByDesignation(designation);
    }

}
