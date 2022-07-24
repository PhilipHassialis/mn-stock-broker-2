package com.hassialis.philip.hello;

import javax.validation.constraints.NotBlank;

import io.micronaut.context.annotation.ConfigurationProperties;

@ConfigurationProperties("hello.world.translation")
public interface HelloWorldTranslationConfig {

  @NotBlank
  String getGr();

  @NotBlank
  String getEn();

}
