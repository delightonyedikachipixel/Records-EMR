import React, { useState } from "react";
import { Navigate, useLocation, useNavigate } from "react-router-dom";
import { Stethoscope, LogIn } from "lucide-react";
import { useAuth } from "../context/AuthContext";
import { ErrorBanner } from "../components/common/ErrorBanner";
import { Field } from "../components/common/Field";

export function LoginPage() {
  const { user, login } = useAuth();
  const navigate = useNavigate();
  const location = useLocation();
  const [username, setUsername] = useState("");
  const [password, setPassword] = useState("");
  const [error, setError] = useState(null);
  const [loading, setLoading] = useState(false);

  if (user) {
    const dest = location.state?.from?.pathname || "/";
    return <Navigate to={dest} replace />;
  }

  async function handleSubmit(e) {
    e.preventDefault();
    setError(null);
    setLoading(true);
    try {
      await login(username, password);
      navigate("/", { replace: true });
    } catch (err) {
      setError(err.message || "Login failed");
    } finally {
      setLoading(false);
    }
  }

  return (
    <div className="flex min-h-screen items-center justify-center bg-paper px-4">
      <div className="w-full max-w-sm">
        <div className="mb-7 flex flex-col items-center text-center">
          <div className="mb-3 flex h-11 w-11 items-center justify-center rounded-xl bg-teal">
            <Stethoscope size={20} className="text-white" />
          </div>
          <div className="font-display text-2xl text-ink">Records EMR</div>
          <div className="mt-1 text-sm text-ink-soft">Sign in with your staff account</div>
        </div>

        <form onSubmit={handleSubmit} className="card flex flex-col gap-4 p-6">
          {error && <ErrorBanner message={error} />}
          <Field label="Username">
            <input
              className="field-input"
              value={username}
              onChange={(e) => setUsername(e.target.value)}
              autoComplete="username"
              required
            />
          </Field>
          <Field label="Password">
            <input
              type="password"
              className="field-input"
              value={password}
              onChange={(e) => setPassword(e.target.value)}
              autoComplete="current-password"
              required
            />
          </Field>
          <button type="submit" disabled={loading} className="btn-primary justify-center">
            <LogIn size={15} />
            {loading ? "Signing in…" : "Sign in"}
          </button>
        </form>

        <div className="mt-4 text-center text-xs text-ink-faint">
          First time running the backend? The seeded default admin is{" "}
          <span className="font-mono">admin</span> / <span className="font-mono">12345678</span>.
        </div>
      </div>
    </div>
  );
}
