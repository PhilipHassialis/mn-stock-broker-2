package com.hassialis.philip;

import static org.junit.jupiter.api.Assertions.assertEquals;

import io.micronaut.http.client.HttpClient;
import io.micronaut.http.client.annotation.Client;
import jakarta.inject.Inject;

import io.micronaut.test.extensions.junit5.annotation.MicronautTest;
import org.junit.jupiter.api.Test;

@MicronautTest
public class HelloWorldControllerTest {

  @Inject
  @Client("/")
  HttpClient client;

  @Test
  void helloWorldEndpointResponds() {
    var response = client.toBlocking().retrieve("/hello");
    assertEquals("Hello from service!", response);
  }

}
