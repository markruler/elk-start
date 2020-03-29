package com.example.elk.service;

import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.AbstractMap.SimpleEntry;
import java.util.Map.Entry;
import java.util.stream.Collectors;
import java.util.stream.Stream;

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
import org.elasticsearch.search.aggregations.bucket.range.DateRangeAggregationBuilder;
import org.elasticsearch.search.aggregations.bucket.terms.ParsedStringTerms;
import org.elasticsearch.search.aggregations.bucket.terms.ParsedTerms;
import org.elasticsearch.search.aggregations.bucket.terms.Terms;
import org.elasticsearch.search.aggregations.bucket.terms.TermsAggregationBuilder;
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
      // log.info("service -> book : {}", book);
      list.add(book);
    });
    return list;
  }

  @Override
  public Map<LocalDate, Long> getAggregations() {
    DateRangeAggregationBuilder dateRange =
      AggregationBuilders
        .dateRange("date_range")
        .addRange("now-2M/M", "now-1M/M")
        .field("createDateTime");
    
    TermsAggregationBuilder terms = 
      AggregationBuilders
        .terms("category_count")
        .field("category");
    
    AggregationBuilder aggregation =
      AggregationBuilders
        .global("group_by_category")
          .subAggregation(
            AggregationBuilders
              .dateRange("date_range")
              .addRange("now-2M/M", "now-1M/M")
              .field("createDateTime")
              .subAggregation(
                AggregationBuilders
                  .terms("category_count")
                  .field("category")
              )
          );
    
    AggregationBuilder histogram = 
      AggregationBuilders
        .dateHistogram("books")
          .field("createDateTime")
          .dateHistogramInterval(DateHistogramInterval.MONTH);

    SearchSourceBuilder searchSourceBuilder =
      SearchSourceBuilder
        .searchSource()
        // .aggregation(aggregation);
        .aggregation(histogram);
        // .aggregation(dateRange)
        // .aggregation(terms);
    
    SearchRequest searchRequest = new SearchRequest();
    // searchRequest.indices("reading_books");
    searchRequest.source(searchSourceBuilder);
    
    Aggregations agg = null;
    Map<LocalDate, Long> map = new HashMap<>();
    try {
      SearchResponse res = client.search(searchRequest, RequestOptions.DEFAULT);
      agg = res.getAggregations();
      log.info(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>");
      log.info("res : {}", res);
      log.info("agg : {}", agg.get("역사"));
      if (agg != null) {
        agg.forEach(consumer -> {
          log.info("result : {}", consumer);
        });
      }
      
      log.info(agg.get("books").toString());
      for (Histogram.Bucket entry : ((ParsedDateHistogram)agg.get("books")).getBuckets()) {
        // log.info("entry : {}", entry);
        String key = entry.getKeyAsString(); // bucket key
        long docCount = entry.getDocCount(); // Doc count
        log.info("key [{}], doc_count [{}]", key, docCount);
        LocalDate seriesDate = LocalDate.parse(key.substring(0, 10), DateTimeFormatter.ISO_DATE);
        log.info("string2date : {}", seriesDate);
        map.put(seriesDate, docCount);
      }

      // SearchHits searchHits = res.getHits();
      // SearchHit[] hits = searchHits.getHits();
      // for (SearchHit hit : hits) {
      //   log.info(hit.getSourceAsString());
      // }

      // XContentBuilder builder = CborXContent.contentBuilder(); 
      // res.getAggregations().toXContent(builder, ToXContent.EMPTY_PARAMS);
      // ObjectMapper cborObjectMapper = new ObjectMapper(new CBORFactory());
      // JsonNode jsonNode = cborObjectMapper.readTree(builder.bytes().streamInput());

    } catch (IOException e) {
      e.printStackTrace();
    }
    
    // SearchRequestBuilder req = new SearchRequestBuilder();
    // bookRepositoryES.search();
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
        // .aggregation(aggregation);
        .aggregation(pie);
        // .aggregation(dateRange)
        // .aggregation(terms);
        
    SearchRequest searchRequest = new SearchRequest();
    // searchRequest.indices("reading_books");
    searchRequest.source(searchSourceBuilder);
    
    Aggregations agg = null;
    Map<LocalDate, Map<String, Long>> superMap = new LinkedHashMap<>();
    Map<String, Long> map = new LinkedHashMap<>();
    try {
      SearchResponse res = client.search(searchRequest, RequestOptions.DEFAULT);
      agg = res.getAggregations();
      log.info(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>");
      log.info("res : {}", res);
      if (agg != null) {
        agg.forEach(consumer -> {
          log.info("result : {}", consumer);
        });
      }
      
      // log.info(agg.get("books").toString());
      // 날짜별
      for (Histogram.Bucket hBucket : ((ParsedDateHistogram)agg.get("books")).getBuckets()) {
        String histogramKey = hBucket.getKeyAsString(); // bucket key
        long histogramDocCount = hBucket.getDocCount(); // Doc count
        log.info("h! key [{}] / doc_count [{}]", histogramKey, histogramDocCount);
        LocalDate seriesDate = LocalDate.parse(histogramKey.substring(0, 10), DateTimeFormatter.ISO_DATE);
        log.info("string2date : {}", seriesDate);
        // map.put(seriesDate, histogramDocCount);

        // 분야별
        for (Terms.Bucket tBucket : ((ParsedStringTerms)hBucket.getAggregations().get("category")).getBuckets()) {
          String termsKey = tBucket.getKeyAsString();
          long termsDocCount = tBucket.getDocCount();
          log.info("t! key [{}] / doc_count [{}]권", termsKey, termsDocCount);
          map.put(termsKey, termsDocCount);
        }

        superMap.put(seriesDate, map);
        // log.info("superMap : {}", superMap.get(seriesDate));
        map = new LinkedHashMap<>();
        // log.info("superMap : {}", superMap.get(seriesDate));
      }
    } catch (IOException e) {
      e.printStackTrace();
    }

    // Flattening Nested Collections in Java
    // Stream<SimpleEntry<String, Long>> entryStream = 
    //   superMap.entrySet().stream().flatMap(e -> e.getValue().entrySet().stream().flatMap(v -> Stream.of(new SimpleEntry<>(e.getKey() + "." + v.getKey(), v.getValue()))));
    // Map<String, Long> collect = entryStream.collect(Collectors.toMap(Entry::getKey, Entry::getValue));
    // collect.forEach((k, v) -> {
    //   log.info("key:" + k + " / value:" + v);
    // });
    
    return superMap;
  }
  
}