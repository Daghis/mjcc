package com.mindex.challenge;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import com.mindex.challenge.data.ReportingStructure;
import com.mindex.challenge.service.impl.EmployeeServiceImpl;
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
  private EmployeeServiceImpl employeeService;

  private static final String JOHN_LENNON_ID = "16a596ae-edd3-4847-99fe-c4518e82c86f";

  @Test
  public void contextLoads() {
    assertNotNull(employeeService.read(JOHN_LENNON_ID));

    exceptionRule.expect(RuntimeException.class);
    employeeService.read("this id fails");
  }

  @Test
  public void getReportingStructure_basicValidation() {
    ReportingStructure structure = employeeService.getReportingStructure(JOHN_LENNON_ID);

    assertEquals(JOHN_LENNON_ID, structure.getEmployee().getEmployeeId());
    assertEquals(4, structure.getNumberOfReports());
  }

}
