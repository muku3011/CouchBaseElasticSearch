package com.learn.java.repository;

import com.learn.java.document.Blog;
import org.springframework.data.repository.CrudRepository;

public interface BlogRepository extends CrudRepository<Blog, String> {
}
