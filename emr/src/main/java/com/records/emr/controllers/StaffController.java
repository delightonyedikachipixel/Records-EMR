package com.records.emr.controllers;

import com.records.emr.dtos.requests.StaffRequest;
import com.records.emr.dtos.responses.StaffResponse;
import com.records.emr.services.StaffService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class StaffController {

    private final StaffService staffService;

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping
    public ResponseEntity<StaffResponse> register(@Valid @RequestBody StaffRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(staffService.register(request));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping
    public ResponseEntity<List<StaffResponse>> getAll() {
        return ResponseEntity.ok(staffService.getAll());
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/{id}")
    public ResponseEntity<StaffResponse> getById(@PathVariable String id) {
        return ResponseEntity.ok(staffService.getById(id));
    }
}
