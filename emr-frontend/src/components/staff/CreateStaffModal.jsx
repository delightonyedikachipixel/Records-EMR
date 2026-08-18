import React, { useState } from "react";
import { Check } from "lucide-react";
import { Modal } from "../common/Modal";
import { Field } from "../common/Field";
import { ErrorBanner } from "../common/ErrorBanner";
import { registerStaff } from "../../api/staff";
import { ROLES } from "../../utils/enums";
import { ApiError } from "../../api/client";

export function CreateStaffModal({ onClose, onCreated }) {
  const [form, setForm] = useState({ name: "", role: "DOCTOR", username: "", password: "" });
  const [error, setError] = useState(null);
  const [fields, setFields] = useState(null);
  const [saving, setSaving] = useState(false);

  const set = (k) => (e) => setForm({ ...form, [k]: e.target.value });

  async function handleSubmit(e) {
    e.preventDefault();
    setError(null);
    setFields(null);
    if (form.password.length < 8) {
      setError("Password must be at least 8 characters.");
      return;
    }
    setSaving(true);
    try {
      const staff = await registerStaff(form);
      onCreated(staff);
    } catch (err) {
      if (err instanceof ApiError) {
        setError(err.message);
        setFields(err.fields);
      } else {
        setError("Could not create the staff account.");
      }
    } finally {
      setSaving(false);
    }
  }

  return (
    <Modal
      title="Create staff account"
      onClose={onClose}
      footer={
        <>
          <button type="button" onClick={onClose} className="btn-ghost">
            Cancel
          </button>
          <button type="submit" form="create-staff-form" disabled={saving} className="btn-primary">
            <Check size={15} />
            {saving ? "Creating…" : "Create account"}
          </button>
        </>
      }
    >
      <form id="create-staff-form" onSubmit={handleSubmit} className="flex flex-col gap-3">
        {error && <ErrorBanner message={error} fields={fields} />}
        <Field label="Full name">
          <input className="field-input" value={form.name} onChange={set("name")} required />
        </Field>
        <Field label="Role">
          <select className="field-input" value={form.role} onChange={set("role")}>
            {ROLES.map((r) => (
              <option key={r.value} value={r.value}>
                {r.label}
              </option>
            ))}
          </select>
        </Field>
        <Field label="Username">
          <input className="field-input" value={form.username} onChange={set("username")} required />
        </Field>
        <Field label="Password">
          <input
            type="password"
            className="field-input"
            value={form.password}
            onChange={set("password")}
            placeholder="At least 8 characters"
            required
          />
        </Field>
      </form>
    </Modal>
  );
}
