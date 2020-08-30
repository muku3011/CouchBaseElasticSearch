/*
package com.learn.java.service;

import com.learn.java.couchbase.repository.BlogRepository;
import com.learn.java.couchbase.document.Blog;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.DependsOn;
import org.springframework.stereotype.Service;

import java.util.Optional;

//@Service
//@DependsOn({"initializeElasticSearch"})
public class BlogService {

    @Autowired
    private BlogRepository blogRepository;

    public Blog createBlog(Blog blog) {
        return blogRepository.save(blog);
    }

    public Optional<Blog> readBlog(String blogId) {
        return blogRepository.findById(blogId);
    }

    public Optional<Blog> updateBlog(Blog blogToBeUpdated, String blogId) {
        return blogRepository.findById(blogId)
                .map(blog -> blogRepository
                        .save(Blog
                                .builder()
                                .id(blogId)
                                .topic(blogToBeUpdated.getTopic())
                                .tags(blogToBeUpdated.getTags())
                                .author(blogToBeUpdated.getAuthor())
                                .date(blogToBeUpdated.getDate())
                                .build())
                );
    }

    public void deleteBlog(String blogId) {
        blogRepository.deleteById(blogId);
    }
}
*/
