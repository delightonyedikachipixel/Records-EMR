package com.records.emr.controllers;

import com.records.emr.dtos.requests.PrescriptionRequest;
import com.records.emr.dtos.responses.PrescriptionResponse;
import com.records.emr.services.PrescriptionService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/prescriptions")
@RequiredArgsConstructor
public class PrescriptionController {

    private final PrescriptionService prescriptionService;

    @PreAuthorize("hasRole('DOCTOR')")
    @PostMapping
    public ResponseEntity<PrescriptionResponse> add(@Valid @RequestBody PrescriptionRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(prescriptionService.addPrescription(request));
    }

    @GetMapping("/visit/{visitId}")
    public ResponseEntity<List<PrescriptionResponse>> getForVisit(@PathVariable String visitId) {
        return ResponseEntity.ok(prescriptionService.getForVisit(visitId));
    }
}
