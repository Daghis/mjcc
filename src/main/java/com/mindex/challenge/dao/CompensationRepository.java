package com.mindex.challenge.dao;

import com.mindex.challenge.data.CompensationDataRecord;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface CompensationRepository extends MongoRepository<CompensationDataRecord, String> {

  CompensationDataRecord findByEmployeeId(String employeeId);
}
