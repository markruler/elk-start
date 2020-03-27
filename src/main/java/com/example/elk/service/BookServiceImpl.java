package com.example.elk.service;

import java.util.ArrayList;
import java.util.List;

import com.example.elk.model.BookEntityES;
import com.example.elk.model.BookEntityOracle;
import com.example.elk.repository.BookRepositoryES;
import com.example.elk.repository.BookRepositoryOracle;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
@Service
public class BookServiceImpl implements BookService {

  private final BookRepositoryES bookRepositoryES;
  private final BookRepositoryOracle bookRepositoryOracle;

  @Override
  public void save(BookEntityOracle book) {
    bookRepositoryOracle.save(book);
  }

  @Override
  public List<BookEntityES> getAll() {
    Iterable<BookEntityES> result = bookRepositoryES.findAll();
    List<BookEntityES> list = new ArrayList<>();
    result.forEach(book -> {
      log.info("service -> book : {}", book);
      list.add(book);
    });
    return list;
  }
  
}