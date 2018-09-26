package org.pl.blockchain.web;

import java.util.Locale;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.LocaleResolver;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurationSupport;
import org.springframework.web.servlet.i18n.LocaleChangeInterceptor;
import org.springframework.web.servlet.i18n.SessionLocaleResolver;

@Configuration
@EnableAutoConfiguration
@ComponentScan
public class App extends WebMvcConfigurationSupport
{
    public static void main( String[] args )
    {	
    	SpringApplication.run(App.class, args);
    }
    
    @Bean  
    public LocaleResolver localeResolver() {  
        SessionLocaleResolver slr = new SessionLocaleResolver();  
        slr.setDefaultLocale(Locale.SIMPLIFIED_CHINESE);  
        return slr;  
    }  
  
    @Bean  
    public LocaleChangeInterceptor localeChangeInterceptor() {  
        LocaleChangeInterceptor lci = new LocaleChangeInterceptor();  
        lci.setParamName("lang");  
        return lci;  
    }  
  
    @Override  
    public void addInterceptors(InterceptorRegistry registry) {  
        registry.addInterceptor(localeChangeInterceptor());  
    }  
}
