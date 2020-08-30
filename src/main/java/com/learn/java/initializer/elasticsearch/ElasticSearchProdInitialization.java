package com.learn.java.initializer.elasticsearch;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

@Slf4j
@Profile("init")
@Configuration("initializeElasticSearch")
public class ElasticSearchProdInitialization implements ElasticSearchInitialization {

    @Override
    public void initElasticSearch() {
        log.info("Elasticsearch's production configuration, nothings needed");
    }
}
