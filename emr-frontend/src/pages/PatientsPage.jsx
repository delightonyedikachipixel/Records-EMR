import React, { useCallback, useEffect, useState } from "react";
import { useNavigate } from "react-router-dom";
import { Search, UserPlus } from "lucide-react";
import { searchPatients } from "../api/patients";
import { PatientRow } from "../components/patients/PatientRow";
import { RegisterPatientModal } from "../components/patients/RegisterPatientModal";
import { EmptyState } from "../components/common/EmptyState";
import { ErrorBanner } from "../components/common/ErrorBanner";
import { useAuth } from "../context/AuthContext";

export function PatientsPage() {
  const { user } = useAuth();
  const navigate = useNavigate();
  const [q, setQ] = useState("");
  const [patients, setPatients] = useState([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState(null);
  const [showModal, setShowModal] = useState(false);

  const canRegister = user.role === "FRONT_DESK" || user.role === "ADMIN";

  const load = useCallback(async (query) => {
    setLoading(true);
    setError(null);
    try {
      const results = await searchPatients(query);
      setPatients(results);
    } catch (err) {
      setError(err.message || "Could not load patients.");
    } finally {
      setLoading(false);
    }
  }, []);

  useEffect(() => {
    const t = setTimeout(() => load(q), 300);
    return () => clearTimeout(t);
  }, [q, load]);

  return (
    <div>
      <div className="mb-5 flex items-center justify-between">
        <div className="font-display text-2xl text-ink">Patients</div>
        {canRegister && (
          <button onClick={() => setShowModal(true)} className="btn-primary">
            <UserPlus size={15} />
            Register patient
          </button>
        )}
      </div>

      <div className="relative mb-4">
        <Search size={15} className="pointer-events-none absolute left-3 top-1/2 -translate-y-1/2 text-ink-faint" />
        <input
          value={q}
          onChange={(e) => setQ(e.target.value)}
          placeholder="Search by name"
          className="field-input w-full pl-9"
        />
      </div>

      {error && <ErrorBanner message={error} />}

      {!error && loading && <div className="py-8 text-center text-sm text-ink-faint">Loading patients…</div>}

      {!error && !loading && (
        <div className="flex flex-col gap-2">
          {patients.map((p) => (
            <PatientRow key={p.id} patient={p} onOpen={() => navigate(`/patients/${p.id}`)} />
          ))}
          {patients.length === 0 && (
            <EmptyState
              icon={Search}
              title={q ? "No patients match that search" : "No patients registered yet"}
              hint={canRegister ? "Use Register patient to add the first record." : undefined}
            />
          )}
        </div>
      )}

      {showModal && (
        <RegisterPatientModal
          onClose={() => setShowModal(false)}
          onRegistered={(patient) => {
            setShowModal(false);
            setPatients((prev) => [patient, ...prev]);
            navigate(`/patients/${patient.id}`);
          }}
        />
      )}
    </div>
  );
}
