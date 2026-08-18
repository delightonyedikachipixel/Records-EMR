import React, { useState } from "react";
import { Plus, Check } from "lucide-react";
import { Field } from "../common/Field";
import { ErrorBanner } from "../common/ErrorBanner";
import { addPrescription } from "../../api/prescriptions";
import { ApiError } from "../../api/client";

export function AddPrescriptionForm({ visitId, onAdded }) {
  const [open, setOpen] = useState(false);
  const [form, setForm] = useState({ medicineName: "", dosage: "", duration: "" });
  const [error, setError] = useState(null);
  const [saving, setSaving] = useState(false);

  if (!open) {
    return (
      <button onClick={() => setOpen(true)} className="btn-ghost">
        <Plus size={15} />
        Add prescription
      </button>
    );
  }

  async function handleSubmit(e) {
    e.preventDefault();
    setError(null);
    if (!form.medicineName || !form.dosage || !form.duration) {
      setError("Medicine, dosage, and duration are all required.");
      return;
    }
    setSaving(true);
    try {
      const rx = await addPrescription({ visitId, ...form });
      onAdded(rx);
      setForm({ medicineName: "", dosage: "", duration: "" });
      setOpen(false);
    } catch (err) {
      setError(err instanceof ApiError ? err.message : "Could not save the prescription.");
    } finally {
      setSaving(false);
    }
  }

  return (
    <form onSubmit={handleSubmit} className="card flex flex-col gap-3 p-4">
      {error && <ErrorBanner message={error} />}
      <Field label="Medicine">
        <input
          className="field-input"
          value={form.medicineName}
          onChange={(e) => setForm({ ...form, medicineName: e.target.value })}
          placeholder="Artemether/Lumefantrine"
        />
      </Field>
      <div className="flex gap-3">
        <Field label="Dosage" className="flex-1">
          <input
            className="field-input"
            value={form.dosage}
            onChange={(e) => setForm({ ...form, dosage: e.target.value })}
            placeholder="80/480mg"
          />
        </Field>
        <Field label="Duration" className="flex-1">
          <input
            className="field-input"
            value={form.duration}
            onChange={(e) => setForm({ ...form, duration: e.target.value })}
            placeholder="3 days"
          />
        </Field>
      </div>
      <div className="flex justify-end gap-2">
        <button type="button" onClick={() => setOpen(false)} className="btn-ghost">
          Cancel
        </button>
        <button type="submit" disabled={saving} className="btn-primary">
          <Check size={15} />
          {saving ? "Saving…" : "Save prescription"}
        </button>
      </div>
    </form>
  );
}
