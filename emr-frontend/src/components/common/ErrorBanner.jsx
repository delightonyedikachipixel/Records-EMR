import React from "react";
import { AlertCircle } from "lucide-react";

export function ErrorBanner({ message, fields }) {
  if (!message) return null;
  return (
    <div className="flex items-start gap-2 rounded-md border border-rust/30 bg-rust-tint px-3 py-2.5 text-sm text-rust">
      <AlertCircle size={16} className="mt-0.5 flex-shrink-0" />
      <div>
        <div>{message}</div>
        {fields && (
          <ul className="mt-1 list-disc pl-4 text-xs">
            {Object.entries(fields).map(([k, v]) => (
              <li key={k}>
                <span className="font-semibold">{k}</span>: {v}
              </li>
            ))}
          </ul>
        )}
      </div>
    </div>
  );
}
