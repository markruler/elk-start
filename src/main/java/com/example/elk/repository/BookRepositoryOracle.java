package com.example.elk.repository;

import com.example.elk.model.BookEntityOracle;

import org.springframework.data.jpa.repository.JpaRepository;

public interface BookRepositoryOracle extends JpaRepository<BookEntityOracle, String> {

}