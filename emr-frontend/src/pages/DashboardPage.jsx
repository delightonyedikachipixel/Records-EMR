import React, { useEffect, useState } from "react";
import { useNavigate } from "react-router-dom";
import { Users, UserPlus, Search, Calendar, Shield } from "lucide-react";
import { searchPatients } from "../api/patients";
import { useAuth } from "../context/AuthContext";
import { roleLabel } from "../utils/enums";
import { ErrorBanner } from "../components/common/ErrorBanner";

export function DashboardPage() {
  const { user } = useAuth();
  const navigate = useNavigate();
  const [patientCount, setPatientCount] = useState(null);
  const [error, setError] = useState(null);

  useEffect(() => {
    searchPatients("")
      .then((all) => setPatientCount(all.length))
      .catch((err) => setError(err.message || "Could not load patient count."));
  }, []);

  const actions = [
    {
      label: "Register a patient",
      icon: UserPlus,
      onClick: () => navigate("/patients"),
      roles: ["FRONT_DESK", "ADMIN"],
    },
    {
      label: "Search patients",
      icon: Search,
      onClick: () => navigate("/patients"),
      roles: ["FRONT_DESK", "DOCTOR", "ADMIN"],
    },
    {
      label: "Schedule an appointment",
      icon: Calendar,
      onClick: () => navigate("/appointments"),
      roles: ["FRONT_DESK", "DOCTOR"],
    },
    {
      label: "Manage staff",
      icon: Shield,
      onClick: () => navigate("/staff"),
      roles: ["ADMIN"],
    },
  ].filter((a) => a.roles.includes(user.role));

  return (
    <div>
      <div className="font-display text-2xl text-ink">
        Good day, {user.username}
      </div>
      <div className="mt-1 mb-6 text-[13.5px] text-ink-soft">
        Signed in as {roleLabel(user.role)} ·{" "}
        {new Date().toLocaleDateString("en-GB", { weekday: "long", day: "numeric", month: "long" })}
      </div>

      {error && <ErrorBanner message={error} />}

      <div className="mb-7 flex gap-3">
        <div className="card min-w-[160px] p-4">
          <div className="mb-2 flex items-center justify-between">
            <span className="text-xs font-semibold text-ink-soft">Total patients</span>
            <Users size={16} className="text-teal" />
          </div>
          <div className="font-display text-3xl text-ink">
            {patientCount === null ? "…" : patientCount}
          </div>
        </div>
      </div>

      <div className="mb-3 text-xs font-bold uppercase tracking-wide text-ink-soft">Quick actions</div>
      <div className="grid grid-cols-2 gap-3 sm:grid-cols-4">
        {actions.map((a) => (
          <button
            key={a.label}
            onClick={a.onClick}
            className="card flex flex-col items-start gap-2 p-4 text-left transition-shadow hover:shadow-sm"
          >
            <a.icon size={18} className="text-teal" />
            <span className="text-[13.5px] font-semibold text-ink">{a.label}</span>
          </button>
        ))}
      </div>
    </div>
  );
}
