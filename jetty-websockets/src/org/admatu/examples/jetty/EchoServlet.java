package org.admatu.examples.jetty;

import java.util.Arrays;

import javax.servlet.ServletException;

import org.apache.felix.dm.annotation.api.Component;
import org.apache.felix.dm.annotation.api.Inject;
import org.apache.felix.dm.annotation.api.Property;
import org.apache.felix.dm.annotation.api.ServiceDependency;
import org.apache.felix.dm.annotation.api.Start;
import org.eclipse.jetty.websocket.servlet.WebSocketServlet;
import org.eclipse.jetty.websocket.servlet.WebSocketServletFactory;
import org.osgi.framework.Bundle;
import org.osgi.framework.BundleContext;
import org.osgi.framework.wiring.BundleWiring;
import org.osgi.service.http.HttpService;
import org.osgi.service.http.NamespaceException;

@Component
public class EchoServlet extends WebSocketServlet {

	@ServiceDependency
	private volatile HttpService m_httpService;
	
	@Inject
	private volatile BundleContext m_bundleContext;
	
	@Start
	public void start()  {
		try {
			BundleWiring bundleWiring = findJettyBundle().adapt(BundleWiring.class);
			ClassLoader classLoader = bundleWiring.getClassLoader();
			
			Thread.currentThread().setContextClassLoader(classLoader);
			m_httpService.registerServlet("/echo", this, null, null);
		} catch (ServletException | NamespaceException e) {
			e.printStackTrace();
		}
	}
	
	private Bundle findJettyBundle() {
		return Arrays.stream(m_bundleContext.getBundles()).filter(b -> b.getSymbolicName().equals("org.apache.felix.http.jetty")).findAny().get();
	}
	
	@Override
	public void configure(WebSocketServletFactory factory) {
		factory.getPolicy().setIdleTimeout(10000);
		factory.register(MyEchoSocket.class);
	}
}
