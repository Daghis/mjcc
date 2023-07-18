package com.mindex.challenge;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import com.mindex.challenge.controller.EmployeeController;
import com.mindex.challenge.dao.CompensationRepository;
import com.mindex.challenge.data.Compensation;
import com.mindex.challenge.data.CompensationDataRecord;
import com.mindex.challenge.data.Employee;
import com.mindex.challenge.data.ReportingStructure;
import java.time.Instant;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class ChallengeApplicationTests {

  @Rule
  public ExpectedException exceptionRule = ExpectedException.none();

  @Autowired
  private EmployeeController employeeController;
  @Autowired
  private CompensationRepository compensationRepository;


  private static final String JOHN_LENNON_ID = "16a596ae-edd3-4847-99fe-c4518e82c86f";
  private static final String CREATED_COMPENSATION_ID = "b7839309-3348-463b-a7e3-5de1c168beb3";
  private static final String NO_COMPENSATION_ID = "03aa1462-ffa9-4978-901b-7c001562cf6f";
  private static final String INVALID_ID = "this id fails";
  private static final long SALARY_IN_CENTS = 123_456_00;

  // These two values represent the same point in time. They must be kept in sync or tests will
  // fail.
  private static final Instant EFFECTIVE_DATE = Instant.ofEpochSecond(1468195200);
  private static final String EFFECTIVE_DATE_ISO = "2016-07-11T00:00:00Z";

  private static final String FIRST_NAME = "John";
  private static final String LAST_NAME = "Lennon";
  private static final String POSITION = "Development Manager";
  private static final String DEPARTMENT = "Engineering";
  private static final String DIRECT_REPORT_1 = "b7839309-3348-463b-a7e3-5de1c168beb3";
  private static final String DIRECT_REPORT_2 = "03aa1462-ffa9-4978-901b-7c001562cf6f";

  private void assertEqualToJohnLennon(Employee employee) {
    assertEquals(JOHN_LENNON_ID, employee.getEmployeeId());
    assertEquals(FIRST_NAME, employee.getFirstName());
    assertEquals(LAST_NAME, employee.getLastName());
    assertEquals(POSITION, employee.getPosition());
    assertEquals(DEPARTMENT, employee.getDepartment());
    assertEquals(2, employee.getDirectReports().size());
    assertEquals(DIRECT_REPORT_1, employee.getDirectReports().get(0).getEmployeeId());
    assertEquals(DIRECT_REPORT_2, employee.getDirectReports().get(1).getEmployeeId());
  }

  @Test
  public void contextLoads() {
    assertNotNull(employeeController.read(JOHN_LENNON_ID));

    exceptionRule.expect(RuntimeException.class);
    employeeController.read(INVALID_ID);
  }

  @Test
  public void getReportingStructure_basicValidation() {
    ReportingStructure structure = employeeController.getReportingStructure(JOHN_LENNON_ID);

    assertEqualToJohnLennon(structure.getEmployee());
    assertEquals(4, structure.getNumberOfReports());
  }

  @Test
  public void getReportingStructure_employeeNotFound() {
    exceptionRule.expect(RuntimeException.class);
    employeeController.getReportingStructure(INVALID_ID);
  }

  @Test
  public void getReportingStructure_noReports() {
    ReportingStructure structure = employeeController.getReportingStructure(
        CREATED_COMPENSATION_ID);

    assertEquals(CREATED_COMPENSATION_ID, structure.getEmployee().getEmployeeId());
    assertEquals(0, structure.getNumberOfReports());
  }

  @Test
  public void getReportingStructure_noId() {
    exceptionRule.expect(RuntimeException.class);
    employeeController.getReportingStructure("");
  }

  @Test
  public void createCompensation_basic() {
    long salaryInCents = 12345678;
    Compensation compensation = employeeController.createCompensation(CREATED_COMPENSATION_ID,
        salaryInCents, EFFECTIVE_DATE_ISO);

    // First, validate the returned Compensation object.
    assertEquals(CREATED_COMPENSATION_ID, compensation.getEmployee().getEmployeeId());
    assertEquals(salaryInCents, compensation.getSalaryInCents());
    assertEquals(EFFECTIVE_DATE, compensation.getEffectiveDate());

    // Now, check the data that was written to Mongo.
    CompensationDataRecord dataRecord = compensationRepository.findByEmployeeId(
        CREATED_COMPENSATION_ID);

    assertEquals(CREATED_COMPENSATION_ID, dataRecord.getEmployeeId());
    assertEquals(salaryInCents, dataRecord.getSalaryInCents());
    assertEquals(EFFECTIVE_DATE, dataRecord.getEffectiveDate());
  }

  @Test
  public void createCompensation_employeeNotFound() {
    exceptionRule.expect(RuntimeException.class);
    employeeController.createCompensation(INVALID_ID, 12345600, EFFECTIVE_DATE_ISO);
  }

  @Test
  public void readCompensation_basic() {
    Compensation compensation = employeeController.readCompensation(JOHN_LENNON_ID);

    assertEqualToJohnLennon(compensation.getEmployee());
    assertEquals(SALARY_IN_CENTS, compensation.getSalaryInCents());
    assertEquals(EFFECTIVE_DATE, compensation.getEffectiveDate());
  }

  @Test
  public void readCompensation_employeeNotFound() {
    exceptionRule.expect(RuntimeException.class);
    employeeController.readCompensation(INVALID_ID);
  }

  @Test
  public void readCompensation_employeeHasNoCompensation() {
    exceptionRule.expect(RuntimeException.class);
    employeeController.readCompensation(NO_COMPENSATION_ID);
  }
}
