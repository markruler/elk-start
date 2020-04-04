package com.example.elk.controller;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

import com.example.elk.model.BookEntityES;
import com.example.elk.model.BookEntityOracle;
import com.example.elk.repository.RestDao;
import com.example.elk.service.BookService;
import com.google.gson.Gson;
import com.google.gson.JsonElement;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
@RestController
public class BookController {

  private final BookService bookService;
  private final RestDao restDao;

  @PostMapping("/insert")
  public String insertBook(@RequestBody BookEntityOracle book) {
    bookService.save(book);
    return "success";
  }

  @GetMapping("/all")
  public ResponseEntity<List<BookEntityES>> getAll() {
    List<BookEntityES> books = bookService.getAll();
    return new ResponseEntity<>(books, HttpStatus.OK);
  }

  @PostMapping("/agg")
  public String aggregation(@RequestBody String requestJson) throws Exception {
    String index = "/reading_books/_search";
    JsonElement json = new Gson().toJsonTree(requestJson);
    log.info("requestJson2JsonTree : {}", json);
    log.info("requestJson2String : {}", requestJson.toString());
    String query = json.toString();
    
    return restDao.getAggregation(index, query);
  }

  @GetMapping("/histogram")
  public ResponseEntity<Map<LocalDate, Long>> getAggregations() {
    Map<LocalDate, Long> agg = bookService.getAggregations();
    return new ResponseEntity<>(agg, HttpStatus.OK);
  }

  @GetMapping("/pie")
  public ResponseEntity<Map<LocalDate, Map<String, Long>>> getPie() {
    Map<LocalDate, Map<String, Long>> agg = bookService.getCategoryCount();
    return new ResponseEntity<>(agg, HttpStatus.OK);
  }
}