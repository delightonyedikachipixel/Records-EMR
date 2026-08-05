package com.records.emr.mappers;

import com.records.emr.data.models.Staff;
import com.records.emr.data.models.Visit;
import com.records.emr.dtos.requests.VisitRequest;
import com.records.emr.dtos.responses.VisitResponse;

import java.time.LocalDateTime;

public class VisitMapper {

    public static Visit toEntity(VisitRequest request) {
        return Visit.builder()
                .patientId(request.getPatientId())
                .doctorId(request.getDoctorId())
                .symptoms(request.getSymptoms())
                .diagnosis(request.getDiagnosis())
                .notes(request.getNotes())
                .date(LocalDateTime.now())
                .build();
    }

    public static VisitResponse toResponse(Visit visit, Staff doctor) {
        return VisitResponse.builder()
                .id(visit.getId())
                .patientId(visit.getPatientId())
                .doctorName(doctor != null ? doctor.getName() : "Unknown")
                .date(visit.getDate())
                .symptoms(visit.getSymptoms())
                .diagnosis(visit.getDiagnosis())
                .notes(visit.getNotes())
                .build();
    }
}
