package com.records.emr;


import com.records.emr.Exceptions.ResourceNotFoundException;
import com.records.emr.data.models.Appointment;
import com.records.emr.data.models.Patient;
import com.records.emr.data.models.Staff;
import com.records.emr.data.models.enums.AppointmentStatus;
import com.records.emr.data.repositories.AppointmentRepository;
import com.records.emr.data.repositories.PatientRepository;
import com.records.emr.data.repositories.StaffRepository;
import com.records.emr.dtos.requests.AppointmentRequest;
import com.records.emr.dtos.responses.AppointmentResponse;
import com.records.emr.services.AppointmentService;
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
public class AppointmentServiceTest {

    @Mock
    private AppointmentRepository appointmentRepository;

    @Mock
    private PatientRepository patientRepository;

    @Mock
    private StaffRepository staffRepository;

    @InjectMocks
    private AppointmentService appointmentService;

    private String patientId;
    private String doctorId;
    private String appointmentId;
    private AppointmentRequest validRequest;
    private Patient existingPatient;
    private Staff existingDoctor;
    private Appointment existingAppointment;

    @BeforeEach
    void setUp() {
        patientId = "patient-001";
        doctorId = "doctor-001";
        appointmentId = "appt-001";

        validRequest = new AppointmentRequest();
        validRequest.setPatientId(patientId);
        validRequest.setDoctorId(doctorId);

        existingPatient = new Patient();
        existingDoctor = new Staff();
        existingDoctor.setId(doctorId);

        existingAppointment = new Appointment();
        existingAppointment.setId(appointmentId);
        existingAppointment.setPatientId(patientId);
        existingAppointment.setDoctorId(doctorId);
        existingAppointment.setStatus(AppointmentStatus.PENDING);
    }

    @Nested
    @DisplayName("Schedule Appointment Unit Tests")
    class ScheduleTests {

        @Test
        @DisplayName("Regular Test: Patient can successfully book an appointment")
        void testThatPatientCanBookAppointmentService() {
            when(patientRepository.findById(patientId)).thenReturn(Optional.of(existingPatient));
            when(staffRepository.findById(doctorId)).thenReturn(Optional.of(existingDoctor));
            when(appointmentRepository.save(any(Appointment.class))).thenReturn(existingAppointment);


            AppointmentResponse response = appointmentService.schedule(validRequest);

            assertNotNull(response);
            verify(patientRepository, times(1)).findById(patientId);
            verify(staffRepository, times(1)).findById(doctorId);
            verify(appointmentRepository, times(1)).save(any(Appointment.class));
        }

        @Test
        @DisplayName("Edge Case: Throws ResourceNotFoundException when Patient ID is invalid")
        void schedule_WhenPatientDoesNotExist_ThrowsResourceNotFoundException() {
            when(patientRepository.findById(patientId)).thenReturn(Optional.empty());

            ResourceNotFoundException exception = assertThrows(
                    ResourceNotFoundException.class,
                    () -> appointmentService.schedule(validRequest)
            );

            assertEquals("Patient not found: " + patientId, exception.getMessage());
            verify(staffRepository, never()).findById(anyString());
            verify(appointmentRepository, never()).save(any(Appointment.class));
        }

        @Test
        @DisplayName("Edge Case: Throws ResourceNotFoundException when Doctor ID is invalid")
        void schedule_WhenDoctorDoesNotExist_ThrowsResourceNotFoundException() {
            when(patientRepository.findById(patientId)).thenReturn(Optional.of(existingPatient));
            when(staffRepository.findById(doctorId)).thenReturn(Optional.empty());

            ResourceNotFoundException exception = assertThrows(
                    ResourceNotFoundException.class,
                    () -> appointmentService.schedule(validRequest)
            );

            assertEquals("Doctor not found: " + doctorId, exception.getMessage());
            verify(appointmentRepository, never()).save(any(Appointment.class));
        }
    }

    @Nested
    @DisplayName("Get Appointments for Patient Unit Tests")
    class GetForPatientTests {

        @Test
        @DisplayName("Regular Test: Returns list of appointments for a valid patient")
        void getForPatient_WhenAppointmentsExist_ReturnsAppointmentList() {
            when(appointmentRepository.findByPatientId(patientId))
                    .thenReturn(List.of(existingAppointment));
            when(staffRepository.findById(doctorId))
                    .thenReturn(Optional.of(existingDoctor));

            List<AppointmentResponse> responseList = appointmentService.getForPatient(patientId);

            assertNotNull(responseList);
            assertEquals(1, responseList.size());
            verify(appointmentRepository, times(1)).findByPatientId(patientId);
            verify(staffRepository, times(1)).findById(doctorId);
        }

        @Test
        @DisplayName("Edge Case: Returns empty list when patient has no appointments")
        void getForPatient_WhenNoAppointmentsFound_ReturnsEmptyList() {
            when(appointmentRepository.findByPatientId(patientId))
                    .thenReturn(Collections.emptyList());

            List<AppointmentResponse> responseList = appointmentService.getForPatient(patientId);

            assertNotNull(responseList);
            assertTrue(responseList.isEmpty());
            verify(staffRepository, never()).findById(anyString());
        }

        @Test
        @DisplayName("Edge Case: Handles appointment gracefully when assigned doctor is deleted/missing")
        void getForPatient_WhenDoctorMissing_NullDoctorHandledGracefully() {
            when(appointmentRepository.findByPatientId(patientId))
                    .thenReturn(List.of(existingAppointment));
            when(staffRepository.findById(doctorId))
                    .thenReturn(Optional.empty()); // Doctor no longer exists in DB

            List<AppointmentResponse> responseList = appointmentService.getForPatient(patientId);

            assertNotNull(responseList);
            assertEquals(1, responseList.size());
            verify(staffRepository, times(1)).findById(doctorId);
        }
    }

    @Nested
    @DisplayName("Cancel Appointment Unit Tests")
    class CancelTests {

        @Test
        @DisplayName("Regular Test: Successfully cancels an active appointment")
        void cancel_WhenAppointmentExists_UpdatesStatusToCancelled() {
            when(appointmentRepository.findById(appointmentId))
                    .thenReturn(Optional.of(existingAppointment));
            when(appointmentRepository.save(existingAppointment))
                    .thenReturn(existingAppointment);
            when(staffRepository.findById(doctorId))
                    .thenReturn(Optional.of(existingDoctor));

            AppointmentResponse response = appointmentService.cancel(appointmentId);

            assertNotNull(response);
            assertEquals(AppointmentStatus.CANCELLED, existingAppointment.getStatus());
            verify(appointmentRepository, times(1)).save(existingAppointment);
        }

        @Test
        @DisplayName("Edge Case: Throws ResourceNotFoundException when cancelling invalid appointment ID")
        void cancel_WhenAppointmentDoesNotExist_ThrowsResourceNotFoundException() {
            String invalidId = "invalid-appt-id";
            when(appointmentRepository.findById(invalidId)).thenReturn(Optional.empty());

            ResourceNotFoundException exception = assertThrows(
                    ResourceNotFoundException.class,
                    () -> appointmentService.cancel(invalidId)
            );

            assertEquals("Appointment not found: " + invalidId, exception.getMessage());
            verify(appointmentRepository, never()).save(any(Appointment.class));
        }
    }
}