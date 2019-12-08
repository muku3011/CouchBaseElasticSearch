package com.learn.java.couchbase.repository;

import com.learn.java.couchbase.document.Blog;
import org.springframework.data.couchbase.repository.CouchbaseRepository;

public interface BlogRepository extends CouchbaseRepository<Blog, String> {
}
