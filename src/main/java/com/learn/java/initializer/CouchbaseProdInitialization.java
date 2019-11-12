package com.learn.java.initializer;

import lombok.extern.java.Log;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import javax.annotation.PostConstruct;

@Log
@Profile("prod")
@Configuration("initializeCouchbase")
public class CouchbaseProdInitialization implements CouchbaseInitialization {

    @Override
    @PostConstruct
    public void initCouchBase() {
        log.info("Couchbase production configuration, nothings needed");
    }
}
