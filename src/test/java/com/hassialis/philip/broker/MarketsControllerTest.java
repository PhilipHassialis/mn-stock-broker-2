package com.hassialis.philip.broker;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.LinkedHashMap;
import java.util.List;

import org.junit.jupiter.api.Test;

import io.micronaut.http.client.HttpClient;
import io.micronaut.http.client.annotation.Client;
import io.micronaut.runtime.EmbeddedApplication;
import io.micronaut.test.extensions.junit5.annotation.MicronautTest;
import jakarta.inject.Inject;

import static org.assertj.core.api.Assertions.assertThat;

@MicronautTest
public class MarketsControllerTest {

  @Inject
  EmbeddedApplication<?> application;

  @Inject
  @Client("/")
  HttpClient client;

  @Test
  void returnsListOfMarkets() {
    final List<LinkedHashMap<String, String>> result = client.toBlocking().retrieve("/markets", List.class);
    assertEquals(10, result.size());
    // assertThat(result)
    // .extracting(entry -> entry.get("value"))
    // .containsExactlyInAnyOrder("AAPL", "AMZN", "META", "GOOG", "MSFT", "NFLX");
  }

}