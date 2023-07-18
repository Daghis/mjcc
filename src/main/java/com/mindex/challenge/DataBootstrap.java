package com.mindex.challenge;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mindex.challenge.dao.CompensationRepository;
import com.mindex.challenge.dao.EmployeeRepository;
import com.mindex.challenge.data.CompensationDataRecord;
import com.mindex.challenge.data.Employee;
import java.time.Instant;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.io.InputStream;

@Component
public class DataBootstrap {

  private static final String DATASTORE_LOCATION = "/static/employee_database.json";

  @Autowired
  private EmployeeRepository employeeRepository;
  @Autowired
  private CompensationRepository compensationRepository;

  @Autowired
  private ObjectMapper objectMapper;

  // Test data for Compensation tests.
  private static final String JOHN_LENNON_ID = "16a596ae-edd3-4847-99fe-c4518e82c86f";
  private static final long SALARY_IN_CENTS = 123_456_00;
  private static final Instant EFFECTIVE_DATE = Instant.ofEpochSecond(1689707057);

  @PostConstruct
  public void init() {
    InputStream inputStream = this.getClass().getResourceAsStream(DATASTORE_LOCATION);

    Employee[] employees = null;

    try {
      employees = objectMapper.readValue(inputStream, Employee[].class);
    } catch (IOException e) {
      throw new RuntimeException(e);
    }

    for (Employee employee : employees) {
      employeeRepository.insert(employee);
    }

    compensationRepository.insert(new CompensationDataRecord(JOHN_LENNON_ID,
        SALARY_IN_CENTS, EFFECTIVE_DATE));
  }
}
