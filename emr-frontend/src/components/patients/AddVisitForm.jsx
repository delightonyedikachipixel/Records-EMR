import React, { useState } from "react";
import { Plus, Check } from "lucide-react";
import { Field } from "../common/Field";
import { ErrorBanner } from "../common/ErrorBanner";
import { logVisit } from "../../api/visits";
import { useAuth } from "../../context/AuthContext";
import { ApiError } from "../../api/client";

export function AddVisitForm({ patientId, onAdded }) {
  const { user } = useAuth();
  const [open, setOpen] = useState(false);
  const [form, setForm] = useState({ symptoms: "", diagnosis: "", notes: "" });
  const [error, setError] = useState(null);
  const [saving, setSaving] = useState(false);

  if (!open) {
    return (
      <button onClick={() => setOpen(true)} className="btn-ghost">
        <Plus size={15} />
        Log new visit
      </button>
    );
  }

  async function handleSubmit(e) {
    e.preventDefault();
    setError(null);
    if (!form.symptoms || !form.diagnosis) {
      setError("Symptoms and diagnosis are required.");
      return;
    }
    setSaving(true);
    try {
      const visit = await logVisit({
        patientId,
        doctorId: user.userId,
        symptoms: form.symptoms,
        diagnosis: form.diagnosis,
        notes: form.notes,
      });
      onAdded(visit);
      setForm({ symptoms: "", diagnosis: "", notes: "" });
      setOpen(false);
    } catch (err) {
      setError(err instanceof ApiError ? err.message : "Could not save the visit.");
    } finally {
      setSaving(false);
    }
  }

  return (
    <form onSubmit={handleSubmit} className="card flex flex-col gap-3 p-4">
      {error && <ErrorBanner message={error} />}
      <Field label="Symptoms">
        <input
          className="field-input"
          value={form.symptoms}
          onChange={(e) => setForm({ ...form, symptoms: e.target.value })}
          placeholder="Fever, headache"
        />
      </Field>
      <Field label="Diagnosis">
        <input
          className="field-input"
          value={form.diagnosis}
          onChange={(e) => setForm({ ...form, diagnosis: e.target.value })}
          placeholder="Malaria"
        />
      </Field>
      <Field label="Notes">
        <textarea
          className="field-input min-h-[70px] resize-y"
          value={form.notes}
          onChange={(e) => setForm({ ...form, notes: e.target.value })}
          placeholder="Started on ACT, review in 5 days."
        />
      </Field>
      <div className="flex justify-end gap-2">
        <button type="button" onClick={() => setOpen(false)} className="btn-ghost">
          Cancel
        </button>
        <button type="submit" disabled={saving} className="btn-primary">
          <Check size={15} />
          {saving ? "Saving…" : "Save visit"}
        </button>
      </div>
    </form>
  );
}
