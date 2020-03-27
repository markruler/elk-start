package com.example.elk.model;

import java.time.LocalDateTime;

import javax.persistence.Id;

import org.springframework.data.elasticsearch.annotations.Document;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@Document(indexName = "book", type = "_doc")
public class BookEntityES {
  @Id
  private String id;
  private String isbn;
  private String title;
  private String author;
  private String category;
  private LocalDateTime createDateTime;
}