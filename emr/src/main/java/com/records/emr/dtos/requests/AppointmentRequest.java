package com.records.emr.dtos.requests;

import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class AppointmentRequest {
    @NotNull
    private String patientId;

    @NotNull
    private String doctorId;

    @NotNull
    @Future(message = "Appointment must be scheduled in the future")
    private LocalDateTime scheduledAt;
}
