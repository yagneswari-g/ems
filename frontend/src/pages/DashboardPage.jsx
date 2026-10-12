import React,{ useEffect, useState } from "react";
import { api, errorMessage } from "../api";
import StatCard from "../components/StatCard";
import { Link } from "react-router-dom";

export default function DashboardPage() {
  const [stats, setStats] = useState(null); const [error, setError] = useState("");
  useEffect(() => { api.get("/dashboard/summary").then((r) => setStats(r.data)).catch((e) => setError(errorMessage(e))); }, []);
  if (error) return <div className="alert error">{error}</div>;
  if (!stats) return <div className="loading-card">Loading your dashboard…</div>;
  return <section className="page"><div className="hero-banner"><div><span className="pill light">TEAM OVERVIEW</span><h2>Your people, at a glance.</h2><p>Keep your employee records organized and your team moving forward.</p></div><Link className="primary" to="/employees">View employees →</Link></div><div className="stats-grid"><StatCard label="Total employees" value={stats.totalEmployees} icon="♙" tone="blue" /><StatCard label="Active employees" value={stats.activeEmployees} icon="✓" tone="green" /><StatCard label="On leave" value={stats.onLeaveEmployees} icon="◷" tone="orange" /><StatCard label="Departments" value={stats.totalDepartments} icon="▤" tone="purple" /></div><div className="dashboard-grid"><div className="info-card"><div className="section-title"><div><span className="eyebrow">WORKFORCE HEALTH</span><h3>Current status</h3></div><span className="status-dot">● Live</span></div><div className="health-row"><div className="health-number">{stats.activeEmployees}<small>active teammates</small></div><div className="progress"><div style={{ width: `${stats.totalEmployees ? Math.round(stats.activeEmployees / stats.totalEmployees * 100) : 0}%` }} /></div></div><p className="muted">A quick view of your active workforce. Visit Employees to search, edit, upload documents, or export a report.</p></div><div className="info-card accent-card"><span className="eyebrow">QUICK ACTION</span><h3>Everything in one place.</h3><p>Use the AI assistant whenever you need help navigating your workspace.</p><Link className="soft-button" to="/employees">Manage employees →</Link></div></div></section>;
}
