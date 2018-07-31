package com.ostec.nats_jms_bridge;

import java.util.Locale;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.ImportResource;
import org.springframework.context.support.ResourceBundleMessageSource;
import org.springframework.core.task.TaskExecutor;
import org.springframework.jms.annotation.EnableJms;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.LocaleResolver;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;
import org.springframework.web.servlet.i18n.SessionLocaleResolver;


@SpringBootApplication
@ImportResource({ "classpath:JMSCommsContext.xml" })
@EnableJms
@ComponentScan({ "com.ostec.nats_jms_bridge"})
@EnableScheduling
@EnableAsync
@Configuration
public class Application extends WebMvcConfigurerAdapter {
	
	
	public static void main(String[] args) {
		SpringApplication.run(Application.class, args);
		
	}
	
	
	
	@Bean(name = "createCandleTaskExecutor")
    public TaskExecutor threadPoolTaskExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(300);
        executor.setMaxPoolSize(350);
        executor.setThreadNamePrefix("createCandleTaskExecutor-");
        executor.initialize();
        return executor;
    }

	
	
	@Bean
	public RestTemplate restTemplate(RestTemplateBuilder builder) {
		return builder.build();
	}

	@Bean
	public LocaleResolver localeResolver() {
		SessionLocaleResolver slr = new SessionLocaleResolver();
		slr.setDefaultLocale(Locale.US); // Set default Locale as UK
		return slr;
	}

	@Bean
	public ResourceBundleMessageSource messageSource() {
		ResourceBundleMessageSource source = new ResourceBundleMessageSource();
		source.setBasenames("messages/messages"); // name of the resource bundle
		source.setUseCodeAsDefaultMessage(true);
		return source;
	}
	
	
}
