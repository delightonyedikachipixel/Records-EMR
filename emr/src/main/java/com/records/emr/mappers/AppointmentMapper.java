package com.records.emr.mappers;

import com.records.emr.data.models.Appointment;
import com.records.emr.data.models.Staff;
import com.records.emr.data.models.enums.AppointmentStatus;
import com.records.emr.dtos.requests.AppointmentRequest;
import com.records.emr.dtos.responses.AppointmentResponse;

public class AppointmentMapper {

    public static Appointment toEntity(AppointmentRequest request) {
        return Appointment.builder()
                .patientId(request.getPatientId())
                .doctorId(request.getDoctorId())
                .scheduledAt(request.getScheduledAt())
                .status(AppointmentStatus.SCHEDULED)
                .build();
    }

    public static AppointmentResponse toResponse(Appointment appointment, Staff doctor) {
        return AppointmentResponse.builder()
                .id(appointment.getId())
                .patientId(appointment.getPatientId())
                .doctorName(doctor != null ? doctor.getName() : "Unknown")
                .scheduledAt(appointment.getScheduledAt())
                .status(appointment.getStatus())
                .build();
    }
}
