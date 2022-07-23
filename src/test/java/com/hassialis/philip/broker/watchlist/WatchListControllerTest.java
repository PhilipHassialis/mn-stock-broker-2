package com.hassialis.philip.broker.watchlist;

import java.util.UUID;
import java.util.stream.Stream;

import com.fasterxml.jackson.databind.JsonNode;
import io.micronaut.http.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.hassialis.philip.broker.data.InMemoryAccountStore;
import com.hassialis.philip.broker.model.Symbol;

import io.micronaut.http.client.HttpClient;
import io.micronaut.http.client.annotation.Client;
import io.micronaut.test.extensions.junit5.annotation.MicronautTest;
import jakarta.inject.Inject;

import static org.junit.jupiter.api.Assertions.*;

@MicronautTest
class WatchListControllerTest {

  private static final Logger LOG = LoggerFactory.getLogger(WatchListControllerTest.class);
  private static final UUID TEST_ACCOUNT_ID = WatchListController.ACCOUNT_ID;

  @Inject
  @Client("/account/watchlist")
  HttpClient client;

  @Inject
  InMemoryAccountStore inMemoryAccountStore;

  @BeforeEach
  void setup() {
    inMemoryAccountStore.deleteWatchList(TEST_ACCOUNT_ID);
  }

  private void givenWatchListForAccountExists() {
    inMemoryAccountStore.updateWatchList(TEST_ACCOUNT_ID, new WatchList(
        Stream.of("AAPL", "MSFT", "GOOG").map(Symbol::new).toList()));
  }

  @Test
  void returnsEmptyWatchListForTestAccount() {
    final WatchList result = client.toBlocking().retrieve("/", WatchList.class);
    assertNull(result.symbols());
    assertTrue(inMemoryAccountStore.getWatchList(TEST_ACCOUNT_ID).symbols().isEmpty());
    LOG.debug("returnsEmptyWatchListForTestAccount {}", result);
  }

  @Test
  void returnsWatchListForTestAccount() {
    givenWatchListForAccountExists();

    var response = client.toBlocking().exchange("/", JsonNode.class);
    assertEquals(HttpStatus.OK, response.getStatus());
    assertEquals("{\"symbols\":[{\"value\":\"AAPL\"},{\"value\":\"MSFT\"},{\"value\":\"GOOG\"}]}",
        response.getBody().get().toString());
  }

  @Test
  void canUpdateWatchListForTestAccount() {
    var symbols = Stream.of("AAPL", "MSFT", "GOOG").map(Symbol::new).toList();
    var request = HttpRequest.PUT("/", new WatchList(symbols)).accept(MediaType.APPLICATION_JSON);
    final HttpResponse<Object> response = client.toBlocking().exchange(request);
    assertEquals(HttpStatus.OK, response.getStatus());
    assertEquals(symbols, inMemoryAccountStore.getWatchList(TEST_ACCOUNT_ID).symbols());

  }

  @Test
  void canDeleteWatchListForTestAccount() {
    givenWatchListForAccountExists();
    assertFalse(inMemoryAccountStore.getWatchList(TEST_ACCOUNT_ID).symbols().isEmpty());

    var response = client.toBlocking().exchange(HttpRequest.DELETE("/"));
    assertEquals(HttpStatus.NO_CONTENT, response.getStatus());
  }

}