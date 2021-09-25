package com.advatix.partner;

import java.util.Arrays;
import java.util.List;
import java.util.Properties;

import org.apache.commons.lang3.StringUtils;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.PropertySource;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.data.web.config.EnableSpringDataWebSupport;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

import springfox.documentation.swagger2.annotations.EnableSwagger2;



@SpringBootApplication
@EnableAutoConfiguration
@ComponentScan("com.advatix.partner")
@EnableSwagger2
@EnableAsync
@PropertySource(ignoreResourceNotFound = true, value = {
		"file:${" + PartnerSelectionServiceApplication.PROPERTIES_LOCATION_ENV + "}/" + PartnerSelectionServiceApplication.APPLICATION_PROPERTY
				+ ".properties",
		"file:${" + PartnerSelectionServiceApplication.PROPERTIES_LOCATION_ENV + "}/" + PartnerSelectionServiceApplication.ERROR_MESSAGES_PROPERTY
				+ ".properties" })
@EnableJpaRepositories
@EnableWebMvc
@EnableSpringDataWebSupport
public class PartnerSelectionServiceApplication {
	
	public static final String PROPERTIES_LOCATION_ENV = "spring.config.location";
	public static final String APPLICATION_PROPERTY = "PartnerSelection-application";
	public static final String ERROR_MESSAGES_PROPERTY = "PartnerSelection-error-message";


	protected static final List<String> PROPERTY_FILES = Arrays.asList(APPLICATION_PROPERTY, ERROR_MESSAGES_PROPERTY);
	public static final String PROPERTIES_FILE_NAME = String.join(",", PROPERTY_FILES);

	public static void main(String[] args) {
		String configLocation = System.getProperty(PartnerSelectionServiceApplication.PROPERTIES_LOCATION_ENV, "classpath:/");
		String configPath = configLocation + " - " + PartnerSelectionServiceApplication.PROPERTIES_FILE_NAME;
		// log.info("Configpath: {}", configPath);
		if (StringUtils.isNotBlank(configLocation)) {
			Properties props = new Properties();
			props.setProperty(PartnerSelectionServiceApplication.PROPERTIES_LOCATION_ENV, configLocation);
			props.setProperty("spring.config.name", PartnerSelectionServiceApplication.PROPERTIES_FILE_NAME);
			ConfigurableApplicationContext applicationContext = new SpringApplicationBuilder(PartnerSelectionServiceApplication.class)
					.properties(props).build().run(args);
			applicationContext.getEnvironment();
		} else {
			SpringApplication.run(PartnerSelectionServiceApplication.class, args);
		}
	}

}
