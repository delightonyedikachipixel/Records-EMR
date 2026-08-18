import React from "react";

export function EmptyState({ icon: Icon, title, hint }) {
  return (
    <div className="flex flex-col items-center gap-2 rounded-lg border border-dashed border-border py-12 text-center">
      {Icon && <Icon size={22} className="text-ink-faint" />}
      <div className="font-display text-base text-ink">{title}</div>
      {hint && <div className="max-w-xs text-xs text-ink-soft">{hint}</div>}
    </div>
  );
}
