package com.hassialis.philip.auth.jwt;

import java.util.ArrayList;

import org.reactivestreams.Publisher;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.micronaut.http.HttpRequest;
import io.micronaut.security.authentication.AuthenticationProvider;
import io.micronaut.security.authentication.AuthenticationRequest;
import io.micronaut.security.authentication.AuthenticationResponse;
import io.reactivex.rxjava3.core.BackpressureStrategy;
import io.reactivex.rxjava3.core.Flowable;
import jakarta.inject.Singleton;

@Singleton
public class AuthenticationProviderUserPassword implements AuthenticationProvider {

  private final static Logger LOG = LoggerFactory.getLogger(AuthenticationProviderUserPassword.class);

  public final static String USERNAME = "username";
  public final static String PASSWORD = "password";

  @Override
  public Publisher<AuthenticationResponse> authenticate(HttpRequest<?> httpRequest,
      AuthenticationRequest<?, ?> authenticationRequest) {

    return Flowable.create(emitter -> {
      final Object identity = authenticationRequest.getIdentity();
      final Object secret = authenticationRequest.getSecret();
      LOG.debug("User {} tries to login", identity);
      if (identity.equals(USERNAME) && secret.equals(PASSWORD)) {
        var userDetails = AuthenticationResponse.success((String) identity, new ArrayList<>());
        emitter.onNext(userDetails);
        emitter.onComplete();
        return;
      }
      emitter.onError(AuthenticationResponse.exception("Wrong username or password"));
    }, BackpressureStrategy.ERROR);

  }

}
