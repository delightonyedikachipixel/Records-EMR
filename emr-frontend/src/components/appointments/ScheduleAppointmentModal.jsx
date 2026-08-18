import React, { useState } from "react";
import { Check } from "lucide-react";
import { Modal } from "../common/Modal";
import { Field } from "../common/Field";
import { ErrorBanner } from "../common/ErrorBanner";
import { DoctorPicker } from "./DoctorPicker";
import { scheduleAppointment } from "../../api/appointments";
import { toLocalDateTimeString } from "../../utils/format";
import { ApiError } from "../../api/client";

export function ScheduleAppointmentModal({ patientId, patientName, onClose, onScheduled }) {
  const [doctorId, setDoctorId] = useState("");
  const [date, setDate] = useState("");
  const [time, setTime] = useState("");
  const [error, setError] = useState(null);
  const [fields, setFields] = useState(null);
  const [saving, setSaving] = useState(false);

  async function handleSubmit(e) {
    e.preventDefault();
    setError(null);
    setFields(null);

    const scheduledAt = toLocalDateTimeString(date, time);
    if (!scheduledAt) {
      setError("Pick a date and time for the appointment.");
      return;
    }
    if (new Date(scheduledAt) <= new Date()) {
      setError("Appointments must be scheduled in the future.");
      return;
    }
    if (!doctorId) {
      setError("Choose a doctor for this appointment.");
      return;
    }

    setSaving(true);
    try {
      const appt = await scheduleAppointment({ patientId, doctorId, scheduledAt });
      onScheduled(appt);
    } catch (err) {
      if (err instanceof ApiError) {
        setError(err.message);
        setFields(err.fields);
      } else {
        setError("Could not schedule the appointment.");
      }
    } finally {
      setSaving(false);
    }
  }

  return (
    <Modal
      title={`Schedule appointment${patientName ? ` — ${patientName}` : ""}`}
      onClose={onClose}
      footer={
        <>
          <button type="button" onClick={onClose} className="btn-ghost">
            Cancel
          </button>
          <button type="submit" form="schedule-appt-form" disabled={saving} className="btn-primary">
            <Check size={15} />
            {saving ? "Scheduling…" : "Schedule"}
          </button>
        </>
      }
    >
      <form id="schedule-appt-form" onSubmit={handleSubmit} className="flex flex-col gap-3">
        {error && <ErrorBanner message={error} fields={fields} />}
        <Field label="Doctor">
          <DoctorPicker value={doctorId} onChange={setDoctorId} required />
        </Field>
        <div className="flex gap-3">
          <Field label="Date" className="flex-1">
            <input
              type="date"
              className="field-input"
              value={date}
              onChange={(e) => setDate(e.target.value)}
              min={new Date().toISOString().slice(0, 10)}
              required
            />
          </Field>
          <Field label="Time" className="flex-1">
            <input
              type="time"
              className="field-input"
              value={time}
              onChange={(e) => setTime(e.target.value)}
              required
            />
          </Field>
        </div>
      </form>
    </Modal>
  );
}
