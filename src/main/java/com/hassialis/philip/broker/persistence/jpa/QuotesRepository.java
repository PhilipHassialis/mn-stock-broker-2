package com.hassialis.philip.broker.persistence.jpa;

import java.util.List;
import java.util.Optional;

import com.hassialis.philip.broker.persistence.model.QuoteEntity;
import com.hassialis.philip.broker.persistence.model.SymbolEntity;

import io.micronaut.data.annotation.Repository;
import io.micronaut.data.repository.CrudRepository;

@Repository
public interface QuotesRepository extends CrudRepository<QuoteEntity, Integer> {

  @Override
  List<QuoteEntity> findAll();

  Optional<QuoteEntity> findBySymbol(SymbolEntity symbol);

}
