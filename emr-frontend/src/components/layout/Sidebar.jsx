import React from "react";
import { NavLink } from "react-router-dom";
import { Activity, Users, Calendar, Shield, Stethoscope, LogOut } from "lucide-react";
import { useAuth } from "../../context/AuthContext";
import { roleLabel } from "../../utils/enums";

const NAV_ITEMS = [
  { to: "/", label: "Dashboard", icon: Activity, roles: ["ADMIN", "DOCTOR", "FRONT_DESK"] },
  { to: "/patients", label: "Patients", icon: Users, roles: ["ADMIN", "DOCTOR", "FRONT_DESK"] },
  { to: "/appointments", label: "Appointments", icon: Calendar, roles: ["ADMIN", "DOCTOR", "FRONT_DESK"] },
  { to: "/staff", label: "Staff", icon: Shield, roles: ["ADMIN"] },
];

export function Sidebar() {
  const { user, logout } = useAuth();
  const items = NAV_ITEMS.filter((n) => n.roles.includes(user.role));

  return (
    <div className="flex w-56 flex-shrink-0 flex-col border-r border-border bg-white px-3 py-5">
      <div className="mb-6 flex items-center gap-2 px-2">
        <div className="flex h-8 w-8 items-center justify-center rounded-lg bg-teal">
          <Stethoscope size={16} className="text-white" />
        </div>
        <div>
          <div className="font-display text-[15px] leading-tight text-ink">Records EMR</div>
          <div className="text-[10px] text-ink-faint">Clinic system</div>
        </div>
      </div>

      <nav className="flex flex-col gap-0.5">
        {items.map((item) => (
          <NavLink
            key={item.to}
            to={item.to}
            end={item.to === "/"}
            className={({ isActive }) =>
              `flex items-center gap-3 rounded-md px-3 py-2.5 text-sm transition-colors ${
                isActive
                  ? "bg-teal-tint font-semibold text-teal-dark"
                  : "font-medium text-ink-soft hover:bg-paper"
              }`
            }
          >
            <item.icon size={17} />
            {item.label}
          </NavLink>
        ))}
      </nav>

      <div className="mt-auto border-t border-border px-2 pt-3">
        <div className="text-[11px] text-ink-faint">Signed in as</div>
        <div className="text-sm font-semibold text-ink">{user.username}</div>
        <div className="text-xs text-ink-soft">{roleLabel(user.role)}</div>
        <button
          onClick={logout}
          className="mt-3 flex items-center gap-2 text-xs font-semibold text-ink-soft transition-colors hover:text-rust"
        >
          <LogOut size={13} />
          Sign out
        </button>
      </div>
    </div>
  );
}
