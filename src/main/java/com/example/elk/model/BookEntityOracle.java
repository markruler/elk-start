package com.example.elk.model;

import java.time.LocalDateTime;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.CreationTimestamp;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@Entity
@Table(name = "book")
public class BookEntityOracle {
  @Id
  @GeneratedValue(strategy = GenerationType.SEQUENCE)
  private Long id;  // UUID로 지정시 oracle type이 RAW가 되고, logstash로 데이터 적재 시 "LogStash::Json::GeneratorError"
  private String isbn;
  private String title;
  private String author;
  private String category;

  @CreationTimestamp
  private LocalDateTime createDateTime;
}