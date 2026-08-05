package com.records.emr.dtos.requests;



import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class VisitRequest {
    @NotNull
    private String patientId;

    @NotNull
    private String doctorId;

    @NotBlank
    private String symptoms;

    @NotBlank
    private String diagnosis;

    private String notes;
}