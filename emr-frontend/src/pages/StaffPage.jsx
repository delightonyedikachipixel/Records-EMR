import React, { useEffect, useState } from "react";
import { UserPlus } from "lucide-react";
import { getAllStaff } from "../api/staff";
import { CreateStaffModal } from "../components/staff/CreateStaffModal";
import { Badge } from "../components/common/Badge";
import { ErrorBanner } from "../components/common/ErrorBanner";
import { roleLabel } from "../utils/enums";

export function StaffPage() {
  const [staff, setStaff] = useState([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState(null);
  const [showModal, setShowModal] = useState(false);

  useEffect(() => {
    load();
  }, []);

  function load() {
    setLoading(true);
    setError(null);
    getAllStaff()
      .then(setStaff)
      .catch((err) => setError(err.message || "Could not load staff."))
      .finally(() => setLoading(false));
  }

  return (
    <div>
      <div className="mb-5 flex items-center justify-between">
        <div className="font-display text-2xl text-ink">Staff accounts</div>
        <button onClick={() => setShowModal(true)} className="btn-primary">
          <UserPlus size={15} />
          Create account
        </button>
      </div>

      {error && <ErrorBanner message={error} />}
      {loading && <div className="py-8 text-center text-sm text-ink-faint">Loading staff…</div>}

      {!loading && !error && (
        <div className="card overflow-hidden">
          <table className="w-full border-collapse text-[13.5px]">
            <thead>
              <tr className="bg-paper/60">
                {["Name", "Role", "Username"].map((h) => (
                  <th
                    key={h}
                    className="border-b border-border px-4 py-2.5 text-left text-[11.5px] font-bold uppercase tracking-wide text-ink-soft"
                  >
                    {h}
                  </th>
                ))}
              </tr>
            </thead>
            <tbody>
              {staff.map((u) => (
                <tr key={u.id} className="border-b border-border last:border-0">
                  <td className="px-4 py-3 text-ink">{u.name}</td>
                  <td className="px-4 py-3">
                    <Badge className="bg-teal-tint text-teal">{roleLabel(u.role)}</Badge>
                  </td>
                  <td className="px-4 py-3 font-mono text-ink-soft">{u.username}</td>
                </tr>
              ))}
              {staff.length === 0 && (
                <tr>
                  <td colSpan={3} className="px-4 py-8 text-center text-ink-faint">
                    No staff accounts yet.
                  </td>
                </tr>
              )}
            </tbody>
          </table>
        </div>
      )}

      {showModal && (
        <CreateStaffModal
          onClose={() => setShowModal(false)}
          onCreated={(s) => {
            setStaff((prev) => [s, ...prev]);
            setShowModal(false);
          }}
        />
      )}
    </div>
  );
}
