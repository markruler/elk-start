package com.example.elk.model;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.Date;

import javax.persistence.Id;

import com.fasterxml.jackson.annotation.JsonProperty;

import org.springframework.data.elasticsearch.annotations.DateFormat;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@Document(indexName = "reading_books", type = "_doc")
public class BookEntityES {
  @Id
  private String id;
  private String isbn;
  private String title;
  private String author;
  private String category;

  // @JsonProperty("createDateTime")
  // @Field(type = FieldType.Date, format = DateFormat.year_month_day)
  private Date createDateTime;
}