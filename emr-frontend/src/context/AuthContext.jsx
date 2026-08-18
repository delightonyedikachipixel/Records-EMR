import React, { createContext, useContext, useEffect, useState } from "react";
import { login as loginRequest } from "../api/auth";
import { getToken, setToken as persistToken } from "../api/client";
import { decodeJwt } from "../utils/jwt";

const AuthContext = createContext(null);

const USER_KEY = "emr_user";

function loadStoredUser() {
  const raw = localStorage.getItem(USER_KEY);
  if (!raw) return null;
  try {
    return JSON.parse(raw);
  } catch {
    return null;
  }
}

export function AuthProvider({ children }) {
  const [user, setUser] = useState(loadStoredUser);
  const [ready, setReady] = useState(false);

  useEffect(() => {
    const token = getToken();
    if (token) {
      const claims = decodeJwt(token);
      if (!claims || (claims.exp && claims.exp * 1000 < Date.now())) {
        persistToken(null);
        localStorage.removeItem(USER_KEY);
        setUser(null);
      }
    }
    setReady(true);
  }, []);

  async function login(username, password) {
    const res = await loginRequest(username, password);
    const claims = decodeJwt(res.token);
    const nextUser = {
      username: res.username,
      role: res.role,
      userId: claims?.userId || null,
    };
    persistToken(res.token);
    localStorage.setItem(USER_KEY, JSON.stringify(nextUser));
    setUser(nextUser);
    return nextUser;
  }

  function logout() {
    persistToken(null);
    localStorage.removeItem(USER_KEY);
    setUser(null);
  }

  return (
    <AuthContext.Provider value={{ user, ready, login, logout }}>
      {children}
    </AuthContext.Provider>
  );
}

export function useAuth() {
  const ctx = useContext(AuthContext);
  if (!ctx) throw new Error("useAuth must be used inside AuthProvider");
  return ctx;
}
