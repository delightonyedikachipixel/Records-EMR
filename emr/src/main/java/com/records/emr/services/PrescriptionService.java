package com.records.emr.services;

import com.records.emr.Exceptions.ResourceNotFoundException;
import com.records.emr.data.models.Prescription;
import com.records.emr.data.repositories.PrescriptionRepository;
import com.records.emr.data.repositories.VisitRepository;
import com.records.emr.dtos.requests.PrescriptionRequest;
import com.records.emr.dtos.responses.PrescriptionResponse;
import com.records.emr.mappers.PrescriptionMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PrescriptionService {

    private final PrescriptionRepository prescriptionRepository;
    private final VisitRepository visitRepository;

    public PrescriptionResponse addPrescription(PrescriptionRequest request) {
        visitRepository.findById(request.getVisitId())
                .orElseThrow(() -> new ResourceNotFoundException("Visit not found: " + request.getVisitId()));

        Prescription saved = prescriptionRepository.save(PrescriptionMapper.toEntity(request));
        return PrescriptionMapper.toResponse(saved);
    }

    public List<PrescriptionResponse> getForVisit(String visitId) {
        return prescriptionRepository.findByVisitId(visitId).stream()
                .map(PrescriptionMapper::toResponse)
                .toList();
    }
}