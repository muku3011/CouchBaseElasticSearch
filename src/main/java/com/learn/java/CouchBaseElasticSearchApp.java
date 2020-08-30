package com.learn.java;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.couchbase.repository.config.EnableCouchbaseRepositories;
import org.springframework.data.elasticsearch.repository.config.EnableElasticsearchRepositories;

@SpringBootApplication
//@EnableCouchbaseRepositories(basePackages = "com.learn.java.couchbase")
//@EnableElasticsearchRepositories(basePackages = "com.learn.java.elasticsearch")
public class CouchBaseElasticSearchApp {

	public static void main(String[] args) {
		SpringApplication.run(CouchBaseElasticSearchApp.class, args);
	}
}
