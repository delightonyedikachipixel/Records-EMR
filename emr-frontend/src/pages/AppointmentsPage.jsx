import React, { useState } from "react";
import { Search, CalendarPlus } from "lucide-react";
import { searchPatients } from "../api/patients";
import { getAppointmentsForPatient } from "../api/appointments";
import { AppointmentRow } from "../components/appointments/AppointmentRow";
import { ScheduleAppointmentModal } from "../components/appointments/ScheduleAppointmentModal";
import { EmptyState } from "../components/common/EmptyState";
import { ErrorBanner } from "../components/common/ErrorBanner";
import { useAuth } from "../context/AuthContext";

export function AppointmentsPage() {
  const { user } = useAuth();
  const [q, setQ] = useState("");
  const [results, setResults] = useState([]);
  const [selected, setSelected] = useState(null);
  const [appointments, setAppointments] = useState([]);
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState(null);
  const [showSchedule, setShowSchedule] = useState(false);

  const canSchedule = user.role === "FRONT_DESK" || user.role === "DOCTOR";

  async function handleSearch(e) {
    e.preventDefault();
    setError(null);
    setSelected(null);
    setLoading(true);
    try {
      const patients = await searchPatients(q);
      setResults(patients);
    } catch (err) {
      setError(err.message || "Could not search patients.");
    } finally {
      setLoading(false);
    }
  }

  async function selectPatient(patient) {
    setSelected(patient);
    setError(null);
    setLoading(true);
    try {
      const appts = await getAppointmentsForPatient(patient.id);
      setAppointments(appts);
    } catch (err) {
      setError(err.message || "Could not load appointments.");
    } finally {
      setLoading(false);
    }
  }

  return (
    <div>
      <div className="mb-5 font-display text-2xl text-ink">Appointments</div>

      <form onSubmit={handleSearch} className="relative mb-4">
        <Search size={15} className="pointer-events-none absolute left-3 top-1/2 -translate-y-1/2 text-ink-faint" />
        <input
          value={q}
          onChange={(e) => setQ(e.target.value)}
          placeholder="Search for a patient by name to view or schedule their appointments"
          className="field-input w-full pl-9"
        />
      </form>

      {error && <ErrorBanner message={error} />}

      {!selected && !loading && results.length > 0 && (
        <div className="flex flex-col gap-1.5">
          {results.map((p) => (
            <button
              key={p.id}
              onClick={() => selectPatient(p)}
              className="card px-4 py-2.5 text-left text-sm text-ink transition-colors hover:bg-paper"
            >
              {p.name} <span className="text-ink-faint">· {p.phoneNumber}</span>
            </button>
          ))}
        </div>
      )}

      {!selected && !loading && results.length === 0 && (
        <EmptyState icon={Search} title="Search for a patient" hint="Their upcoming and past appointments will show here." />
      )}

      {loading && <div className="py-8 text-center text-sm text-ink-faint">Loading…</div>}

      {selected && !loading && (
        <div>
          <div className="mb-3 flex items-center justify-between">
            <button onClick={() => setSelected(null)} className="text-sm text-ink-soft hover:text-ink">
              ← Choose a different patient
            </button>
            {canSchedule && (
              <button onClick={() => setShowSchedule(true)} className="btn-primary">
                <CalendarPlus size={15} />
                Schedule for {selected.name}
              </button>
            )}
          </div>
          <div className="flex flex-col gap-2">
            {appointments.map((a) => (
              <AppointmentRow
                key={a.id}
                appointment={a}
                onChanged={(updated) =>
                  setAppointments((prev) => prev.map((x) => (x.id === updated.id ? updated : x)))
                }
              />
            ))}
            {appointments.length === 0 && <EmptyState icon={CalendarPlus} title="No appointments yet" />}
          </div>
        </div>
      )}

      {showSchedule && selected && (
        <ScheduleAppointmentModal
          patientId={selected.id}
          patientName={selected.name}
          onClose={() => setShowSchedule(false)}
          onScheduled={(appt) => {
            setAppointments((prev) => [appt, ...prev]);
            setShowSchedule(false);
          }}
        />
      )}
    </div>
  );
}
