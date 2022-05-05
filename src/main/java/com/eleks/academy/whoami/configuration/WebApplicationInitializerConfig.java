package com.eleks.academy.whoami.configuration;

import org.springframework.web.WebApplicationInitializer;
import org.springframework.web.context.support.AnnotationConfigWebApplicationContext;
import org.springframework.web.servlet.DispatcherServlet;

import javax.servlet.ServletContext;

public class WebApplicationInitializerConfig implements WebApplicationInitializer {

	@Override
	public void onStartup(ServletContext container) {
		final var webContext = new AnnotationConfigWebApplicationContext();
		webContext.register(WebConfig.class);

		final var registration = container
				.addServlet("dispatcher", new DispatcherServlet(webContext));

		registration.setLoadOnStartup(1);
		registration.addMapping("/");
	}

}
