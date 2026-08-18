import React, { useState } from "react";
import { ChevronDown, ChevronUp, Pill } from "lucide-react";
import { fmtDate } from "../../utils/format";
import { getPrescriptionsForVisit } from "../../api/prescriptions";
import { AddPrescriptionForm } from "./AddPrescriptionForm";
import { useAuth } from "../../context/AuthContext";

export function VisitCard({ visit }) {
  const { user } = useAuth();
  const [expanded, setExpanded] = useState(false);
  const [rx, setRx] = useState(null);
  const [loading, setLoading] = useState(false);

  async function toggle() {
    const next = !expanded;
    setExpanded(next);
    if (next && rx === null) {
      setLoading(true);
      try {
        const list = await getPrescriptionsForVisit(visit.id);
        setRx(list);
      } catch {
        setRx([]);
      } finally {
        setLoading(false);
      }
    }
  }

  return (
    <div className="card p-4">
      <div className="mb-1.5 flex items-center justify-between">
        <span className="font-display text-[15.5px] text-ink">{visit.diagnosis}</span>
        <span className="font-mono text-[11.5px] text-ink-faint">{fmtDate(visit.date)}</span>
      </div>
      <div className="mb-1 text-[13px] text-ink-soft">Symptoms: {visit.symptoms}</div>
      {visit.notes && <div className="mb-1 text-[13px] text-ink-soft">{visit.notes}</div>}
      <div className="mb-2 text-xs text-ink-faint">{visit.doctorName}</div>

      <button
        onClick={toggle}
        className="flex items-center gap-1 text-xs font-semibold text-teal transition-colors hover:text-teal-dark"
      >
        <Pill size={13} />
        Prescriptions
        {expanded ? <ChevronUp size={13} /> : <ChevronDown size={13} />}
      </button>

      {expanded && (
        <div className="mt-3 flex flex-col gap-2 border-t border-border pt-3">
          {loading && <div className="text-xs text-ink-faint">Loading…</div>}
          {!loading &&
            rx?.map((p) => (
              <div key={p.id} className="flex items-center gap-2 rounded-md bg-paper px-3 py-2">
                <Pill size={13} className="text-gold" />
                <div className="text-[13px] text-ink">
                  <span className="font-semibold">{p.medicineName}</span> — {p.dosage}, {p.duration}
                </div>
              </div>
            ))}
          {!loading && rx?.length === 0 && (
            <div className="text-xs text-ink-faint">No prescriptions logged for this visit.</div>
          )}
          {user.role === "DOCTOR" && (
            <AddPrescriptionForm visitId={visit.id} onAdded={(p) => setRx((prev) => [...(prev || []), p])} />
          )}
        </div>
      )}
    </div>
  );
}
