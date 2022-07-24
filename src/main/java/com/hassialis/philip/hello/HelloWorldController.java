package com.hassialis.philip.hello;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.micronaut.context.annotation.Property;
import io.micronaut.http.MediaType;
import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Get;
// import jakarta.inject.Inject;

@Controller("/hello")
public class HelloWorldController {

  private static final Logger LOG = LoggerFactory.getLogger(HelloWorldController.class);

  // @Inject
  private final MyService service;
  private final String helloFromConfig;
  private final HelloWorldTranslationConfig translationConfig;

  public HelloWorldController(MyService service, @Property(name = "hello.world.message") String helloFromConfig,
      HelloWorldTranslationConfig translationConfig) {
    this.service = service;
    this.helloFromConfig = helloFromConfig;
    this.translationConfig = translationConfig;
  }

  @Get(produces = MediaType.TEXT_PLAIN)
  public String helloWorld() {
    LOG.debug("Called hello world api");
    return service.helloFromService();
  }

  @Get(uri = "/config", produces = MediaType.TEXT_PLAIN)
  public String helloConfig() {
    LOG.debug("Return config value: {}", helloFromConfig);
    return helloFromConfig;
  }

  @Get(uri = "/translation", produces = MediaType.APPLICATION_JSON)
  public HelloWorldTranslationConfig helloTranslation() {
    return translationConfig;
  }

}
