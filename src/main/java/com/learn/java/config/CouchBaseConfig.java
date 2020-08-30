package com.learn.java.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.couchbase.config.AbstractCouchbaseConfiguration;

import java.util.Collections;
import java.util.List;

/**
 * Workaround for rbac changes in Couchbase
 */
@Configuration
public class CouchBaseConfig extends AbstractCouchbaseConfiguration {

    @Override
    protected List<String> getBootstrapHosts() {
        return Collections.singletonList("127.0.0.1");
    }

    @Override
    protected String getBucketName() {
        return "default";
    }

    @Override
    protected String getUsername() {
        return "Administrator";
    }

    @Override
    protected String getBucketPassword() {
        return "password";
    }
}
