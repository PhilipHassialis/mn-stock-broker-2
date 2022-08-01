package com.hassialis.philip.broker;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import com.hassialis.philip.broker.error.CustomError;
import com.hassialis.philip.broker.model.Quote;
import com.hassialis.philip.broker.persistence.jpa.QuotesRepository;
import com.hassialis.philip.broker.persistence.model.QuoteDTO;
import com.hassialis.philip.broker.persistence.model.QuoteEntity;
import com.hassialis.philip.broker.persistence.model.SymbolEntity;
import com.hassialis.philip.broker.data.InMemoryStore;

import io.micronaut.data.annotation.Query;
import io.micronaut.data.model.Pageable;
import io.micronaut.http.HttpResponse;
import io.micronaut.http.HttpStatus;
import io.micronaut.http.MediaType;
import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Get;
import io.micronaut.http.annotation.PathVariable;
import io.micronaut.http.annotation.QueryValue;
import io.micronaut.security.annotation.Secured;
import io.micronaut.security.rules.SecurityRule;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;

@Secured(SecurityRule.IS_ANONYMOUS)
@Controller("/quotes")
public class QuotesController {

  private final InMemoryStore store;
  private final QuotesRepository quotesRepository;

  public QuotesController(InMemoryStore store, QuotesRepository quotesRepository) {
    this.store = store;
    this.quotesRepository = quotesRepository;
  }

  @Operation(summary = "Returns a quote for a given symbol")
  @ApiResponse(content = @Content(mediaType = MediaType.APPLICATION_JSON))
  @ApiResponse(responseCode = "400", description = "Invalid symbol specified")
  @Tag(name = "quotes")
  @Get("/{symbol}")
  public HttpResponse getQuote(@PathVariable String symbol) {
    final Optional<Quote> maybeQuote = store.fetchQuote(symbol);
    if (maybeQuote.isEmpty()) {
      final CustomError notFound = new CustomError(HttpStatus.NOT_FOUND.getCode(), HttpStatus.NOT_FOUND.name(),
          "Quote for symbol not available", "/quotes/" + symbol);
      return HttpResponse.notFound(notFound);
    }
    return HttpResponse.ok(maybeQuote.get());

  }

  @Operation(summary = "Returns all quotes via JPA")
  @ApiResponse(content = @Content(mediaType = MediaType.APPLICATION_JSON))
  @ApiResponse(responseCode = "400", description = "Invalid symbol specified")
  @Tag(name = "quotes")
  @Get("/jpa")
  public List<QuoteEntity> getAllQuotesViaJpa() {
    return quotesRepository.findAll();
  }

  @Operation(summary = "Returns a quote for a given symbol via JPA")
  @ApiResponse(content = @Content(mediaType = MediaType.APPLICATION_JSON))
  @ApiResponse(responseCode = "400", description = "Invalid symbol specified")
  @Tag(name = "quotes")
  @Get("/{symbol}/jpa")
  public HttpResponse getQuoteViaJPA(@PathVariable String symbol) {
    final Optional<QuoteEntity> maybeQuote = quotesRepository.findBySymbol(new SymbolEntity(symbol));
    if (maybeQuote.isEmpty()) {
      final CustomError notFound = new CustomError(HttpStatus.NOT_FOUND.getCode(), HttpStatus.NOT_FOUND.name(),
          "Quote for symbol not available in db", "/quotes/" + symbol + "/jpa");
      return HttpResponse.notFound(notFound);
    }
    return HttpResponse.ok(maybeQuote.get());

  }

  @Operation(summary = "Returns all quotes ordered by volume descending")
  @ApiResponse(content = @Content(mediaType = MediaType.APPLICATION_JSON))
  @Tag(name = "quotes")
  @Get("/jpa/ordered/desc")
  public List<QuoteDTO> orderedDesc() {
    return quotesRepository.listOrderByVolumeDesc();
  }

  @Operation(summary = "Returns all quotes ordered by volume ascending")
  @ApiResponse(content = @Content(mediaType = MediaType.APPLICATION_JSON))
  @Tag(name = "quotes")
  @Get("/jpa/ordered/asc")
  public List<QuoteDTO> orderedAsc() {
    return quotesRepository.listOrderByVolumeAsc();
  }

  @Operation(summary = "Returns all quotes with volume higher than path variable and pagination support")
  @ApiResponse(content = @Content(mediaType = MediaType.APPLICATION_JSON))
  @Tag(name = "quotes")
  @Get("/jpa/pagination{?page,volume}")
  public List<QuoteEntity> volumeFilterPagination(@QueryValue Optional<Integer> page,
      @QueryValue Optional<BigDecimal> volume) {
    int myPage = page.isEmpty() ? 0 : page.get();
    BigDecimal myVolume = volume.isEmpty() ? BigDecimal.ZERO : volume.get();
    return quotesRepository.findByVolumeGreaterThan(myVolume, Pageable.from(myPage, 2));
  }

  @Operation(summary = "Returns all quotes with volume higher than path variable ordered by volume descending")
  @ApiResponse(content = @Content(mediaType = MediaType.APPLICATION_JSON))
  @Tag(name = "quotes")
  @Get("/jpa/volume/{volume}/ordered/desc")
  public List<QuoteEntity> volumeFilterOrdered(@PathVariable BigDecimal volume) {
    return quotesRepository.findByVolumeGreaterThanOrderByVolumeDesc(volume);
  }

  @Operation(summary = "Returns all quotes with pagination support in path variable")
  @ApiResponse(content = @Content(mediaType = MediaType.APPLICATION_JSON))
  @Tag(name = "quotes")
  @Get("/jpa/pagination/{page}")
  List<QuoteEntity> allWithPagination(@PathVariable int page) {
    return quotesRepository.listOrderBySymbol(Pageable.from(page, 5)).getContent();
  }

}
