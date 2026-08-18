import { api } from "./client";

export function scheduleAppointment(payload) {
  return api.post("/api/appointments/schedule", payload);
}

export function getAppointmentsForPatient(patientId) {
  return api.get(`/api/appointments/patient/${patientId}`);
}

export function cancelAppointment(id) {
  return api.patch(`/api/appointments/${id}/cancel`);
}
