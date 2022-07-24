package com.hassialis.philip.broker;

import java.util.List;

import com.hassialis.philip.broker.model.Symbol;
import com.hassialis.philip.broker.data.InMemoryStore;

import io.micronaut.http.MediaType;
import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Get;
import io.micronaut.security.annotation.Secured;
import io.micronaut.security.rules.SecurityRule;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;

@Secured(SecurityRule.IS_ANONYMOUS)
@Controller("/markets")
public class MarketsController {

  private final InMemoryStore store;

  public MarketsController(final InMemoryStore store) {
    this.store = store;
  }

  @Operation(summary = "Returns all available markets")
  @ApiResponse(content = @Content(mediaType = MediaType.APPLICATION_JSON))
  @Tag(name = "markets")
  @Get
  public List<Symbol> all() {
    return store.getAllSymbols();
  }

}