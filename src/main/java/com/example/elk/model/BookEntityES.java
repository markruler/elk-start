package com.example.elk.model;

import java.util.Date;

import javax.persistence.Id;

import org.springframework.data.elasticsearch.annotations.Document;

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
  private String publisher;

  // @JsonProperty("createDateTime")
  // @Field(type = FieldType.Date, format = DateFormat.year_month_day)
  private Date createDateTime;
}