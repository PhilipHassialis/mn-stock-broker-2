package com.hassialis.philip.broker.watchlist;

import io.micronaut.http.HttpResponse;
import io.micronaut.http.annotation.Body;
import io.micronaut.http.annotation.Get;
import io.micronaut.http.annotation.Header;
import io.micronaut.http.annotation.Post;
import io.micronaut.http.client.annotation.Client;
import io.micronaut.security.authentication.UsernamePasswordCredentials;
import io.micronaut.security.token.jwt.render.BearerAccessRefreshToken;
// import io.reactivex.rxjava3.core.Flowable;
import reactor.core.publisher.Flux;

@Client("/")
public interface JWTWatchListClient {

  @Post("/login")
  BearerAccessRefreshToken login(@Body UsernamePasswordCredentials credentials);

  @Get("/account/watchlist")
  Flux<HttpResponse<WatchList>> retrieveWatchList(@Header(name = "Authorization") String authorization);
}
