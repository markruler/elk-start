package com.example.elk.repository;

import com.example.elk.model.BookEntityES;

import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

public interface BookRepositoryES extends ElasticsearchRepository<BookEntityES, String> {
  
}