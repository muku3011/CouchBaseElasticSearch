package com.learn.java.elasticsearch.repository;

import com.learn.java.elasticsearch.document.Employee;
import org.springframework.data.elasticsearch.repository.ElasticsearchCrudRepository;

import java.util.List;

public interface EmployeeRepository extends ElasticsearchCrudRepository<Employee, String> {

    List<Employee> findByDesignation(String designation);

}