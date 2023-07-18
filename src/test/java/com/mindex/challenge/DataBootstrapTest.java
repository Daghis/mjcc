package com.mindex.challenge;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import com.mindex.challenge.dao.CompensationRepository;
import com.mindex.challenge.dao.EmployeeRepository;
import com.mindex.challenge.data.CompensationDataRecord;
import com.mindex.challenge.data.Employee;
import java.time.Instant;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class DataBootstrapTest {

  @Autowired
  private EmployeeRepository employeeRepository;
  @Autowired
  private CompensationRepository compensationRepository;

  private static final long SALARY_IN_CENTS = 123_456_00;
  private static final Instant EFFECTIVE_DATE = Instant.ofEpochSecond(1468195200);
  @Test
  public void test() {
    Employee employee = employeeRepository.findByEmployeeId("16a596ae-edd3-4847-99fe-c4518e82c86f");
    assertNotNull(employee);
    assertEquals("John", employee.getFirstName());
    assertEquals("Lennon", employee.getLastName());
    assertEquals("Development Manager", employee.getPosition());
    assertEquals("Engineering", employee.getDepartment());
  }

  @Test
  public void testCompensation() {
    CompensationDataRecord dataRecord = compensationRepository.findByEmployeeId(
        "16a596ae-edd3-4847-99fe-c4518e82c86f");
    assertNotNull(dataRecord);
    assertEquals("16a596ae-edd3-4847-99fe-c4518e82c86f", dataRecord.getEmployeeId());
    assertEquals(SALARY_IN_CENTS, dataRecord.getSalaryInCents());
    assertEquals(EFFECTIVE_DATE, dataRecord.getEffectiveDate());
  }
}