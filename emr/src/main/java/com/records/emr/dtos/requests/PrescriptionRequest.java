package com.records.emr.dtos.requests;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class PrescriptionRequest {
    @NotNull
    private String visitId;

    @NotBlank
    private String medicineName;

    @NotBlank
    private String dosage;

    @NotBlank
    private String duration;
}
