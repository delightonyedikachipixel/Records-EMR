package com.records.emr.data.repositories;


import com.records.emr.data.models.Appointment;
import com.records.emr.data.models.enums.AppointmentStatus;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface AppointmentRepository extends MongoRepository<Appointment, String> {
    List<Appointment> findByPatientId(String patientId);
    List<Appointment> findByDoctorIdAndStatus(String doctorId, AppointmentStatus status);
}
