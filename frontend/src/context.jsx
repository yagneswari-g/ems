import React,{ createContext, useContext, useEffect, useMemo, useState } from "react";
import { api } from "./api";

const AuthContext = createContext(null);
const ThemeContext = createContext(null);

export function AuthProvider({ children }) {
  const [user, setUser] = useState(() => JSON.parse(localStorage.getItem("employeehub_user") || "null"));
  const [loading, setLoading] = useState(Boolean(localStorage.getItem("employeehub_token")));
  useEffect(() => { if (localStorage.getItem("employeehub_token")) api.get("/auth/me").then((r) => setUser(r.data)).catch(() => logout()).finally(() => setLoading(false)); }, []);
  async function login(values) { const { data } = await api.post("/auth/login", values); save(data); }
  async function register(values) { const { data } = await api.post("/auth/register", values); save(data); }
  async function logout() { try { await api.post("/auth/logout"); } catch {} localStorage.removeItem("employeehub_token"); localStorage.removeItem("employeehub_user"); setUser(null); }
  function save(data) { localStorage.setItem("employeehub_token", data.token); localStorage.setItem("employeehub_user", JSON.stringify(data)); setUser(data); }
  const value = useMemo(() => ({ user, loading, login, register, logout }), [user, loading]);
  return <AuthContext.Provider value={value}>{children}</AuthContext.Provider>;
}

export function ThemeProvider({ children }) {
  const [dark, setDark] = useState(() => localStorage.getItem("employeehub_theme") === "dark");
  useEffect(() => { document.documentElement.dataset.theme = dark ? "dark" : "light"; localStorage.setItem("employeehub_theme", dark ? "dark" : "light"); }, [dark]);
  return <ThemeContext.Provider value={{ dark, toggle: () => setDark((v) => !v) }}>{children}</ThemeContext.Provider>;
}
export const useAuth = () => useContext(AuthContext);
export const useTheme = () => useContext(ThemeContext);
