import React, { useEffect, useState } from "react";
import { useNavigate, useParams } from "react-router-dom";
import { ArrowLeft, FileText, Activity, Calendar, CalendarPlus } from "lucide-react";
import { getPatient } from "../api/patients";
import { getVisitsForPatient } from "../api/visits";
import { getAppointmentsForPatient } from "../api/appointments";
import { age, initials, fmtDate } from "../utils/format";
import { bloodGroupLabel } from "../utils/enums";
import { Badge } from "../components/common/Badge";
import { ErrorBanner } from "../components/common/ErrorBanner";
import { EmptyState } from "../components/common/EmptyState";
import { AddVisitForm } from "../components/patients/AddVisitForm";
import { VisitCard } from "../components/patients/VisitCard";
import { AppointmentRow } from "../components/appointments/AppointmentRow";
import { ScheduleAppointmentModal } from "../components/appointments/ScheduleAppointmentModal";
import { useAuth } from "../context/AuthContext";

const TABS = [
  { id: "overview", label: "Overview", icon: FileText },
  { id: "visits", label: "Visits", icon: Activity },
  { id: "appointments", label: "Appointments", icon: Calendar },
];

export function PatientDetailPage() {
  const { id } = useParams();
  const navigate = useNavigate();
  const { user } = useAuth();

  const [tab, setTab] = useState("overview");
  const [patient, setPatient] = useState(null);
  const [visits, setVisits] = useState([]);
  const [appointments, setAppointments] = useState([]);
  const [error, setError] = useState(null);
  const [loading, setLoading] = useState(true);
  const [showSchedule, setShowSchedule] = useState(false);

  const canSchedule = user.role === "FRONT_DESK" || user.role === "DOCTOR";

  useEffect(() => {
    let cancelled = false;
    setLoading(true);
    setError(null);
    Promise.all([getPatient(id), getVisitsForPatient(id), getAppointmentsForPatient(id)])
      .then(([p, v, a]) => {
        if (cancelled) return;
        setPatient(p);
        setVisits(v);
        setAppointments(a);
      })
      .catch((err) => {
        if (cancelled) return;
        setError(err.message || "Could not load this patient.");
      })
      .finally(() => !cancelled && setLoading(false));
    return () => {
      cancelled = true;
    };
  }, [id]);

  if (loading) return <div className="py-8 text-center text-sm text-ink-faint">Loading patient…</div>;
  if (error) return <ErrorBanner message={error} />;
  if (!patient) return null;

  return (
    <div>
      <button
        onClick={() => navigate("/patients")}
        className="mb-4 flex items-center gap-1.5 text-sm text-ink-soft transition-colors hover:text-ink"
      >
        <ArrowLeft size={15} />
        Back to patients
      </button>

      <div className="mb-5 flex items-start gap-4">
        <div className="flex h-14 w-14 flex-shrink-0 items-center justify-center rounded-xl bg-teal-tint text-lg font-bold text-teal">
          {initials(patient.name)}
        </div>
        <div className="flex-1">
          <div className="font-display text-2xl text-ink">{patient.name}</div>
          <div className="mt-0.5 text-[13px] text-ink-soft">
            {age(patient.dateOfBirth) ?? "—"} years old · {patient.gender} · DOB {fmtDate(patient.dateOfBirth)}
          </div>
        </div>
        <Badge className="bg-teal-tint text-teal">
          {bloodGroupLabel(patient.bloodGroup)} · {patient.genotype}
        </Badge>
        {canSchedule && (
          <button onClick={() => setShowSchedule(true)} className="btn-ghost">
            <CalendarPlus size={15} />
            Schedule
          </button>
        )}
      </div>

      <div className="flex gap-6">
        <div className="flex w-40 flex-shrink-0 flex-col gap-1">
          {TABS.map((t) => (
            <button
              key={t.id}
              onClick={() => setTab(t.id)}
              className={`flex items-center gap-2 rounded-md border-l-[3px] px-2.5 py-2 text-left text-[13.5px] transition-colors ${
                tab === t.id
                  ? "border-teal bg-teal-tint font-semibold text-teal"
                  : "border-transparent font-medium text-ink-soft hover:bg-paper"
              }`}
            >
              <t.icon size={15} />
              {t.label}
            </button>
          ))}
        </div>

        <div className="min-w-0 flex-1">
          {tab === "overview" && (
            <div className="flex flex-col gap-3">
              <div className="card p-4">
                <div className="mb-3 text-xs font-bold uppercase tracking-wide text-ink-soft">Contact</div>
                <div className="grid grid-cols-2 gap-4 text-[13.5px] text-ink">
                  <div>
                    <div className="text-xs text-ink-faint">Phone</div>
                    <div className="mt-0.5 font-mono">{patient.phoneNumber}</div>
                  </div>
                  <div>
                    <div className="text-xs text-ink-faint">Address</div>
                    <div className="mt-0.5">{patient.address || "—"}</div>
                  </div>
                </div>
              </div>
              <div className="card p-4">
                <div className="mb-2 text-xs font-bold uppercase tracking-wide text-ink-soft">
                  Most recent visit
                </div>
                {visits[0] ? (
                  <div>
                    <div className="text-[13.5px] text-ink">{visits[0].diagnosis}</div>
                    <div className="mt-0.5 text-xs text-ink-soft">
                      {fmtDate(visits[0].date)} · {visits[0].doctorName}
                    </div>
                  </div>
                ) : (
                  <div className="text-[13px] text-ink-faint">No visits recorded yet.</div>
                )}
              </div>
            </div>
          )}

          {tab === "visits" && (
            <div className="flex flex-col gap-3">
              {user.role === "DOCTOR" && (
                <AddVisitForm patientId={patient.id} onAdded={(v) => setVisits((prev) => [v, ...prev])} />
              )}
              {visits.map((v) => (
                <VisitCard key={v.id} visit={v} />
              ))}
              {visits.length === 0 && (
                <EmptyState icon={Activity} title="No visits recorded yet" />
              )}
            </div>
          )}

          {tab === "appointments" && (
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
              {appointments.length === 0 && (
                <EmptyState icon={Calendar} title="No appointments scheduled" />
              )}
            </div>
          )}
        </div>
      </div>

      {showSchedule && (
        <ScheduleAppointmentModal
          patientId={patient.id}
          patientName={patient.name}
          onClose={() => setShowSchedule(false)}
          onScheduled={(appt) => {
            setAppointments((prev) => [appt, ...prev]);
            setShowSchedule(false);
            setTab("appointments");
          }}
        />
      )}
    </div>
  );
}
