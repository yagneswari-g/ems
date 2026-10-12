import React,{ useState } from "react";
import { api, errorMessage } from "../api";

export default function Chatbot() {
  const [open, setOpen] = useState(false); const [message, setMessage] = useState(""); const [messages, setMessages] = useState([{ from: "bot", text: "Hi! I’m your EmployeeHub assistant. How can I help?" }]); const [busy, setBusy] = useState(false);
  async function send(event) { event.preventDefault(); if (!message.trim() || busy) return; const text = message.trim(); setMessage(""); setMessages((m) => [...m, { from: "user", text }]); setBusy(true); try { const { data } = await api.post("/chat", { message: text }); setMessages((m) => [...m, { from: "bot", text: data.reply }]); } catch (e) { setMessages((m) => [...m, { from: "bot", text: errorMessage(e) }]); } finally { setBusy(false); } }
  return <div className="chat-wrap"><button className="chat-launch" onClick={() => setOpen(!open)}>✦</button>{open && <section className="chat-panel"><div className="chat-head"><div><strong>EmployeeHub AI</strong><small>Gemini assistant</small></div><button onClick={() => setOpen(false)}>×</button></div><div className="chat-messages">{messages.map((m, i) => <div className={`bubble ${m.from}`} key={i}>{m.text}</div>)}{busy && <div className="bubble bot">Thinking…</div>}</div><form onSubmit={send} className="chat-form"><input value={message} onChange={(e) => setMessage(e.target.value)} placeholder="Ask about the app…" /><button>➤</button></form></section>}</div>;
}
