package com.records.emr.data.repositories;

import com.records.emr.data.models.Staff;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface StaffRepository extends MongoRepository<Staff, String> {
    Optional<Staff> findByUsername(String username);
}
