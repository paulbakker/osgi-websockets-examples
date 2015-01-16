package org.amdatu.examples.jsr356;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.websocket.DeploymentException;
import javax.websocket.server.ServerContainer;

import org.apache.felix.dm.annotation.api.Component;
import org.apache.felix.dm.annotation.api.ServiceDependency;
import org.apache.felix.dm.annotation.api.Start;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.util.Jetty;
import org.eclipse.jetty.websocket.jsr356.server.deploy.WebSocketServerContainerInitializer;
import org.osgi.service.http.HttpService;
import org.osgi.service.http.NamespaceException;

@Component
public class HelloServer extends HttpServlet{

	@ServiceDependency
	private volatile HttpService m_httpService;
	
	
	
	@Start
	public void start() {
		
		try {
			m_httpService.registerServlet("/", this, null, null);
		} catch (ServletException | NamespaceException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * This really shouldn't be in a get, but I'm not sure how to get a valid ServletContextHandler from any other place
	 * Because of this, the first request will always fail the WS handshake.
	 */
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		
		ServerContainer wscontainer;
		try {
			ServletContextHandler handler = (ServletContextHandler)ServletContextHandler.getContextHandler(getServletContext());
			wscontainer = WebSocketServerContainerInitializer.configureContext(handler);
			
			wscontainer.addEndpoint(HelloEndpoint.class);
			
		} catch (ServletException | DeploymentException e) {
			e.printStackTrace();
		}
	}
	
}
