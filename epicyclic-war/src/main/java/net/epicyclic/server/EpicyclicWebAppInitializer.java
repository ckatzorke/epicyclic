package net.epicyclic.server;

import javax.servlet.ServletContext;
import javax.servlet.ServletRegistration;

import org.springframework.web.WebApplicationInitializer;
import org.springframework.web.context.ContextLoaderListener;
import org.springframework.web.context.support.AnnotationConfigWebApplicationContext;
import org.springframework.web.servlet.DispatcherServlet;

public class EpicyclicWebAppInitializer implements WebApplicationInitializer {

	@Override
	public void onStartup(ServletContext servletContext) {
		// Create the 'root' Spring application context
		AnnotationConfigWebApplicationContext appCtx = new AnnotationConfigWebApplicationContext();
		appCtx.scan("net.epicyclic.server", "net.epicyclic.portletcontainer", "net.epicyclic.portletinvocation");

		// Manages the lifecycle of the root application context
		servletContext.addListener(new ContextLoaderListener(appCtx));

		ServletRegistration.Dynamic epicyclicRestServlet = servletContext.addServlet("epicyclicRestServlet",
				new DispatcherServlet(appCtx));
		epicyclicRestServlet.setLoadOnStartup(1);
		epicyclicRestServlet.addMapping("/");
	}
}