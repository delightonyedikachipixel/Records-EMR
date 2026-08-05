package com.records.emr.dtos.responses;


import com.records.emr.data.models.enums.BloodGroup;
import com.records.emr.data.models.enums.Genotype;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PatientResponse {
    private String id;
    private String name;
    private String dateOfBirth;
    private String gender;
    private String phoneNumber;
    private String address;
    private BloodGroup bloodGroup;
    private Genotype genotype;
}