package com.records.emr.mappers;

import com.records.emr.data.models.Staff;
import com.records.emr.dtos.requests.StaffRequest;
import com.records.emr.dtos.responses.StaffResponse;

public class StaffMapper {

    public static Staff toEntity(StaffRequest request, String hashedPassword) {
        return Staff.builder()
                .name(request.getName())
                .role(request.getRole())
                .username(request.getUsername())
                .passwordHash(hashedPassword)
                .build();
    }

    public static StaffResponse toResponse(Staff staff) {
        return StaffResponse.builder()
                .id(staff.getId())
                .name(staff.getName())
                .role(staff.getRole())
                .username(staff.getUsername())
                .build();
    }
}
