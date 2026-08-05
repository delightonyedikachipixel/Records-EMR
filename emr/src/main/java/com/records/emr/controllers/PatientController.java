package com.records.emr.controllers;

import com.records.emr.dtos.requests.PatientRequest;
import com.records.emr.dtos.responses.PatientResponse;
import com.records.emr.services.PatientService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/patients")
@RequiredArgsConstructor
public class PatientController {

    private final PatientService patientService;

    @PreAuthorize("hasAnyRole('FRONT_DESK', 'ADMIN')")
    @PostMapping
    public ResponseEntity<PatientResponse> register(@Valid @RequestBody PatientRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(patientService.register(request));
    }

    @GetMapping("/{id}")
    public ResponseEntity<PatientResponse> getById(@PathVariable String id) {
        return ResponseEntity.ok(patientService.getById(id));
    }

    @GetMapping
    public ResponseEntity<List<PatientResponse>> search(@RequestParam(required = false) String q) {
        return ResponseEntity.ok(patientService.search(q));
    }
}
