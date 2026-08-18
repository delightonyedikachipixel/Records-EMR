import { api } from "./client";

// POST /api/prescriptions/dosage (DOCTOR only)
export function addPrescription(payload) {
  return api.post("/api/prescriptions/dosage", payload);
}

// GET /api/prescriptions/visit/{visitId}
export function getPrescriptionsForVisit(visitId) {
  return api.get(`/api/prescriptions/visit/${visitId}`);
}
