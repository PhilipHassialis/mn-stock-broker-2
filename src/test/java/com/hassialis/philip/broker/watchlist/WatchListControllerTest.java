package com.hassialis.philip.broker.watchlist;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

import java.util.UUID;
import java.util.stream.Stream;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.databind.JsonNode;
import com.hassialis.philip.auth.jwt.AuthenticationProviderUserPassword;
import com.hassialis.philip.broker.data.InMemoryAccountStore;
import com.hassialis.philip.broker.model.Symbol;

import io.micronaut.http.HttpRequest;
import io.micronaut.http.HttpStatus;
import io.micronaut.http.MediaType;
import io.micronaut.http.client.HttpClient;
import io.micronaut.http.client.annotation.Client;
import io.micronaut.http.client.exceptions.HttpClientResponseException;
import io.micronaut.security.authentication.UsernamePasswordCredentials;
import io.micronaut.security.token.jwt.render.BearerAccessRefreshToken;
import io.micronaut.test.extensions.junit5.annotation.MicronautTest;
import jakarta.inject.Inject;

@MicronautTest
class WatchListControllerTest {

  private static final Logger LOG = LoggerFactory.getLogger(WatchListControllerTest.class);
  private static final UUID TEST_ACCOUNT_ID = WatchListController.ACCOUNT_ID;

  @Inject
  @Client("/")
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
  void unauthorizedAccessIsForbidden() {
    try {
      client.toBlocking().exchange(HttpRequest.GET("/account/watchlist"), JsonNode.class);
      fail("Should fail if no exception is thrown");

    } catch (HttpClientResponseException e) {
      assertEquals(HttpStatus.UNAUTHORIZED, e.getStatus());
    }
  }

  @Test
  void returnsEmptyWatchListForTestAccount() {

    final BearerAccessRefreshToken token = givenUserIsLoggedIn();

    var request = HttpRequest.GET("/account/watchlist").accept(MediaType.APPLICATION_JSON)
        .bearerAuth(token.getAccessToken());

    final WatchList result = client.toBlocking().retrieve(request, WatchList.class);
    assertNull(result.symbols());
    assertTrue(inMemoryAccountStore.getWatchList(TEST_ACCOUNT_ID).symbols().isEmpty());
    LOG.debug("returnsEmptyWatchListForTestAccount {}", result);
  }

  private BearerAccessRefreshToken givenUserIsLoggedIn() {
    final UsernamePasswordCredentials credentials = new UsernamePasswordCredentials(
        AuthenticationProviderUserPassword.USERNAME, AuthenticationProviderUserPassword.PASSWORD);
    var login = HttpRequest.POST("/login", credentials);
    var response = client.toBlocking().exchange(login, BearerAccessRefreshToken.class);
    assertEquals(HttpStatus.OK, response.getStatus());
    final BearerAccessRefreshToken token = response.body();
    assertNotNull(token);
    assertEquals(AuthenticationProviderUserPassword.USERNAME, token.getUsername());
    LOG.debug("Token {} expires in {}", token.getAccessToken(), token.getExpiresIn());
    return token;
  }

  @Test
  void returnsWatchListForTestAccount() {
    givenWatchListForAccountExists();

    final BearerAccessRefreshToken token = givenUserIsLoggedIn();
    var request = HttpRequest.GET("/account/watchlist").accept(MediaType.APPLICATION_JSON)
        .bearerAuth(token.getAccessToken());

    var response = client.toBlocking().exchange(request, JsonNode.class);
    assertEquals(HttpStatus.OK, response.getStatus());
    assertEquals("{\"symbols\":[{\"value\":\"AAPL\"},{\"value\":\"MSFT\"},{\"value\":\"GOOG\"}]}",
        response.getBody().get().toString());
  }

  @Test
  void canUpdateWatchListForTestAccount() {
    var symbols = Stream.of("AAPL", "MSFT", "GOOG").map(Symbol::new).toList();
    var request = HttpRequest.PUT("/account/watchlist", new WatchList(symbols)).accept(MediaType.APPLICATION_JSON);
    var response = client.toBlocking().exchange(request);
    assertEquals(HttpStatus.OK, response.getStatus());
    assertEquals(symbols,
        inMemoryAccountStore.getWatchList(TEST_ACCOUNT_ID).symbols());

  }

  @Test
  void canDeleteWatchListForTestAccount() {

    final BearerAccessRefreshToken token = givenUserIsLoggedIn();
    var request = HttpRequest.DELETE("/account/watchlist").accept(MediaType.APPLICATION_JSON)
        .bearerAuth(token.getAccessToken());

    givenWatchListForAccountExists();
    assertFalse(inMemoryAccountStore.getWatchList(TEST_ACCOUNT_ID).symbols().isEmpty());

    var response = client.toBlocking().exchange(request);
    assertEquals(HttpStatus.NO_CONTENT, response.getStatus());
  }

}