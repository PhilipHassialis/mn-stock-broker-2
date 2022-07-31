package com.hassialis.philip.broker.persistence.jpa;

import java.math.BigDecimal;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Stream;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.hassialis.philip.broker.persistence.model.QuoteEntity;
import com.hassialis.philip.broker.persistence.model.SymbolEntity;

import io.micronaut.context.annotation.Requires;
import io.micronaut.context.env.Environment;
import io.micronaut.context.event.StartupEvent;
import io.micronaut.runtime.event.annotation.EventListener;
import jakarta.inject.Singleton;

@Singleton
@Requires(notEnv = Environment.TEST)
public class TestDataProvider {

  private static final Logger LOG = LoggerFactory.getLogger(TestDataProvider.class);
  private final SymbolsRepository symbols;
  private final QuotesRepository quotes;
  private static final ThreadLocalRandom RANDOM = ThreadLocalRandom.current();

  public TestDataProvider(SymbolsRepository symbols, QuotesRepository quotes) {
    this.symbols = symbols;
    this.quotes = quotes;
  }

  @EventListener
  public void init(StartupEvent event) {
    if (symbols.findAll().isEmpty()) {
      LOG.info("Initializing test data for symbols");
      Stream.of("AAPL", "AMZN", "META", "TSLA")
          .map(SymbolEntity::new)
          .forEach(symbols::save);
    }

    if (quotes.findAll().isEmpty()) {
      LOG.info("Initializing test data for quotes");
      symbols.findAll().forEach(symbol -> {
        var quote = new QuoteEntity();
        quote.setSymbol(symbol);
        quote.setAsk(randomValue());
        quote.setBid(randomValue());
        quote.setLastPrice(randomValue());
        quote.setVolume(randomValue());
        quotes.save(quote);
      });
    }
  }

  private BigDecimal randomValue() {
    return BigDecimal.valueOf(RANDOM.nextDouble() * 100);
  }

}
