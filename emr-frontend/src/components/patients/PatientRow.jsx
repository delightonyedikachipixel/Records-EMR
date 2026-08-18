import React from "react";
import { ChevronRight } from "lucide-react";
import { age, initials } from "../../utils/format";
import { bloodGroupLabel } from "../../utils/enums";

const TAB_COLORS = [
  { fg: "#0B5D52", bg: "#E4EFEC" },
  { fg: "#B4791F", bg: "#F6EBD8" },
  { fg: "#A2402F", bg: "#F3E2DD" },
  { fg: "#3E5266", bg: "#E4E9EE" },
];

function tabColor(seed = "") {
  return TAB_COLORS[(seed.charCodeAt(0) || 0) % TAB_COLORS.length];
}

export function PatientRow({ patient, onOpen }) {
  const tc = tabColor(patient.name);
  return (
    <button
      onClick={onOpen}
      className="flex w-full items-center gap-4 overflow-hidden rounded-lg border border-border bg-white text-left transition-shadow hover:shadow-sm"
    >
      <div style={{ background: tc.fg }} className="h-full w-2 self-stretch" />
      <div
        style={{ background: tc.bg, color: tc.fg }}
        className="flex h-9 w-9 flex-shrink-0 items-center justify-center rounded-lg text-[13px] font-bold"
      >
        {initials(patient.name)}
      </div>
      <div className="min-w-0 flex-1 py-3.5">
        <div className="font-display text-base text-ink">{patient.name}</div>
        <div className="mt-0.5 text-xs text-ink-soft">
          {age(patient.dateOfBirth) ?? "—"} yrs · {patient.gender} · {bloodGroupLabel(patient.bloodGroup)}
        </div>
      </div>
      <div className="mr-1 font-mono text-xs text-ink-faint">{patient.phoneNumber}</div>
      <ChevronRight size={16} className="mr-4 flex-shrink-0 text-ink-faint" />
    </button>
  );
}
