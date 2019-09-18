package com.sample.app;

import com.github.apuex.springbootsolution.runtime.*;
import com.google.protobuf.*;
import com.google.protobuf.util.*;
import com.sample.message.*;

import org.springframework.boot.*;
import org.springframework.boot.autoconfigure.*;
import org.springframework.boot.builder.*;
import org.springframework.boot.web.servlet.support.*;
import org.springframework.context.annotation.*;
import org.springframework.http.converter.protobuf.*;

import java.util.*;

@Configuration
@ComponentScan({"com.sample.*"})
@ImportResource("classpath:app-config.xml")
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
}
    