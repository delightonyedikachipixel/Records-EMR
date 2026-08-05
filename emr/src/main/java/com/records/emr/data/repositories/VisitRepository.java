package com.records.emr.data.repositories;

import com.records.emr.data.models.Visit;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface VisitRepository extends MongoRepository<Visit, String> {
    List<Visit> findByPatientIdOrderByDateDesc(String patientId);
    List<Visit> findByDoctorId(String doctorId);
}