import React from "react";
import { X } from "lucide-react";

export function Modal({ title, onClose, children, footer, width = 460 }) {
  return (
    <div className="fixed inset-0 z-50 flex items-center justify-center bg-ink/40 p-5">
      <div
        className="max-h-[90vh] w-full overflow-y-auto rounded-xl bg-white"
        style={{ maxWidth: width }}
      >
        <div className="flex items-center justify-between border-b border-border px-5 py-4">
          <span className="font-display text-lg text-ink">{title}</span>
          <button
            onClick={onClose}
            className="text-ink-soft transition-colors hover:text-ink"
            aria-label="Close"
          >
            <X size={18} />
          </button>
        </div>
        <div className="px-5 py-4">{children}</div>
        {footer && (
          <div className="flex justify-end gap-2 border-t border-border px-5 py-4">{footer}</div>
        )}
      </div>
    </div>
  );
}
