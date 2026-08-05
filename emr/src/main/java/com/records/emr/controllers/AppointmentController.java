package com.records.emr.controllers;

import com.records.emr.dtos.requests.AppointmentRequest;
import com.records.emr.dtos.responses.AppointmentResponse;
import com.records.emr.services.AppointmentService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/appointments")
@RequiredArgsConstructor
public class AppointmentController {

    private final AppointmentService appointmentService;

    @PreAuthorize("hasAnyRole('FRONT_DESK', 'DOCTOR')")
    @PostMapping
    public ResponseEntity<AppointmentResponse> schedule(@Valid @RequestBody AppointmentRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(appointmentService.schedule(request));
    }

    @GetMapping("/patient/{patientId}")
    public ResponseEntity<List<AppointmentResponse>> getForPatient(@PathVariable String patientId) {
        return ResponseEntity.ok(appointmentService.getForPatient(patientId));
    }

    @PreAuthorize("hasAnyRole('FRONT_DESK', 'DOCTOR', 'ADMIN')")
    @PatchMapping("/{id}/cancel")
    public ResponseEntity<AppointmentResponse> cancel(@PathVariable String id) {
        return ResponseEntity.ok(appointmentService.cancel(id));
    }
}
