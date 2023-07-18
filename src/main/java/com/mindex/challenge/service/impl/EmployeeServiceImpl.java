package com.mindex.challenge.service.impl;

import static java.util.stream.Collectors.toSet;

import com.mindex.challenge.dao.EmployeeRepository;
import com.mindex.challenge.data.Employee;
import com.mindex.challenge.data.ReportingStructure;
import com.mindex.challenge.service.EmployeeService;
import java.util.HashSet;
import java.util.Set;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.UUID;

@Service
public class EmployeeServiceImpl implements EmployeeService {

  private static final Logger LOG = LoggerFactory.getLogger(EmployeeServiceImpl.class);

  @Autowired
  private EmployeeRepository employeeRepository;

  @Override
  public Employee create(Employee employee) {
    LOG.debug("Creating employee [{}]", employee);

    employee.setEmployeeId(UUID.randomUUID().toString());
    employeeRepository.insert(employee);

    return employee;
  }

  @Override
  public Employee read(String id) {
    LOG.debug("Creating employee with id [{}]", id);

    Employee employee = employeeRepository.findByEmployeeId(id);

    if (employee == null) {
      throw new RuntimeException("Invalid employeeId: " + id);
    }

    return employee;
  }

  @Override
  public Employee update(Employee employee) {
    LOG.debug("Updating employee [{}]", employee);

    return employeeRepository.save(employee);
  }

  /**
   * Returns a set of all employee IDs that report to a specified employee, either directly or
   * indirectly.
   *
   * @param root Top-level employee to check for direct/indirect reports.
   * @return All employee IDs listed directly or indirectly below @{link root}.
   */
  private Set<String> getNestedReportsIds(Employee root) {
    Set<String> nestedReportsIds = new HashSet<>();

    if (root.getDirectReports() == null) {
      return Set.of();
    }

    Set<Employee> employeeReports = root.getDirectReports().stream()
        .map(Employee::getEmployeeId)
        .map(employeeRepository::findByEmployeeId)
        .collect(toSet());

    for (Employee employee : employeeReports) {
      // Only add reports for employees we've not seen previously.
      if (nestedReportsIds.add(employee.getEmployeeId())) {
        nestedReportsIds.addAll(getNestedReportsIds(employee));
      }
    }

    return nestedReportsIds;
  }

  @Override
  public ReportingStructure getReportingStructure(String id) {
    LOG.debug("Computing reporting structure for employee with id [{}]", id);

    Employee employee = employeeRepository.findByEmployeeId(id);
    if (employee == null) {
      throw new RuntimeException("Invalid employeeId: " + id);
    }

    // By counting the employee IDs found in this set, we get the number of unique employees
    // who are found in this employee's reporting hierarchy. Doing the calculation this way
    // addresses potential issues with reports having multiple managers.
    int totalNumberOfReports = getNestedReportsIds(employee).size();

    ReportingStructure reporting = new ReportingStructure();
    reporting.setEmployee(employee);
    reporting.setNumberOfReports(totalNumberOfReports);

    return reporting;
  }
}
