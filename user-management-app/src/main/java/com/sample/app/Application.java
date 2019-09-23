package com.sample.app;

import com.github.apuex.springbootsolution.runtime.Messages;
import com.google.protobuf.Descriptors;
import com.google.protobuf.util.JsonFormat;
import com.sample.controller.CaptchaFilter;
import com.sample.message.UserManagement;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.ImportResource;
import org.springframework.http.converter.protobuf.ProtobufHttpMessageConverter;
import org.springframework.http.converter.protobuf.ProtobufJsonFormatHttpMessageConverter;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;

import java.util.HashSet;
import java.util.Set;

@Configuration
@ComponentScan({"com.sample.*"})
@ImportResource("classpath:app-config.xml")
@EnableWebSecurity
@SpringBootApplication
public class Application extends SpringBootServletInitializer {

  public static void main(String[] args) {
    SpringApplication.run(Application.class, args);
  }

  @Bean
  ProtobufHttpMessageConverter protobufHttpMessageConverter() throws Exception {
    JsonFormat.TypeRegistry registry = JsonFormat.TypeRegistry.newBuilder()
        .add(UserManagement.getDescriptor().getMessageTypes())
        .add(Messages.getDescriptor().getMessageTypes())
        .build();
    Set<Descriptors.FieldDescriptor> fieldsToAlwaysOutput = new HashSet<>();
    UserManagement.getDescriptor().getMessageTypes()
        .forEach(t -> fieldsToAlwaysOutput.addAll(t.getFields()));
    JsonFormat.Printer printer = JsonFormat.printer()
        .usingTypeRegistry(registry)
        .includingDefaultValueFields(fieldsToAlwaysOutput);

    JsonFormat.Parser parser = JsonFormat.parser().usingTypeRegistry(registry);
    return new ProtobufJsonFormatHttpMessageConverter(parser, printer);
  }

  @Override
  protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
    return application.sources(Application.class);
  }

//  @Bean
//  public FilterRegistrationBean<CaptchaFilter> loggingFilter(){
//    FilterRegistrationBean<CaptchaFilter> registrationBean
//            = new FilterRegistrationBean<>();
//
//    registrationBean.setFilter(new CaptchaFilter());
//    registrationBean.addUrlPatterns("/login");
//    registrationBean.setOrder(0);
//
//    return registrationBean;
//  }
}
    