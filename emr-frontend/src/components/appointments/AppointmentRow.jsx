import React, { useState } from "react";
import { X } from "lucide-react";
import { fmtDateTime } from "../../utils/format";
import { StatusBadge } from "../common/Badge";
import { cancelAppointment } from "../../api/appointments";
import { useAuth } from "../../context/AuthContext";

export function AppointmentRow({ appointment, onChanged }) {
  const { user } = useAuth();
  const [busy, setBusy] = useState(false);
  const canCancel =
    appointment.status === "SCHEDULED" &&
    ["ADMIN", "DOCTOR", "FRONT_DESK"].includes(user.role);

  async function handleCancel() {
    setBusy(true);
    try {
      const updated = await cancelAppointment(appointment.id);
      onChanged(updated);
    } catch {
    } finally {
      setBusy(false);
    }
  }

  return (
    <div className="card flex items-center gap-3 p-3.5">
      <div className="flex-1">
        <div className="text-sm font-semibold text-ink">{fmtDateTime(appointment.scheduledAt)}</div>
        <div className="text-xs text-ink-soft">{appointment.doctorName}</div>
      </div>
      <StatusBadge status={appointment.status} />
      {canCancel && (
        <button
          onClick={handleCancel}
          disabled={busy}
          className="flex items-center gap-1 text-xs font-semibold text-ink-faint transition-colors hover:text-rust disabled:opacity-50"
        >
          <X size={13} />
          {busy ? "Cancelling…" : "Cancel"}
        </button>
      )}
    </div>
  );
}
