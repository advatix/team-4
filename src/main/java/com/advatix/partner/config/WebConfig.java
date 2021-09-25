package com.advatix.partner.config;

import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import com.advatix.partner.PartnerSelectionServiceApplication;
import com.advatix.partner.commons.logger.Logger;




@Configuration
@EnableWebMvc
public class WebConfig implements WebMvcConfigurer {
	


	  /** The logging interceptor. */
		/*
		 * @Autowired private LoggingInterceptor loggingInterceptor;
		 */
	
	private static final Logger log = Logger.getLogger(WebConfig.class);
	
	@Override
	public void addResourceHandlers(ResourceHandlerRegistry registry) {
	    registry.addResourceHandler("swagger-ui.html")
	        .addResourceLocations("classpath:/META-INF/resources/");

	    registry.addResourceHandler("/webjars/**")
	        .addResourceLocations("classpath:/META-INF/resources/webjars/");
	  }

	
	
	@Bean
	  public MessageSource messageSource() {
	    ReloadableResourceBundleMessageSource messageSource =
	        new ReloadableResourceBundleMessageSource();
	    String configLocation =
	        System.getProperty(PartnerSelectionServiceApplication.PROPERTIES_LOCATION_ENV, "classpath:");
	    String configPath = configLocation + PartnerSelectionServiceApplication.ERROR_MESSAGES_PROPERTY;
	    log.info(configPath);
	    messageSource.setBasename(configPath);
	    messageSource.setDefaultEncoding("UTF-8");
	    return messageSource;
	  }
	
	
}
