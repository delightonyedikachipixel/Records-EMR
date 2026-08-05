package com.records.emr.dtos.responses;


import com.records.emr.data.models.enums.Role;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class StaffResponse {
    private String id;
    private String name;
    private Role role;
    private String username;
}