package com.records.emr.services;

import com.records.emr.Exceptions.ResourceNotFoundException;
import com.records.emr.data.models.Staff;
import com.records.emr.data.repositories.StaffRepository;
import com.records.emr.dtos.requests.StaffRequest;
import com.records.emr.dtos.responses.StaffResponse;
import com.records.emr.mappers.StaffMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class StaffService {

    private final StaffRepository staffRepository;
    private final PasswordEncoder passwordEncoder;

    public StaffResponse register(StaffRequest request) {
        if (staffRepository.findByUsername(request.getUsername()).isPresent()) {
            throw new IllegalArgumentException("Username already taken: " + request.getUsername());
        }
        String hashed = passwordEncoder.encode(request.getPassword());
        Staff saved = staffRepository.save(StaffMapper.toEntity(request, hashed));
        return StaffMapper.toResponse(saved);
    }

    public List<StaffResponse> getAll() {
        return staffRepository.findAll().stream().map(StaffMapper::toResponse).toList();
    }

    public StaffResponse getById(String id) {
        Staff user = staffRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User not found: " + id));
        return StaffMapper.toResponse(user);
    }
}