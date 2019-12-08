package com.learn.java.controller;

import com.learn.java.couchbase.document.Blog;
import com.learn.java.exception.ErrorCode;
import com.learn.java.exception.ObjectNotFoundException;
import com.learn.java.service.BlogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.Optional;

@RestController
@RequestMapping("/blog")
public class BlogController {

    @Autowired
    private BlogService blogService;

    @GetMapping
    public String index() {
        return "Welcome to the CRUD application!!";
    }

    @PostMapping
    public Blog createBlog(@RequestBody Blog blog) {
        return blogService.createBlog(blog);
    }

    @GetMapping("/{blogId}")
    public Blog readBlog(@PathVariable String blogId) throws ObjectNotFoundException {
        Optional<Blog> blog = blogService.readBlog(blogId);
        if (blog.isPresent()) {
            return blog.get();
        } else {
            throw new ObjectNotFoundException(ErrorCode.BLOG_NOT_FOUND);
        }
    }

    @PutMapping("/{idToBeUpdated}")
    public Blog updateBlog(@RequestBody Blog blogToBeUpdated, @PathVariable String idToBeUpdated) throws ObjectNotFoundException {
        Optional<Blog> blog = blogService.updateBlog(blogToBeUpdated, idToBeUpdated);
        if (blog.isPresent()) {
            return blog.get();
        } else {
            throw new ObjectNotFoundException(ErrorCode.BLOG_NOT_FOUND);
        }
    }

    @DeleteMapping("/{id}")
    public void deleteBlog(@PathVariable String id) {
        blogService.deleteBlog(id);
    }
}
