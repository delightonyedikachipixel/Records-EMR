import { api } from "./client";

export function login(username, password) {
  return api.post("/api/auth/login", { username, password }, { auth: false });
}
