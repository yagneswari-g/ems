import React,{ useEffect, useState } from "react";
import { api, errorMessage } from "../api";
import { useAuth } from "../context";

export default function DepartmentsPage() {
  const { user } = useAuth(); const [items, setItems] = useState([]); const [form, setForm] = useState({ name: "", description: "" }); const [error, setError] = useState("");
  function load() { api.get("/departments").then((r) => setItems(r.data)).catch((e) => setError(errorMessage(e))); } useEffect(load, []);
  async function add(e) { e.preventDefault(); try { await api.post("/departments", form); setForm({ name: "", description: "" }); load(); } catch (err) { setError(errorMessage(err)); } }
  async function remove(id) { if (!confirm("Delete this department?")) return; try { await api.delete(`/departments/${id}`); load(); } catch (err) { setError(errorMessage(err)); } }
  const manager = user?.role === "ADMIN" || user?.role === "HR";
  return <section className="page"><div className="page-heading"><div><span className="eyebrow">ORGANIZATION</span><h2>Departments</h2><p className="muted">Keep your team structure easy to understand.</p></div></div>{error && <div className="alert error">{error}</div>}<div className="two-column">{manager && <form className="form-card" onSubmit={add}><h3>Add department</h3><label>Department name<input value={form.name} onChange={(e) => setForm({ ...form, name: e.target.value })} placeholder="Engineering" required /></label><label>Description<textarea value={form.description} onChange={(e) => setForm({ ...form, description: e.target.value })} placeholder="What this team does" /></label><button className="primary">Add department</button></form>}<div className="list-card"><div className="section-title"><h3>All departments</h3><span className="count-badge">{items.length}</span></div>{items.map((d) => <div className="department-row" key={d.id}><div className="department-icon">{d.name[0]}</div><div><strong>{d.name}</strong><small>{d.description || "No description"}</small></div><span className="employee-count">{d.employeeCount} people</span>{manager && <button className="icon-button danger-text" onClick={() => remove(d.id)}>×</button>}</div>)}{!items.length && <p className="muted">No departments yet.</p>}</div></div></section>;
}
