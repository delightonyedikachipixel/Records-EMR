import React from "react";

export function Field({ label, error, children, className = "" }) {
  return (
    <label className={`block ${className}`}>
      <span className="field-label">{label}</span>
      {children}
      {error && <span className="mt-1 block text-xs text-rust">{error}</span>}
    </label>
  );
}
