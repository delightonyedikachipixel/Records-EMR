package com.records.emr.dtos.responses;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class VisitResponse {
    private String id;
    private String patientId;
    private String doctorName;
    private LocalDateTime date;
    private String symptoms;
    private String diagnosis;
    private String notes;
}