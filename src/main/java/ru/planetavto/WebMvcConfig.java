package ru.planetavto;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

@Configuration
public class WebMvcConfig implements WebMvcConfigurer {	
	
	private HandlerInterceptorAdapter viewNameInModelInterceptor;
	
	@Autowired
	public WebMvcConfig(HandlerInterceptorAdapter viewNameInModelInterceptor) {
		this.viewNameInModelInterceptor = viewNameInModelInterceptor;
	}

	@Override
	public void addInterceptors(InterceptorRegistry registry) {
		registry.addInterceptor(viewNameInModelInterceptor);
	}
	
	@Override
	  public void addViewControllers(ViewControllerRegistry registry) {
	    registry.addViewController("/login");
	  }
}
