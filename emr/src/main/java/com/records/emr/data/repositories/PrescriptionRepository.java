package com.records.emr.data.repositories;


import com.records.emr.data.models.Prescription;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

    public interface PrescriptionRepository extends MongoRepository<Prescription, String> {
        List<Prescription> findByVisitId(String visitId);
    }
