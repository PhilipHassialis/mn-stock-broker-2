package com.hassialis.philip.broker.watchlist;

import java.util.UUID;

import com.hassialis.philip.broker.data.InMemoryAccountStore;
import io.micronaut.http.HttpResponse;
import io.micronaut.http.MediaType;
import io.micronaut.http.annotation.*;
import io.micronaut.security.annotation.Secured;
import io.micronaut.security.rules.SecurityRule;

// @Secured(SecurityRule.IS_AUTHENTICATED)
@Secured(SecurityRule.IS_ANONYMOUS)
@Controller("/account/watchlist")
public record WatchListController(InMemoryAccountStore store) {

  static final UUID ACCOUNT_ID = UUID.randomUUID();

  @Get(produces = MediaType.APPLICATION_JSON)
  public WatchList get() {
    return store.getWatchList(ACCOUNT_ID);
  }

  @Put(consumes = MediaType.APPLICATION_JSON, produces = MediaType.APPLICATION_JSON)
  public WatchList update(@Body WatchList watchList) {
    return store.updateWatchList(ACCOUNT_ID, watchList);
  }

  @Delete(produces = MediaType.APPLICATION_JSON)
  public HttpResponse<Void> delete() {
    store.deleteWatchList(ACCOUNT_ID);
    return HttpResponse.noContent();
  }

}
