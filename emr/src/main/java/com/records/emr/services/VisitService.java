package com.records.emr.services;

import com.records.emr.Exceptions.ResourceNotFoundException;
import com.records.emr.data.models.Staff;
import com.records.emr.data.models.Visit;
import com.records.emr.data.repositories.PatientRepository;
import com.records.emr.data.repositories.StaffRepository;
import com.records.emr.data.repositories.VisitRepository;
import com.records.emr.dtos.requests.VisitRequest;
import com.records.emr.dtos.responses.VisitResponse;
import com.records.emr.mappers.VisitMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class VisitService {

    private final VisitRepository visitRepository;
    private final PatientRepository patientRepository;
    private final StaffRepository staffRepository;

    public VisitResponse logVisit(VisitRequest request) {
        patientRepository.findById(request.getPatientId())
                .orElseThrow(() -> new ResourceNotFoundException("Patient not found: " + request.getPatientId()));
        Staff doctor = staffRepository.findById(request.getDoctorId())
                .orElseThrow(() -> new ResourceNotFoundException("Doctor not found: " + request.getDoctorId()));

        Visit saved = visitRepository.save(VisitMapper.toEntity(request));
        return VisitMapper.toResponse(saved, doctor);
    }

    public List<VisitResponse> getForPatient(String patientId) {
        return visitRepository.findByPatientIdOrderByDateDesc(patientId).stream()
                .map(v -> VisitMapper.toResponse(v, staffRepository.findById(v.getDoctorId()).orElse(null)))
                .toList();
    }
}
