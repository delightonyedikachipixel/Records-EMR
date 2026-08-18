import { api } from "./client";

// POST /api/users/create/staff (ADMIN only)
export function registerStaff(payload) {
  return api.post("/api/users/create/staff", payload);
}

// GET /api/users (ADMIN only)
export function getAllStaff() {
  return api.get("/api/users");
}

// GET /api/users/{id} (ADMIN only)
export function getStaff(id) {
  return api.get(`/api/users/${id}`);
}
