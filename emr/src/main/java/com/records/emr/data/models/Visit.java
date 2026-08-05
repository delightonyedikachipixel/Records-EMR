package com.records.emr.data.models;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "visits")
public class Visit {
    @Id
    private String id;
    private String patientId;
    private LocalDateTime date;
    private String doctorId;
    private String symptoms;
    private String diagnosis;
    private String notes;
}
