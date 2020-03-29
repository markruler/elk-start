package com.example.elk.config;

import com.fasterxml.jackson.annotation.JsonIgnore;

import org.elasticsearch.search.aggregations.bucket.terms.StringTerms;
import org.springframework.boot.autoconfigure.jackson.Jackson2ObjectMapperBuilderCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;

@Configuration
public class JacksonConfig {

  @Bean
  public Jackson2ObjectMapperBuilderCustomizer changeKeyAsNumber() {
    return new Jackson2ObjectMapperBuilderCustomizer() {

      @Override
      public void customize(Jackson2ObjectMapperBuilder jacksonObjectMapperBuilder) {
        jacksonObjectMapperBuilder.mixIn(StringTerms.Bucket.class, MixIn.class);
      }
    };
  }

}

abstract class MixIn {
    @JsonIgnore
    abstract public Number getKeyAsNumber();
}