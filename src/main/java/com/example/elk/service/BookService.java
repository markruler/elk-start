package com.example.elk.service;

import java.util.List;

import com.example.elk.model.BookEntityES;
import com.example.elk.model.BookEntityOracle;

public interface BookService {

	List<BookEntityES> getAll();

	void save(BookEntityOracle book);

}