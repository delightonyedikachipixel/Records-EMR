import { api } from "./client";

export function registerPatient(payload) {
  return api.post("/api/patients/register", payload);
}

export function getPatient(id) {
  return api.get(`/api/patients/${id}`);
}

export function searchPatients(q) {
  const query = q ? `?q=${encodeURIComponent(q)}` : "";
  return api.get(`/api/patients${query}`);
}
