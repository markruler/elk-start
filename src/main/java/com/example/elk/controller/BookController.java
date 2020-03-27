package com.example.elk.controller;

import java.util.List;

import com.example.elk.model.BookEntityES;
import com.example.elk.model.BookEntityOracle;
import com.example.elk.service.BookService;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
@RestController
public class BookController {

  private final BookService bookService;

  @PostMapping("/insert")
  public String insertBook(@RequestBody BookEntityOracle book) {
    log.info("book : {}", book);
    bookService.save(book);
    return "success";
  }

  @GetMapping("/all")
  public ResponseEntity<List<BookEntityES>> getAll() {
    List<BookEntityES> books = bookService.getAll();
    log.info("controller -> books : {}", books);
    return new ResponseEntity<>(books, HttpStatus.OK);
  }
}