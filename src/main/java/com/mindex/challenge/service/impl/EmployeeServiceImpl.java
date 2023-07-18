package com.mindex.challenge.service.impl;

import static java.util.Collections.emptySet;
import static java.util.stream.Collectors.toSet;

import com.mindex.challenge.dao.CompensationRepository;
import com.mindex.challenge.dao.EmployeeRepository;
import com.mindex.challenge.data.Compensation;
import com.mindex.challenge.data.CompensationDataRecord;
import com.mindex.challenge.data.Employee;
import com.mindex.challenge.data.ReportingStructure;
import com.mindex.challenge.service.EmployeeService;
import java.time.Instant;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class EmployeeServiceImpl implements EmployeeService {

  private static final Logger LOG = LoggerFactory.getLogger(EmployeeServiceImpl.class);

  @Autowired
  private EmployeeRepository employeeRepository;
  @Autowired
  private CompensationRepository compensationRepository;

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
      return emptySet();
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

    return new ReportingStructure(employee, totalNumberOfReports);
  }

  private Compensation getCompensationFromData(CompensationDataRecord data) {
    String employeeId = data.getEmployeeId();

    Employee employee = employeeRepository.findByEmployeeId(employeeId);
    if (employee == null) {
      throw new RuntimeException("Invalid employeeId: " + employeeId);
    }

    return new Compensation(employee, data.getSalaryInCents(), data.getEffectiveDate());
  }

  @Override
  public Compensation createCompensation(String id, long salaryInCents,
      Instant effectiveDate) {
    LOG.debug("Creating compensation for employee id [{}]", id);

    if (compensationRepository.findByEmployeeId(id) != null) {
      throw new RuntimeException(
          "Unable to create duplicate compensation record for employee " + id);
    }

    CompensationDataRecord compensationData = new CompensationDataRecord(id, salaryInCents,
        effectiveDate);
    compensationRepository.insert(compensationData);

    return getCompensationFromData(compensationData);
  }

  @Override
  public Compensation readCompensation(String id) {
    LOG.debug("Reading compensation for employee id [{}]", id);

    CompensationDataRecord compensationData = compensationRepository.findByEmployeeId(id);
    if (compensationData == null) {
      throw new RuntimeException("No compensation information for: " + id);
    }

    return getCompensationFromData(compensationData);
  }
}