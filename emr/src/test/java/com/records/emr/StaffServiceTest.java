package com.records.emr;


import com.records.emr.Exceptions.ResourceNotFoundException;
import com.records.emr.data.models.Staff;
import com.records.emr.data.repositories.StaffRepository;
import com.records.emr.dtos.requests.StaffRequest;
import com.records.emr.dtos.responses.StaffResponse;
import com.records.emr.services.StaffService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class StaffServiceTest {

    @Mock
    private StaffRepository staffRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private StaffService staffService;

    private String staffId;
    private String rawPassword;
    private String hashedPassword;
    private String username;
    private StaffRequest validRequest;
    private Staff sampleStaff;

    @BeforeEach
    void setUp() {
        staffId = "staff-101";
        username = "dr_smith";
        rawPassword = "plainPassword123";
        hashedPassword = "$2a$10$encodedHashStringHere";

        validRequest = new StaffRequest();
        validRequest.setUsername(username);
        validRequest.setPassword(rawPassword);

        sampleStaff = new Staff();
        sampleStaff.setId(staffId);
        sampleStaff.setUsername(username);
        sampleStaff.setPassword(hashedPassword);
    }


    @Nested
    @DisplayName("Register Staff Unit Tests")
    class RegisterTests {

        @Test
        @DisplayName("Regular Test: Successfully register a new staff member with hashed password")
        void register_WithUniqueUsername_EncodesPasswordAndSavesStaff() {
            when(staffRepository.findByUsername(username)).thenReturn(Optional.empty());
            when(passwordEncoder.encode(rawPassword)).thenReturn(hashedPassword);
            when(staffRepository.save(any(Staff.class))).thenReturn(sampleStaff);

            StaffResponse response = staffService.register(validRequest);

            assertNotNull(response);
            verify(staffRepository, times(1)).findByUsername(username);
            verify(passwordEncoder, times(1)).encode(rawPassword);
            verify(staffRepository, times(1)).save(any(Staff.class));
        }

        @Test
        @DisplayName("Edge Case: Throws IllegalArgumentException when username is already taken")
        void register_WhenUsernameAlreadyExists_ThrowsIllegalArgumentException() {
            when(staffRepository.findByUsername(username)).thenReturn(Optional.of(sampleStaff));

            IllegalArgumentException exception = assertThrows(
                    IllegalArgumentException.class,
                    () -> staffService.register(validRequest)
            );

            assertEquals("Username already taken: " + username, exception.getMessage());
            verify(passwordEncoder, never()).encode(anyString());
            verify(staffRepository, never()).save(any(Staff.class));
        }
    }
    @Nested
    @DisplayName("Get All Staff Unit Tests")
    class GetAllTests {

        @Test
        @DisplayName("Regular Test: Returns list of all registered staff members")
        void getAll_WhenStaffExist_ReturnsStaffResponseList() {
            when(staffRepository.findAll()).thenReturn(List.of(sampleStaff));

            List<StaffResponse> responseList = staffService.getAll();

            assertNotNull(responseList);
            assertEquals(1, responseList.size());
            verify(staffRepository, times(1)).findAll();
        }

        @Test
        @DisplayName("Edge Case: Returns empty list when no staff members exist")
        void getAll_WhenNoStaffExist_ReturnsEmptyList() {
            when(staffRepository.findAll()).thenReturn(Collections.emptyList());

            List<StaffResponse> responseList = staffService.getAll();

            assertNotNull(responseList);
            assertTrue(responseList.isEmpty());
            verify(staffRepository, times(1)).findAll();
        }
    }

    @Nested
    @DisplayName("Get Staff By ID Unit Tests")
    class GetByIdTests {

        @Test
        @DisplayName("Regular Test: Successfully retrieves staff member by valid ID")
        void getById_WhenStaffExists_ReturnsStaffResponse() {
            when(staffRepository.findById(staffId)).thenReturn(Optional.of(sampleStaff));

            StaffResponse response = staffService.getById(staffId);

            assertNotNull(response);
            verify(staffRepository, times(1)).findById(staffId);
        }

        @Test
        @DisplayName("Edge Case: Throws ResourceNotFoundException when staff ID does not exist")
        void getById_WhenStaffDoesNotExist_ThrowsResourceNotFoundException() {
            String invalidId = "invalid-staff-id";
            when(staffRepository.findById(invalidId)).thenReturn(Optional.empty());

            ResourceNotFoundException exception = assertThrows(
                    ResourceNotFoundException.class,
                    () -> staffService.getById(invalidId)
            );

            assertEquals("User not found: " + invalidId, exception.getMessage());
            verify(staffRepository, times(1)).findById(invalidId);
        }
    }
}