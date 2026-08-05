package com.records.emr.services;

import com.records.emr.Exceptions.ResourceNotFoundException;
import com.records.emr.data.models.Appointment;
import com.records.emr.data.models.Staff;
import com.records.emr.data.models.enums.AppointmentStatus;
import com.records.emr.data.repositories.AppointmentRepository;
import com.records.emr.data.repositories.PatientRepository;
import com.records.emr.data.repositories.StaffRepository;
import com.records.emr.dtos.requests.AppointmentRequest;
import com.records.emr.dtos.responses.AppointmentResponse;
import com.records.emr.mappers.AppointmentMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AppointmentService {

    private final AppointmentRepository appointmentRepository;
    private final PatientRepository patientRepository;
    private final StaffRepository staffRepository;

    public AppointmentResponse schedule(AppointmentRequest request) {
        patientRepository.findById(request.getPatientId())
                .orElseThrow(() -> new ResourceNotFoundException("Patient not found: " + request.getPatientId()));
        Staff doctor = staffRepository.findById(request.getDoctorId())
                .orElseThrow(() -> new ResourceNotFoundException("Doctor not found: " + request.getDoctorId()));

        Appointment saved = appointmentRepository.save(AppointmentMapper.toEntity(request));
        return AppointmentMapper.toResponse(saved, doctor);
    }

    public List<AppointmentResponse> getForPatient(String patientId) {
        return appointmentRepository.findByPatientId(patientId).stream()
                .map(a -> AppointmentMapper.toResponse(a, staffRepository.findById(a.getDoctorId()).orElse(null)))
                .toList();
    }

    public AppointmentResponse cancel(String id) {
        Appointment appointment = appointmentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Appointment not found: " + id));
        appointment.setStatus(AppointmentStatus.CANCELLED);
        Appointment saved = appointmentRepository.save(appointment);
        return AppointmentMapper.toResponse(saved, staffRepository.findById(saved.getDoctorId()).orElse(null));
    }
}
