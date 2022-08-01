package com.hassialis.philip.broker.persistence.jpa;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import com.hassialis.philip.broker.persistence.model.QuoteDTO;
import com.hassialis.philip.broker.persistence.model.QuoteEntity;
import com.hassialis.philip.broker.persistence.model.SymbolEntity;

import io.micronaut.data.annotation.Repository;
import io.micronaut.data.model.Pageable;
import io.micronaut.data.model.Slice;
import io.micronaut.data.repository.CrudRepository;

@Repository
public interface QuotesRepository extends CrudRepository<QuoteEntity, Integer> {

  @Override
  List<QuoteEntity> findAll();

  Optional<QuoteEntity> findBySymbol(SymbolEntity symbol);

  List<QuoteDTO> listOrderByVolumeDesc();

  List<QuoteDTO> listOrderByVolumeAsc();

  List<QuoteEntity> findByVolumeGreaterThanOrderByVolumeDesc(BigDecimal volume);

  // pagination

  List<QuoteEntity> findByVolumeGreaterThan(BigDecimal volume, Pageable pageable);

  Slice<QuoteEntity> listOrderBySymbol(Pageable pageable);

}
