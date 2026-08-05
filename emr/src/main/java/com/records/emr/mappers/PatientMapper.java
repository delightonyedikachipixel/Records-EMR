package com.records.emr.mappers;



import com.records.emr.data.models.Patient;
import com.records.emr.dtos.requests.PatientRequest;
import com.records.emr.dtos.responses.PatientResponse;

public class PatientMapper {

    public static Patient toEntity(PatientRequest request) {
        return Patient.builder()
                .name(request.getName())
                .dateOfBirth(request.getDateOfBirth())
                .gender(request.getGender())
                .phoneNumber(request.getPhoneNumber())
                .address(request.getAddress())
                .bloodGroup(request.getBloodGroup())
                .genotype(request.getGenotype())
                .build();
    }

    public static PatientResponse toResponse(Patient patient) {
        return PatientResponse.builder()
                .id(patient.getId())
                .name(patient.getName())
                .dateOfBirth(patient.getDateOfBirth())
                .gender(patient.getGender())
                .phoneNumber(patient.getPhoneNumber())
                .address(patient.getAddress())
                .bloodGroup(patient.getBloodGroup())
                .genotype(patient.getGenotype())
                .build();
    }
}