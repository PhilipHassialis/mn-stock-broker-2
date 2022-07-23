package com.hassialis.philip.broker;

import com.fasterxml.jackson.databind.JsonNode;
import com.hassialis.philip.broker.data.InMemoryStore;
import com.hassialis.philip.broker.model.Symbol;

import io.micronaut.http.HttpStatus;
import io.micronaut.http.client.HttpClient;
import io.micronaut.http.client.annotation.Client;
import io.micronaut.test.extensions.junit5.annotation.MicronautTest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Assertions;

import jakarta.inject.Inject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@MicronautTest
class SymbolsControllerTest {

    private static final Logger LOG = LoggerFactory.getLogger(SymbolsControllerTest.class);

    @Inject
    @Client("/symbols")
    HttpClient client;

    @Inject
    InMemoryStore inMemoryStore;

    @BeforeEach
    void setup() {
        inMemoryStore.initializeWith(10);
    }

    @Test
    void symbolsEndpointReturnsListOfSymbols() {
        var response = client.toBlocking().exchange("/", JsonNode.class);
        Assertions.assertEquals(HttpStatus.OK, response.getStatus());
        Assertions.assertEquals(10, response.getBody().get().size());

    }

    @Test
    void symbolsEndpointReturnsCorrectSymbol() {
        var testSymbol = new Symbol("TEST");
        inMemoryStore.getSymbols().put(testSymbol.value(), testSymbol);
        var response = client.toBlocking().exchange("/" + testSymbol.value(), Symbol.class);
        Assertions.assertEquals(HttpStatus.OK, response.getStatus());
        Assertions.assertEquals(testSymbol, response.getBody().get());
    }

    @Test
    void symbolsEndpointReturnsListOfSymbolTakingQueryParametersIntoAccount() {
        var max10 = client.toBlocking().exchange("/filter?max=10", JsonNode.class);
        LOG.debug("Max: 10 {}", max10.getBody().get().toPrettyString());
        Assertions.assertEquals(HttpStatus.OK, max10.getStatus());
        Assertions.assertEquals(10, max10.getBody().get().size());

        var offset7 = client.toBlocking().exchange("/filter?offset=7", JsonNode.class);
        LOG.debug("Offset: 7 {}", offset7.getBody().get().toPrettyString());
        Assertions.assertEquals(HttpStatus.OK, offset7.getStatus());
        Assertions.assertEquals(3, offset7.getBody().get().size());

        var max2offset7 = client.toBlocking().exchange("/filter?offset=7&max=2", JsonNode.class);
        LOG.debug("Max: 2 Offset: 7 {}", max2offset7.getBody().get().toPrettyString());
        Assertions.assertEquals(HttpStatus.OK, max2offset7.getStatus());
        Assertions.assertEquals(2, max2offset7.getBody().get().size());
    }

}
