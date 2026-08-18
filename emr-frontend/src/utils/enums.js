

export const BLOOD_GROUPS = [
  { value: "A_POSITIVE", label: "A+" },
  { value: "A_NEGATIVE", label: "A-" },
  { value: "B_POSITIVE", label: "B+" },
  { value: "B_NEGATIVE", label: "B-" },
  { value: "AB_POSITIVE", label: "AB+" },
  { value: "AB_NEGATIVE", label: "AB-" },
  { value: "O_POSITIVE", label: "O+" },
  { value: "O_NEGATIVE", label: "O-" },
];

export const GENOTYPES = ["AA", "AS", "AC", "SC", "SS"];

export const ROLES = [
  { value: "ADMIN", label: "Admin" },
  { value: "DOCTOR", label: "Doctor" },
  { value: "FRONT_DESK", label: "Front desk" },
];

export const APPOINTMENT_STATUSES = ["SCHEDULED", "COMPLETED", "PENDING", "CANCELLED"];

export function bloodGroupLabel(value) {
  return BLOOD_GROUPS.find((b) => b.value === value)?.label || value || "—";
}

export function roleLabel(value) {
  return ROLES.find((r) => r.value === value)?.label || value || "—";
}
