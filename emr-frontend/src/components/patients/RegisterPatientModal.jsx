import React, { useState } from "react";
import { Check } from "lucide-react";
import { Modal } from "../common/Modal";
import { Field } from "../common/Field";
import { ErrorBanner } from "../common/ErrorBanner";
import { registerPatient } from "../../api/patients";
import { BLOOD_GROUPS, GENOTYPES } from "../../utils/enums";
import { ApiError } from "../../api/client";

const PHONE_PATTERN = /^0[0-9]{10}$/;

export function RegisterPatientModal({ onClose, onRegistered }) {
  const [form, setForm] = useState({
    name: "",
    dateOfBirth: "",
    gender: "Female",
    phoneNumber: "",
    address: "",
    bloodGroup: "O_POSITIVE",
    genotype: "AA",
  });
  const [error, setError] = useState(null);
  const [fields, setFields] = useState(null);
  const [saving, setSaving] = useState(false);

  const set = (k) => (e) => setForm({ ...form, [k]: e.target.value });

  async function handleSubmit(e) {
    e.preventDefault();
    setError(null);
    setFields(null);

    if (!PHONE_PATTERN.test(form.phoneNumber)) {
      setError("Phone number must be 11 digits, starting with 0 (e.g. 08031234567).");
      return;
    }

    setSaving(true);
    try {
      const saved = await registerPatient(form);
      onRegistered(saved);
    } catch (err) {
      if (err instanceof ApiError) {
        setError(err.message);
        setFields(err.fields);
      } else {
        setError("Could not register patient.");
      }
    } finally {
      setSaving(false);
    }
  }

  return (
    <Modal
      title="Register new patient"
      onClose={onClose}
      footer={
        <>
          <button type="button" onClick={onClose} className="btn-ghost">
            Cancel
          </button>
          <button type="submit" form="register-patient-form" disabled={saving} className="btn-primary">
            <Check size={15} />
            {saving ? "Saving…" : "Save patient"}
          </button>
        </>
      }
    >
      <form id="register-patient-form" onSubmit={handleSubmit} className="flex flex-col gap-3">
        {error && <ErrorBanner message={error} fields={fields} />}
        <Field label="Full name">
          <input
            className="field-input"
            value={form.name}
            onChange={set("name")}
            placeholder="Adaeze Nwosu"
            required
          />
        </Field>
        <div className="flex gap-3">
          <Field label="Date of birth" className="flex-1">
            <input
              type="date"
              className="field-input"
              value={form.dateOfBirth}
              onChange={set("dateOfBirth")}
              max={new Date().toISOString().slice(0, 10)}
              required
            />
          </Field>
          <Field label="Gender" className="flex-1">
            <select className="field-input" value={form.gender} onChange={set("gender")}>
              <option>Female</option>
              <option>Male</option>
              <option>Other</option>
            </select>
          </Field>
        </div>
        <Field label="Phone number">
          <input
            className="field-input"
            value={form.phoneNumber}
            onChange={set("phoneNumber")}
            placeholder="08031234567"
            required
          />
        </Field>
        <Field label="Address">
          <input
            className="field-input"
            value={form.address}
            onChange={set("address")}
            placeholder="14 Adeola Ln, Ikeja"
          />
        </Field>
        <div className="flex gap-3">
          <Field label="Blood group" className="flex-1">
            <select className="field-input" value={form.bloodGroup} onChange={set("bloodGroup")}>
              {BLOOD_GROUPS.map((b) => (
                <option key={b.value} value={b.value}>
                  {b.label}
                </option>
              ))}
            </select>
          </Field>
          <Field label="Genotype" className="flex-1">
            <select className="field-input" value={form.genotype} onChange={set("genotype")}>
              {GENOTYPES.map((g) => (
                <option key={g} value={g}>
                  {g}
                </option>
              ))}
            </select>
          </Field>
        </div>
      </form>
    </Modal>
  );
}
