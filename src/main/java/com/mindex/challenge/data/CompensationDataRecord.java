package com.mindex.challenge.data;

import java.time.Instant;

public class CompensationDataRecord {

  private String employeeId;
  private long salaryInCents;
  private Instant effectiveDate;

  public CompensationDataRecord() {

  }

  public CompensationDataRecord(String employeeId, long salaryInCents, Instant effectiveDate) {
    this.employeeId = employeeId;
    this.salaryInCents = salaryInCents;
    this.effectiveDate = effectiveDate;
  }

  public String getEmployeeId() {
    return employeeId;
  }

  public void setEmployeeId(String employeeId) {
    this.employeeId = employeeId;
  }

  public long getSalaryInCents() {
    return salaryInCents;
  }

  public void setSalaryInCents(long salaryInCents) {
    this.salaryInCents = salaryInCents;
  }

  public Instant getEffectiveDate() {
    return effectiveDate;
  }

  public void setEffectiveDate(Instant effectiveDate) {
    this.effectiveDate = effectiveDate;
  }
}
