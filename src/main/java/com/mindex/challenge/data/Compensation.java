package com.mindex.challenge.data;

import java.time.Instant;

public class Compensation {

  private Employee employee;
  private long salaryInCents;
  private Instant effectiveDate;

  public Compensation() {

  }

  public Compensation(Employee employee, long salaryInCents, Instant effectiveDate) {
    this.employee = employee;
    this.salaryInCents = salaryInCents;
    this.effectiveDate = effectiveDate;
  }

  public Employee getEmployee() {
    return employee;
  }

  public void setEmployee(Employee employee) {
    this.employee = employee;
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
