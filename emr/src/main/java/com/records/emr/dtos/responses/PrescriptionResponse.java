package com.records.emr.dtos.responses;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PrescriptionResponse {
    private String id;
    private String visitId;
    private String medicineName;
    private String dosage;
    private String duration;
}
