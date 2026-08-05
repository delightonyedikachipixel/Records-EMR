package com.records.emr;


import com.records.emr.Exceptions.ResourceNotFoundException;
import com.records.emr.data.models.Patient;
import com.records.emr.data.repositories.PatientRepository;
import com.records.emr.dtos.requests.PatientRequest;
import com.records.emr.dtos.responses.PatientResponse;
import com.records.emr.services.PatientService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class PatientServiceTest {

    @Mock
    private PatientRepository patientRepository;

    @InjectMocks
    private PatientService patientService;

    private String patientId;
    private PatientRequest sampleRequest;
    private Patient samplePatient;

    @BeforeEach
    void setUp() {
        patientId = "patient-001";

        sampleRequest = new PatientRequest();

        samplePatient = new Patient();
        samplePatient.setId(patientId);
    }

    @Nested
    @DisplayName("Register Patient Unit Tests")
    class RegisterTests {

        @Test
        @DisplayName("Regular Test: Successfully register a new patient")
        void register_WithValidRequest_ReturnsPatientResponse() {
            when(patientRepository.save(any(Patient.class))).thenReturn(samplePatient);

            PatientResponse response = patientService.register(sampleRequest);

            assertNotNull(response);
            verify(patientRepository, times(1)).save(any(Patient.class));
        }
    }

    @Nested
    @DisplayName("Get Patient By ID Unit Tests")
    class GetByIdTests {

        @Test
        @DisplayName("Regular Test: Successfully retrieve patient by valid ID")
        void getById_WhenPatientExists_ReturnsPatientResponse() {
            when(patientRepository.findById(patientId)).thenReturn(Optional.of(samplePatient));

            PatientResponse response = patientService.getById(patientId);

            assertNotNull(response);
            verify(patientRepository, times(1)).findById(patientId);
        }

        @Test
        @DisplayName("Edge Case: Throws ResourceNotFoundException when patient ID does not exist")
        void getById_WhenPatientDoesNotExist_ThrowsResourceNotFoundException() {
            String invalidId = "invalid-id";
            when(patientRepository.findById(invalidId)).thenReturn(Optional.empty());

            ResourceNotFoundException exception = assertThrows(
                    ResourceNotFoundException.class,
                    () -> patientService.getById(invalidId)
            );

            assertEquals("Patient not found: " + invalidId, exception.getMessage());
            verify(patientRepository, times(1)).findById(invalidId);
        }
    }


    @Nested
    @DisplayName("Search Patient Unit Tests")
    class SearchTests {

        @Test
        @DisplayName("Regular Test: Returns filtered patients when a query string is provided")
        void search_WithValidQuery_ReturnsFilteredList() {
            String query = "John";
            when(patientRepository.findByNameContainingIgnoreCase(query))
                    .thenReturn(List.of(samplePatient));

            List<PatientResponse> results = patientService.search(query);

            assertNotNull(results);
            assertEquals(1, results.size());
            verify(patientRepository, times(1)).findByNameContainingIgnoreCase(query);
            verify(patientRepository, never()).findAll();
        }

        @ParameterizedTest
        @NullAndEmptySource
        @ValueSource(strings = {"   ", "\t", "\n"})
        @DisplayName("Edge Case: Null, empty, or whitespace queries fall back to findAll()")
        void search_WithNullOrBlankQuery_CallsFindAll(String blankQuery) {
            when(patientRepository.findAll()).thenReturn(List.of(samplePatient));

            List<PatientResponse> results = patientService.search(blankQuery);

            assertNotNull(results);
            assertEquals(1, results.size());
            verify(patientRepository, times(1)).findAll();
            verify(patientRepository, never()).findByNameContainingIgnoreCase(anyString());
        }

        @Test
        @DisplayName("Edge Case: Returns empty list when search term matches no patients")
        void search_WhenNoMatchesFound_ReturnsEmptyList() {
            String query = "NonExistentName";
            when(patientRepository.findByNameContainingIgnoreCase(query))
                    .thenReturn(Collections.emptyList());

            List<PatientResponse> results = patientService.search(query);

            assertNotNull(results);
            assertTrue(results.isEmpty());
            verify(patientRepository, times(1)).findByNameContainingIgnoreCase(query);
        }
    }
}