package com.learn.java.exception;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ErrorCode {
    BLOG_NOT_FOUND(100, "Blog not found");

    private final int code;
    private final String message;

    public BlogError getBlogErrorResponse() {
        return new BlogError(this.code, this.message);
    }
}

@Builder
@Data
@AllArgsConstructor
class BlogError {

    int errorCode;
    String errorMessage;
}
