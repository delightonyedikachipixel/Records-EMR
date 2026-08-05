package com.records.emr.dtos.responses;

import com.records.emr.data.models.enums.AppointmentStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AppointmentResponse {
    private String id;
    private String patientId;
    private String doctorName;
    private LocalDateTime scheduledAt;
    private AppointmentStatus status;
}
