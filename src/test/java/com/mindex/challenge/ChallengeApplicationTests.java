package com.mindex.challenge;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import com.mindex.challenge.controller.EmployeeController;
import com.mindex.challenge.data.ReportingStructure;
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

  private static final String JOHN_LENNON_ID = "16a596ae-edd3-4847-99fe-c4518e82c86f";
  private static final String PAUL_MCCARTNEY_ID = "b7839309-3348-463b-a7e3-5de1c168beb3";
  private static final String INVALID_ID = "this id fails";

  @Test
  public void contextLoads() {
    assertNotNull(employeeController.read(JOHN_LENNON_ID));

    exceptionRule.expect(RuntimeException.class);
    employeeController.read(INVALID_ID);
  }

  @Test
  public void getReportingStructure_basicValidation() {
    ReportingStructure structure = employeeController.getReportingStructure(JOHN_LENNON_ID);

    assertEquals(JOHN_LENNON_ID, structure.getEmployee().getEmployeeId());
    assertEquals(4, structure.getNumberOfReports());
  }

  @Test
  public void getReportingStructure_employeeNotFound() {
    exceptionRule.expect(RuntimeException.class);
    employeeController.getReportingStructure(INVALID_ID);
  }

  @Test
  public void getReportingStructure_noReports() {
    ReportingStructure structure = employeeController.getReportingStructure(PAUL_MCCARTNEY_ID);

    assertEquals(PAUL_MCCARTNEY_ID, structure.getEmployee().getEmployeeId());
    assertEquals(0, structure.getNumberOfReports());
  }

  @Test
  public void getReportingStructure_noId() {
    exceptionRule.expect(RuntimeException.class);
    employeeController.getReportingStructure("");
  }
}
