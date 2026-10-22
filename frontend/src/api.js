const API_BASE_URL = import.meta.env.VITE_API_BASE_URL || "http://localhost:8080";

export async function fetchMessages() {
  const response = await fetch(`${API_BASE_URL}/api/messages`);
  if (!response.ok) throw new Error("Could not load messages.");
  return response.json();
}

export async function createMessage(content){
    const response = await fetch(`${API_BASE_URL}/api/messages`, {
        method: "POST",
        headers: {"Content-Type": "application/json"},
        body: JSON.stringify({ content })
    });

    if(!response.ok){
        const problem = await response.json().catch(() => null);
        throw new Error(problem?.detail || "Could not create the message.");
    }

    return response.json();
}