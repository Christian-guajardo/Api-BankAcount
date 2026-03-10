package com.example.clientAPI.configuration;

import jakarta.ws.rs.ApplicationPath;
import org.glassfish.jersey.server.ResourceConfig;
import org.springframework.context.annotation.Configuration;

@Configuration
@ApplicationPath("/api/v1")
public class JerseyConfig extends ResourceConfig {

  public JerseyConfig() {
    packages("com.example.clientAPI.controller");
    packages("com.example.clientAPI.exception");
  }
}
