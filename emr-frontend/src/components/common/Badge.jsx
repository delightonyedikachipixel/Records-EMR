import React from "react";

const STATUS_STYLES = {
  SCHEDULED: "text-teal bg-teal-tint",
  PENDING: "text-gold bg-gold-tint",
  COMPLETED: "text-ink-soft bg-paper",
  CANCELLED: "text-rust bg-rust-tint",
};

export function Badge({ children, className = "" }) {
  return (
    <span
      className={`inline-block whitespace-nowrap rounded-full px-2.5 py-1 text-[11px] font-bold uppercase tracking-wide ${className}`}
    >
      {children}
    </span>
  );
}

export function StatusBadge({ status }) {
  return (
    <Badge className={STATUS_STYLES[status] || "text-ink-soft bg-paper"}>
      {status?.toLowerCase() || "unknown"}
    </Badge>
  );
}
