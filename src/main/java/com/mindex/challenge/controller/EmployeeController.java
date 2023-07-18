package com.mindex.challenge.controller;

import com.mindex.challenge.data.Compensation;
import com.mindex.challenge.data.Employee;
import com.mindex.challenge.data.ReportingStructure;
import com.mindex.challenge.service.EmployeeService;
import java.time.Instant;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
public class EmployeeController {

  private static final Logger LOG = LoggerFactory.getLogger(EmployeeController.class);

  @Autowired
  private EmployeeService employeeService;

  @PostMapping("/employee")
  public Employee create(@RequestBody Employee employee) {
    LOG.debug("Received employee create request for [{}]", employee);

    return employeeService.create(employee);
  }

  @GetMapping("/employee/{id}")
  public Employee read(@PathVariable String id) {
    LOG.debug("Received employee create request for id [{}]", id);

    return employeeService.read(id);
  }

  @PutMapping("/employee/{id}")
  public Employee update(@PathVariable String id, @RequestBody Employee employee) {
    LOG.debug("Received employee create request for id [{}] and employee [{}]", id, employee);

    employee.setEmployeeId(id);
    return employeeService.update(employee);
  }

  @GetMapping("/employee/{id}/reporting")
  public ReportingStructure getReportingStructure(@PathVariable String id) {
    LOG.debug("Received get reporting structure request for employee id [{}]", id);

    return employeeService.getReportingStructure(id);
  }

  @GetMapping("/employee/{id}/compensation")
  public Compensation readCompensation(@PathVariable String id) {
    LOG.debug("Received get compensation request for employee id [{}]", id);

    return employeeService.readCompensation(id);
  }

  @PostMapping("/employee/{id}/compensation")
  public Compensation createCompensation(@PathVariable String id,
      @RequestParam(name = "salaryInCents") long salaryInCents,
      @RequestParam(name = "effectiveDate") String dateIso) {
    LOG.debug("Received create compensation request for id [{}]", id);

    return employeeService.createCompensation(id, salaryInCents, Instant.parse(dateIso));
  }
}
