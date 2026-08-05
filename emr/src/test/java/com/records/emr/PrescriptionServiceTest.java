package com.records.emr;


import com.records.emr.Exceptions.ResourceNotFoundException;
import com.records.emr.data.models.Prescription;
import com.records.emr.data.models.Visit;
import com.records.emr.data.repositories.PrescriptionRepository;
import com.records.emr.data.repositories.VisitRepository;
import com.records.emr.dtos.requests.PrescriptionRequest;
import com.records.emr.dtos.responses.PrescriptionResponse;
import com.records.emr.services.PrescriptionService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
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
    public class PrescriptionServiceTest {

        @Mock
        private PrescriptionRepository prescriptionRepository;

        @Mock
        private VisitRepository visitRepository;

        @InjectMocks
        private PrescriptionService prescriptionService;

        private String visitId;
        private String prescriptionId;
        private PrescriptionRequest validRequest;
        private Visit existingVisit;
        private Prescription existingPrescription;

        @BeforeEach
        void setUp() {
            visitId = "visit-101";
            prescriptionId = "rx-505";

            validRequest = new PrescriptionRequest();
            validRequest.setVisitId(visitId);

            existingVisit = new Visit();
            existingVisit.setId(visitId);

            existingPrescription = new Prescription();
            existingPrescription.setId(prescriptionId);
            existingPrescription.setVisitId(visitId);
        }


        @Nested
        @DisplayName("Add Prescription Unit Tests")
        class AddPrescriptionTests {

            @Test
            @DisplayName("Regular Test: Successfully add prescription to an existing visit")
            void addPrescription_WithValidVisitId_ReturnsPrescriptionResponse() {
                when(visitRepository.findById(visitId)).thenReturn(Optional.of(existingVisit));
                when(prescriptionRepository.save(any(Prescription.class))).thenReturn(existingPrescription);

                PrescriptionResponse response = prescriptionService.addPrescription(validRequest);

                assertNotNull(response);
                verify(visitRepository, times(1)).findById(visitId);
                verify(prescriptionRepository, times(1)).save(any(Prescription.class));
            }

            @Test
            @DisplayName("Edge Case: Throws ResourceNotFoundException when Visit ID does not exist")
            void addPrescription_WhenVisitDoesNotExist_ThrowsResourceNotFoundException() {
                when(visitRepository.findById(visitId)).thenReturn(Optional.empty());

                ResourceNotFoundException exception = assertThrows(
                        ResourceNotFoundException.class,
                        () -> prescriptionService.addPrescription(validRequest)
                );

                assertEquals("Visit not found: " + visitId, exception.getMessage());
                verify(visitRepository, times(1)).findById(visitId);
                verify(prescriptionRepository, never()).save(any(Prescription.class));
            }
        }


        @Nested
        @DisplayName("Get Prescriptions For Visit Unit Tests")
        class GetForVisitTests {

            @Test
            @DisplayName("Regular Test: Returns list of prescriptions for a valid visit ID")
            void getForVisit_WhenPrescriptionsExist_ReturnsPrescriptionList() {
                when(prescriptionRepository.findByVisitId(visitId))
                        .thenReturn(List.of(existingPrescription));

                List<PrescriptionResponse> results = prescriptionService.getForVisit(visitId);

                assertNotNull(results);
                assertEquals(1, results.size());
                verify(prescriptionRepository, times(1)).findByVisitId(visitId);
            }

            @Test
            @DisplayName("Edge Case: Returns empty list when visit has no prescriptions")
            void getForVisit_WhenNoPrescriptionsExist_ReturnsEmptyList() {
                when(prescriptionRepository.findByVisitId(visitId))
                        .thenReturn(Collections.emptyList());

                List<PrescriptionResponse> results = prescriptionService.getForVisit(visitId);

                assertNotNull(results);
                assertTrue(results.isEmpty());
                verify(prescriptionRepository, times(1)).findByVisitId(visitId);
            }

            @Test
            @DisplayName("Edge Case: Handles null/unknown visitId parameter gracefully")
            void getForVisit_WithNullVisitId_ReturnsEmptyList() {
                when(prescriptionRepository.findByVisitId(null))
                        .thenReturn(Collections.emptyList());

                List<PrescriptionResponse> results = prescriptionService.getForVisit(null);

                assertNotNull(results);
                assertTrue(results.isEmpty());
                verify(prescriptionRepository, times(1)).findByVisitId(null);
            }
        }
    }

