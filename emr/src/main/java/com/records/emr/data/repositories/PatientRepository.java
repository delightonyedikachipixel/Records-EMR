package com.records.emr.data.repositories;


import com.records.emr.data.models.Patient;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface PatientRepository extends MongoRepository<Patient, String> {
    List<Patient> findByNameContainingIgnoreCase(String name);
    List<Patient> findByPhoneNumber(String phoneNumber);
}

