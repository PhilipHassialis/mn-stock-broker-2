package com.hassialis.philip.broker.watchlist;

import java.util.ArrayList;
import java.util.List;

import com.hassialis.philip.broker.Symbol;

public record WatchList(List<Symbol> symbols) {

  public WatchList() {
    this(new ArrayList<>());
  }
}
