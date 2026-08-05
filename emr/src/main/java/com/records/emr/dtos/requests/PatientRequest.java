package com.records.emr.dtos.requests;


import com.records.emr.data.models.enums.BloodGroup;
import com.records.emr.data.models.enums.Genotype;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

    @Data
    public class PatientRequest {
        @NotBlank(message = "Name is required")
        private String name;

        @NotBlank(message = "Date of birth is required")
        private String dateOfBirth;

        @NotBlank
        private String gender;

        @NotBlank
        @Pattern(regexp = "^0[0-9]{10}$", message = "Phone must be an 11-digit number starting with 0")
        private String phoneNumber;

        private String address;
        private BloodGroup bloodGroup;
        private Genotype genotype;
    }

