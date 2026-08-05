package com.records.emr.data.models;

import com.records.emr.data.models.enums.AppointmentStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Document(collection = "appointments")
public class Appointment {
    @Id
    private String id;
    private String patientId;
    private String doctorId;
    private LocalDateTime scheduledAt;
    private AppointmentStatus status;
}
