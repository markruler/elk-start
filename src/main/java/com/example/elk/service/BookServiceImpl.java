package com.example.elk.service;

import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import com.example.elk.model.BookEntityES;
import com.example.elk.model.BookEntityOracle;
import com.example.elk.repository.BookRepositoryES;
import com.example.elk.repository.BookRepositoryOracle;

import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.search.aggregations.AggregationBuilder;
import org.elasticsearch.search.aggregations.AggregationBuilders;
import org.elasticsearch.search.aggregations.Aggregations;
import org.elasticsearch.search.aggregations.BucketOrder;
import org.elasticsearch.search.aggregations.bucket.histogram.DateHistogramInterval;
import org.elasticsearch.search.aggregations.bucket.histogram.Histogram;
import org.elasticsearch.search.aggregations.bucket.histogram.ParsedDateHistogram;
import org.elasticsearch.search.aggregations.bucket.terms.ParsedStringTerms;
import org.elasticsearch.search.aggregations.bucket.terms.Terms;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
@Service
public class BookServiceImpl implements BookService {

  private final BookRepositoryES bookRepositoryES;
  private final BookRepositoryOracle bookRepositoryOracle;
  private final RestHighLevelClient client;

  @Override
  public void save(BookEntityOracle book) {
    bookRepositoryOracle.save(book);
  }

  @Override
  public List<BookEntityES> getAll() {
    Iterable<BookEntityES> result = bookRepositoryES.findAll();
    List<BookEntityES> list = new ArrayList<>();
    result.forEach(book -> {
      list.add(book);
    });
    return list;
  }

  @Override
  public Map<LocalDate, Long> getAggregations() {

    AggregationBuilder histogram = 
      AggregationBuilders
        .dateHistogram("books")
          .field("createDateTime")
          .dateHistogramInterval(DateHistogramInterval.MONTH);

    SearchSourceBuilder searchSourceBuilder =
      SearchSourceBuilder
        .searchSource()
        .aggregation(histogram);
    
    SearchRequest searchRequest = new SearchRequest();
    searchRequest.source(searchSourceBuilder);
    
    Aggregations agg = null;
    Map<LocalDate, Long> map = new HashMap<>();
    try {
      SearchResponse res = client.search(searchRequest, RequestOptions.DEFAULT);
      agg = res.getAggregations();
      if (agg != null) {
        agg.forEach(consumer -> {
          log.info("result : {}", consumer);
        });
      }
      
      log.info(agg.get("books").toString());
      for (Histogram.Bucket entry : ((ParsedDateHistogram)agg.get("books")).getBuckets()) {
        String key = entry.getKeyAsString(); // bucket key
        long docCount = entry.getDocCount(); // Doc count
        log.info("key [{}], doc_count [{}]", key, docCount);
        LocalDate seriesDate = LocalDate.parse(key.substring(0, 10), DateTimeFormatter.ISO_DATE);
        log.info("string2date : {}", seriesDate);
        map.put(seriesDate, docCount);
      }
    } catch (IOException e) {
      e.printStackTrace();
    }
    
    return map;
  }

  @Override
  public Map<LocalDate, Map<String, Long>> getCategoryCount() {
    
    AggregationBuilder pie = 
      AggregationBuilders
        .dateHistogram("books")
          .field("createDateTime")
          .dateHistogramInterval(DateHistogramInterval.MONTH)
        .subAggregation(
          AggregationBuilders
            .terms("category")
            .field("category")
            .order(BucketOrder.count(false))
        );
    
    SearchSourceBuilder searchSourceBuilder =
      SearchSourceBuilder
        .searchSource()
        .aggregation(pie);
        
    SearchRequest searchRequest = new SearchRequest();
    searchRequest.source(searchSourceBuilder);
    
    Aggregations agg = null;
    Map<LocalDate, Map<String, Long>> superMap = new LinkedHashMap<>();
    Map<String, Long> map = new LinkedHashMap<>();
    try {
      SearchResponse res = client.search(searchRequest, RequestOptions.DEFAULT);
      agg = res.getAggregations();
      if (agg != null) {
        agg.forEach(consumer -> {
          log.info("result : {}", consumer);
        });
      }
      
      // 날짜별
      for (Histogram.Bucket hBucket : ((ParsedDateHistogram)agg.get("books")).getBuckets()) {
        String histogramKey = hBucket.getKeyAsString(); // bucket key
        long histogramDocCount = hBucket.getDocCount(); // Doc count
        log.info("h! key [{}] / doc_count [{}]", histogramKey, histogramDocCount);
        LocalDate seriesDate = LocalDate.parse(histogramKey.substring(0, 10), DateTimeFormatter.ISO_DATE);

        // 분야별
        for (Terms.Bucket tBucket : ((ParsedStringTerms)hBucket.getAggregations().get("category")).getBuckets()) {
          String termsKey = tBucket.getKeyAsString();
          long termsDocCount = tBucket.getDocCount();
          log.info("t! key [{}] / doc_count [{}]권", termsKey, termsDocCount);
          map.put(termsKey, termsDocCount);
        }

        superMap.put(seriesDate, map);
        map = new LinkedHashMap<>();
      }
    } catch (IOException e) {
      e.printStackTrace();
    }
    return superMap;
  }
  
}