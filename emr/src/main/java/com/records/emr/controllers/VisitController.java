package com.records.emr.controllers;

import com.records.emr.dtos.requests.VisitRequest;
import com.records.emr.dtos.responses.VisitResponse;
import com.records.emr.services.VisitService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/visits")
@RequiredArgsConstructor
public class VisitController {

    private final VisitService visitService;

    @PreAuthorize("hasRole('DOCTOR')")
    @PostMapping
    public ResponseEntity<VisitResponse> logVisit(@Valid @RequestBody VisitRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(visitService.logVisit(request));
    }

    @GetMapping("/patient/{patientId}")
    public ResponseEntity<List<VisitResponse>> getForPatient(@PathVariable String patientId) {
        return ResponseEntity.ok(visitService.getForPatient(patientId));
    }
}
