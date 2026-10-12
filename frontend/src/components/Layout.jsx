import React from "react";
import { NavLink, Outlet } from "react-router-dom";
import { useAuth, useTheme } from "../context";
import Chatbot from "./Chatbot";

export default function Layout() {
  const { user, logout } = useAuth(); const { dark, toggle } = useTheme();
  return <div className="app-shell">
    <aside className="sidebar"><div className="brand"><span className="brand-mark">E</span><span>Employee<span className="brand-accent">Hub</span></span></div>
      <nav><NavLink to="/" end>▦ <span>Dashboard</span></NavLink><NavLink to="/employees">♙ <span>Employees</span></NavLink><NavLink to="/departments">▤ <span>Departments</span></NavLink></nav>
      <div className="sidebar-foot"><div className="mini-note">Manage people.<br />Grow together.</div><button className="text-button" onClick={logout}>↪ Sign out</button></div>
    </aside>
    <main className="main"><header className="topbar"><div><p className="eyebrow">EMPLOYEE MANAGEMENT SYSTEM</p><h1>Good to see you, {user?.fullName?.split(" ")[0] || "there"}.</h1></div><div className="top-actions"><button className="theme-button" onClick={toggle}>{dark ? "☀ Light" : "☾ Dark"}</button><div className="user-chip"><span className="avatar">{user?.fullName?.[0] || "U"}</span><span><strong>{user?.fullName}</strong><small>{user?.role}</small></span></div></div></header><Outlet /></main><Chatbot />
  </div>;
}
