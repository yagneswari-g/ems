import React from "react";
import { Navigate, Route, Routes } from "react-router-dom";
import { useAuth } from "./context";
import AuthPage from "./pages/AuthPage";
import DashboardPage from "./pages/DashboardPage";
import EmployeesPage from "./pages/EmployeesPage";
import DepartmentsPage from "./pages/DepartmentsPage";
import Layout from "./components/Layout";

function Protected({ children }) { const { user, loading } = useAuth(); if (loading) return <div className="center-screen">Loading EmployeeHub…</div>; return user ? children : <Navigate to="/auth" replace />; }

export default function App() {
  return <Routes>
    <Route path="/auth" element={<AuthPage />} />
    <Route path="/" element={<Protected><Layout /></Protected>}>
      <Route index element={<DashboardPage />} />
      <Route path="employees" element={<EmployeesPage />} />
      <Route path="departments" element={<DepartmentsPage />} />
    </Route>
    <Route path="*" element={<Navigate to="/" replace />} />
  </Routes>;
}
