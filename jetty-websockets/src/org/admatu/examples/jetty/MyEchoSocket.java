package org.admatu.examples.jetty;

import java.io.IOException;

import org.eclipse.jetty.websocket.api.RemoteEndpoint;
import org.eclipse.jetty.websocket.api.Session;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketClose;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketConnect;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketMessage;
import org.eclipse.jetty.websocket.api.annotations.WebSocket;

@WebSocket
public class MyEchoSocket {
	private Session session;
	private RemoteEndpoint remote;

	public RemoteEndpoint getRemote() {
		return remote;
	}

	@OnWebSocketClose
	public void onClose(int statusCode, String reason) {
		this.session = null;
	}

	@OnWebSocketConnect
	public void onConnect(Session session) {
		this.session = session;
		this.remote = session.getRemote();
	}

	@OnWebSocketMessage
	public void onText(String message) {
		if (session == null) {
			// no connection, do nothing.
			// this is possible due to async behavior
			return;
		}

		try {
			remote.sendString(message);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}