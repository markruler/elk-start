package com.example.elk.service;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

import com.example.elk.model.BookEntityES;
import com.example.elk.model.BookEntityOracle;

public interface BookService {

  void save(BookEntityOracle book);
  
  List<BookEntityES> getAll();
  
  Map<LocalDate, Long> getAggregations();

  Map<LocalDate, Map<String, Long>> getCategoryCount();
}