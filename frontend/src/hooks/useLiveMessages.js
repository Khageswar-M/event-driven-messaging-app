import { useCallback, useEffect, useState } from "react";
import { Client } from "@stomp/stompjs";
import { fetchMessages } from "../api.js";

const WS_URL = import.meta.env.VITE_WS_URL || "ws://localhost:8080/ws";

export function useLiveMessages() {
  const [messages, setMessages] = useState([]);
  const [connection, setConnection] = useState("connecting");
  const [error, setError] = useState("");

  const refreshSnapshot = useCallback(async () => {
    try {
      const latest = await fetchMessages();
      setMessages((current) => {
        const byId = new Map(current.map((message) => [message.id, message]));
        latest.forEach((message) => byId.set(message.id, message));
        return [...byId.values()].sort(
          (a, b) => new Date(b.createdAt) - new Date(a.createdAt)
        );
      });
      setError("");
    } catch (err) {
      setError(err.message || "Unable to load messages.");
    }
  }, []);

  useEffect(() => {
    let active = true;

    const client = new Client({
      brokerURL: WS_URL,
      reconnectDelay: 3000,
      heartbeatIncoming: 10000,
      heartbeatOutgoing: 10000,
      onConnect: () => {
        if (!active) return;
        setConnection("connected");

        client.subscribe("/topic/messages", (frame) => {
          try {
            const incoming = JSON.parse(frame.body);
            setMessages((current) => {
              const byId = new Map(current.map((message) => [message.id, message]));
              byId.set(incoming.id, incoming);
              return [...byId.values()].sort(
                (a, b) => new Date(b.createdAt) - new Date(a.createdAt)
              );
            });
          } catch {
            setError("Received an unreadable message event.");
          }
        });

        // One snapshot per successful connection/reconnection; not polling.
        void refreshSnapshot();
      },
      onWebSocketClose: () => {
        if (active) setConnection("disconnected");
      },
      onWebSocketError: () => {
        if (active) setConnection("disconnected");
      },
      onStompError: (frame) => {
        if (active) setError(frame.headers?.message || "WebSocket broker error.");
      }
    });

    client.activate();

    return () => {
      active = false;
      void client.deactivate();
    };
  }, [refreshSnapshot]);

  return { messages, connection, error, refreshSnapshot };
}
