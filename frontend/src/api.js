import axios from "axios";

export const API_URL = import.meta.env.VITE_API_URL || "http://localhost:8080/api";
export const api = axios.create({ baseURL: API_URL });

api.interceptors.request.use((config) => {
  const token = localStorage.getItem("employeehub_token");
  if (token) config.headers["X-Auth-Token"] = token;
  return config;
});

export function errorMessage(error) {
  return error?.response?.data?.message || "Something went wrong. Please try again.";
}

export async function downloadFile(path, filename) {
  const response = await api.get(path, { responseType: "blob" });
  const url = URL.createObjectURL(response.data);
  const link = document.createElement("a"); link.href = url; link.download = filename; link.click(); URL.revokeObjectURL(url);
}
