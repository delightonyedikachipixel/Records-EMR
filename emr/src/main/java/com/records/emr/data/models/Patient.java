package com.records.emr.data.models;

import com.records.emr.data.models.enums.BloodGroup;
import com.records.emr.data.models.enums.Genotype;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "patients")
public class Patient {
    @Id
    private String id;
    private String name;
    private String dateOfBirth;
    private String gender;
    private String phoneNumber;
    private String address;
    private BloodGroup bloodGroup;
    private Genotype genotype;
}
