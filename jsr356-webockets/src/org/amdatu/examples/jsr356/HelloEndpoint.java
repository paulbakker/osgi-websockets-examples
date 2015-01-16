package org.amdatu.examples.jsr356;

import javax.websocket.OnMessage;
import javax.websocket.server.ServerEndpoint;

@ServerEndpoint("/echo")
public class HelloEndpoint {
	@OnMessage
	public String handleMessage(String message) {
	 return "Got your message (" + message + "). Thanks !";
	 }
}
