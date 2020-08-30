/*
package com.learn.java.controller;

import com.learn.java.elasticsearch.document.Employee;
import com.learn.java.service.EmployeeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value= "/employee")
public class EmployeeController {

    @Autowired
    EmployeeService eserv;

    @PostMapping(value= "/saveemployees")
    public String saveEmployee(@RequestBody List<Employee> myemployees) {
        eserv.saveEmployee(myemployees);
        return "Records saved in the db.";
    }

    @GetMapping(value= "/getall")
    public Iterable<Employee> getAllEmployees() {
        return eserv.findAllEmployees();
    }

    @GetMapping(value= "/findbydesignation/{employee-designation}")
    public Iterable<Employee> getByDesignation(@PathVariable(name= "employee-designation") String designation) {
        return eserv.findByDesignation(designation);
    }
}
*/
