package com.records.emr;

import com.records.emr.Exceptions.ResourceNotFoundException;
import com.records.emr.data.models.Patient;
import com.records.emr.data.models.Staff;
import com.records.emr.data.models.Visit;
import com.records.emr.data.repositories.PatientRepository;
import com.records.emr.data.repositories.StaffRepository;
import com.records.emr.data.repositories.VisitRepository;
import com.records.emr.dtos.requests.VisitRequest;
import com.records.emr.dtos.responses.VisitResponse;
import com.records.emr.services.VisitService;
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
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class VisitServiceTest {

    @Mock
    private VisitRepository visitRepository;

    @Mock
    private PatientRepository patientRepository;

    @Mock
    private StaffRepository staffRepository;

    @InjectMocks
    private VisitService visitService;

    private String patientId;
    private String doctorId;
    private String visitId;
    private VisitRequest validRequest;
    private Patient existingPatient;
    private Staff existingDoctor;
    private Visit existingVisit;

    @BeforeEach
    void setUp() {
        patientId = "patient-101";
        doctorId = "doctor-202";
        visitId = "visit-303";

        validRequest = new VisitRequest();
        validRequest.setPatientId(patientId);
        validRequest.setDoctorId(doctorId);

        existingPatient = new Patient();
        existingPatient.setId(patientId);

        existingDoctor = new Staff();
        existingDoctor.setId(doctorId);

        existingVisit = new Visit();
        existingVisit.setId(visitId);
        existingVisit.setPatientId(patientId);
        existingVisit.setDoctorId(doctorId);
    }


    @Nested
    @DisplayName("Log Visit Unit Tests")
    class LogVisitTests {

        @Test
        @DisplayName("Regular Test: Patient and Doctor exist, visit logs successfully")
        void logVisit_WithValidPatientAndDoctor_ReturnsVisitResponse() {
            when(patientRepository.findById(patientId)).thenReturn(Optional.of(existingPatient));
            when(staffRepository.findById(doctorId)).thenReturn(Optional.of(existingDoctor));
            when(visitRepository.save(any(Visit.class))).thenReturn(existingVisit);

            VisitResponse response = visitService.logVisit(validRequest);

            assertNotNull(response);
            verify(patientRepository, times(1)).findById(patientId);
            verify(staffRepository, times(1)).findById(doctorId);
            verify(visitRepository, times(1)).save(any(Visit.class));
        }

        @Test
        @DisplayName("Edge Case: Throws ResourceNotFoundException when Patient ID is invalid")
        void logVisit_WhenPatientDoesNotExist_ThrowsResourceNotFoundException() {
            when(patientRepository.findById(patientId)).thenReturn(Optional.empty());

            ResourceNotFoundException exception = assertThrows(
                    ResourceNotFoundException.class,
                    () -> visitService.logVisit(validRequest)
            );

            assertEquals("Patient not found: " + patientId, exception.getMessage());
            verify(patientRepository, times(1)).findById(patientId);
            verify(staffRepository, never()).findById(anyString());
            verify(visitRepository, never()).save(any(Visit.class));
        }

        @Test
        @DisplayName("Edge Case: Throws ResourceNotFoundException when Doctor ID is invalid")
        void logVisit_WhenDoctorDoesNotExist_ThrowsResourceNotFoundException() {
            when(patientRepository.findById(patientId)).thenReturn(Optional.of(existingPatient));
            when(staffRepository.findById(doctorId)).thenReturn(Optional.empty());

            ResourceNotFoundException exception = assertThrows(
                    ResourceNotFoundException.class,
                    () -> visitService.logVisit(validRequest)
            );

            assertEquals("Doctor not found: " + doctorId, exception.getMessage());
            verify(patientRepository, times(1)).findById(patientId);
            verify(staffRepository, times(1)).findById(doctorId);
            verify(visitRepository, never()).save(any(Visit.class));
        }
    }


    @Nested
    @DisplayName("Get Visits For Patient Unit Tests")
    class GetForPatientTests {

        @Test
        @DisplayName("Regular Test: Returns list of visits mapped with doctor details")
        void getForPatient_WhenVisitsExist_ReturnsVisitResponseList() {
            when(visitRepository.findByPatientIdOrderByDateDesc(patientId))
                    .thenReturn(List.of(existingVisit));
            when(staffRepository.findById(doctorId))
                    .thenReturn(Optional.of(existingDoctor));

            List<VisitResponse> responses = visitService.getForPatient(patientId);

            assertNotNull(responses);
            assertEquals(1, responses.size());
            verify(visitRepository, times(1)).findByPatientIdOrderByDateDesc(patientId);
            verify(staffRepository, times(1)).findById(doctorId);
        }

        @Test
        @DisplayName("Edge Case: Returns empty list when patient has no visit records")
        void getForPatient_WhenNoVisitsExist_ReturnsEmptyList() {
            when(visitRepository.findByPatientIdOrderByDateDesc(patientId))
                    .thenReturn(Collections.emptyList());

            List<VisitResponse> responses = visitService.getForPatient(patientId);

            assertNotNull(responses);
            assertTrue(responses.isEmpty());
            verify(visitRepository, times(1)).findByPatientIdOrderByDateDesc(patientId);
            verify(staffRepository, never()).findById(anyString());
        }

        @Test
        @DisplayName("Edge Case: Handles missing/deleted Doctor gracefully by supplying null")
        void getForPatient_WhenDoctorNoLongerExists_MapsWithNullDoctor() {
            when(visitRepository.findByPatientIdOrderByDateDesc(patientId))
                    .thenReturn(List.of(existingVisit));
            when(staffRepository.findById(doctorId))
                    .thenReturn(Optional.empty());

            List<VisitResponse> responses = visitService.getForPatient(patientId);


            assertNotNull(responses);
            assertEquals(1, responses.size());
            verify(visitRepository, times(1)).findByPatientIdOrderByDateDesc(patientId);
            verify(staffRepository, times(1)).findById(doctorId);
        }
    }
}