package com.hassialis.philip.broker;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.hassialis.philip.broker.model.Symbol;
import com.hassialis.philip.broker.persistence.jpa.SymbolEntity;
import com.hassialis.philip.broker.persistence.jpa.SymbolsRepository;
import com.hassialis.philip.broker.data.InMemoryStore;

import io.micronaut.core.async.annotation.SingleResult;
import io.micronaut.http.HttpResponse;
import io.micronaut.http.MediaType;
import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Get;
import io.micronaut.security.annotation.Secured;
import io.micronaut.security.rules.SecurityRule;
import io.reactivex.rxjava3.core.Single;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;

@Secured(SecurityRule.IS_ANONYMOUS)
@Controller("/markets")
public class MarketsController {

  private final InMemoryStore store;
  private final SymbolsRepository symbolsRepository;
  private static final Logger LOG = LoggerFactory.getLogger(MarketsController.class);

  public MarketsController(InMemoryStore store, SymbolsRepository symbolsRepository) {
    this.store = store;
    this.symbolsRepository = symbolsRepository;
  }

  @Operation(summary = "Returns all available markets")
  @ApiResponse(content = @Content(mediaType = MediaType.APPLICATION_JSON))
  @Tag(name = "markets")
  @Get
  public List<Symbol> all() {
    return store.getAllSymbols();
  }

  @Operation(summary = "Returns all available markets using JPA")
  @ApiResponse(content = @Content(mediaType = MediaType.APPLICATION_JSON))
  @Tag(name = "markets")
  @Get("/jpa")
  public Single<List<SymbolEntity>> allSymbolsViaJpa() {
    List<SymbolEntity> symbols = symbolsRepository.findAll();
    LOG.info("Found symbols: {}", symbols);
    return Single.just(symbols);
  }

}