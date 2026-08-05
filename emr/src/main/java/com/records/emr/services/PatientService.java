package com.records.emr.services;

import com.records.emr.Exceptions.ResourceNotFoundException;
import com.records.emr.data.models.Patient;
import com.records.emr.data.repositories.PatientRepository;
import com.records.emr.dtos.requests.PatientRequest;
import com.records.emr.dtos.responses.PatientResponse;
import com.records.emr.mappers.PatientMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PatientService {

    private final PatientRepository patientRepository;

    public PatientResponse register(PatientRequest request) {
        Patient saved = patientRepository.save(PatientMapper.toEntity(request));
        return PatientMapper.toResponse(saved);
    }

    public PatientResponse getById(String id) {
        Patient patient = patientRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Patient not found: " + id));
        return PatientMapper.toResponse(patient);
    }

    public List<PatientResponse> search(String query) {
        List<Patient> results = (query == null || query.isBlank())
                ? patientRepository.findAll()
                : patientRepository.findByNameContainingIgnoreCase(query);
        return results.stream().map(PatientMapper::toResponse).toList();
    }
}