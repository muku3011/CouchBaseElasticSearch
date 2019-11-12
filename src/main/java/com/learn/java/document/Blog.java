package com.learn.java.document;

import com.couchbase.client.java.repository.annotation.Field;
import com.couchbase.client.java.repository.annotation.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import org.springframework.data.couchbase.core.mapping.Document;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.Date;
import java.util.List;

@Builder
@Data
@AllArgsConstructor
@Document
public class Blog {

    @Id
    final String id;

    @Size(min=10)
    @NotNull
    @Field
    String topic;

    @NotNull
    @Field
    String author;

    @Field
    List<String> tags;

    @Field
    Date date;
}