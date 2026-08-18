import React, { useEffect, useState } from "react";
import { getAllStaff } from "../../api/staff";
import { useAuth } from "../../context/AuthContext";


export function DoctorPicker({ value, onChange, required }) {
  const { user } = useAuth();
  const [doctors, setDoctors] = useState(null); 
  const [forbidden, setForbidden] = useState(false);

  useEffect(() => {
    if (user.role === "DOCTOR" && user.userId) onChange(user.userId);
  }, [user.role, user.userId, onChange]);

  useEffect(() => {
    if (user.role === "DOCTOR") return; 
    let cancelled = false;
    getAllStaff()
      .then((all) => {
        if (cancelled) return;
        setDoctors(all.filter((s) => s.role === "DOCTOR"));
      })
      .catch((err) => {
        if (cancelled) return;
        if (err.status === 403) setForbidden(true);
        setDoctors([]);
      });
    return () => {
      cancelled = true;
    };
  }, [user.role]);

  if (user.role === "DOCTOR") {
    return (
      <input
        className="field-input bg-paper text-ink-soft"
        value={`${user.username} (you)`}
        disabled
      />
    );
  }

  if (forbidden) {
    return (
      <div>
        <input
          className="field-input"
          value={value}
          onChange={(e) => onChange(e.target.value)}
          placeholder="Doctor's staff ID"
          required={required}
        />
        <p className="mt-1 text-xs text-ink-faint">
          Your account can't list doctors yet — ask an admin for the doctor's staff ID, or open
          Staff (as an admin) to copy it.
        </p>
      </div>
    );
  }

  if (doctors === null) {
    return <input className="field-input bg-paper text-ink-faint" value="Loading doctors…" disabled />;
  }

  return (
    <select
      className="field-input"
      value={value}
      onChange={(e) => onChange(e.target.value)}
      required={required}
    >
      <option value="">Select a doctor</option>
      {doctors.map((d) => (
        <option key={d.id} value={d.id}>
          {d.name}
        </option>
      ))}
    </select>
  );
}
