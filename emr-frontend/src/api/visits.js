import { api } from "./client";

// POST /api/visits (DOCTOR only)
export function logVisit(payload) {
  return api.post("/api/visits", payload);
}

// GET /api/visits/patient/{patientId}
export function getVisitsForPatient(patientId) {
  return api.get(`/api/visits/patient/${patientId}`);
}
