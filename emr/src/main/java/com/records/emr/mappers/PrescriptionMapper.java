package com.records.emr.mappers;

import com.records.emr.data.models.Prescription;
import com.records.emr.dtos.requests.PrescriptionRequest;
import com.records.emr.dtos.responses.PrescriptionResponse;

public class PrescriptionMapper {

    public static Prescription toEntity(PrescriptionRequest request) {
        return Prescription.builder()
                .visitId(request.getVisitId())
                .medicineName(request.getMedicineName())
                .dosage(request.getDosage())
                .duration(request.getDuration())
                .build();
    }

    public static PrescriptionResponse toResponse(Prescription prescription) {
        return PrescriptionResponse.builder()
                .id(prescription.getId())
                .visitId(prescription.getVisitId())
                .medicineName(prescription.getMedicineName())
                .dosage(prescription.getDosage())
                .duration(prescription.getDuration())
                .build();
    }
}
