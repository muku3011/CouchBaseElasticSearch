package com.learn.java.initializer;

import com.learn.java.util.RestService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.binary.Base64;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.couchbase.CouchbaseProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.http.*;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestClientException;

import javax.annotation.PostConstruct;
import java.text.MessageFormat;
import java.util.List;
import java.util.Map;

/**
 * https://docs.couchbase.com/server/current/rest-api/rest-node-provisioning.html
 */

@Slf4j
@Profile("dev")
@Configuration("initializeCouchbase")
public class CouchbaseDevInitialization implements CouchbaseInitialization {

    private static final String BASE_URL_PATTERN = "http://{0}:{1}/";
    private static final String COUCHBASE_DEFAULT_PORT = "8091";

    private static final String COUCHBASE_DEFAULT_ADMIN_CREDENTIALS = "Administrator:password";
    private static final String HEADER_NAME_AUTHORIZATION = "Authorization";

    // Initialize Node
    private static final String INIT_NODE_URL = "nodes/self/controller/settings";
    private static final String INIT_NODE_PATH_KEY = "path";
    private static final String INIT_NODE_PATH_VALUE = "/opt/couchbase/var/lib/couchbase/data"; //NOSONAR
    private static final String INIT_NODE_INDEX_PATH_KEY = "index_path";
    private static final String INIT_NODE_INDEX_PATH_VALUE = "/opt/couchbase/var/lib/couchbase/data"; //NOSONAR

    // Rename Node
    private static final String RENAME_NODE_URL = "node/controller/rename";
    private static final String RENAME_NODE_HOSTNAME = "hostname";

    // Setup Services
    private static final String SETUP_SERVICE_URL = "node/controller/setupServices";
    private static final String SETUP_SERVICES_KEY = "services";
    private static final String SETUP_SERVICES_VALUE = "kv,index,n1ql,fts";

    // Setup Bucket
    private static final String SETUP_BUCKET_URL = "pools/default/buckets";
    private static final String SETUP_BUCKET_FLUSH_ENABLED_KEY = "flushEnabled";
    private static final String SETUP_BUCKET_FLUSH_ENABLED_VALUE = "1";
    private static final String SETUP_BUCKET_THREADS_NUMBER_KEY = "threadsNumber";
    private static final String SETUP_BUCKET_THREADS_NUMBER_VALUE = "3";
    private static final String SETUP_BUCKET_REPLICA_INDEX_KEY = "replicaIndex";
    private static final String SETUP_BUCKET_REPLICA_INDEX_VALUE = "0";
    private static final String SETUP_BUCKET_REPLICA_NUMBER_KEY = "replicaNumber";
    private static final String SETUP_BUCKET_REPLICA_NUMBER_VALUE = "0";
    private static final String SETUP_BUCKET_EVICTION_POLICY_KEY = "flushEnabled";
    private static final String SETUP_BUCKET_EVICTION_POLICY_VALUE = "valueOnly";
    private static final String SETUP_BUCKET_RAM_QUOTA_MB_KEY = "ramQuotaMB";
    private static final String SETUP_BUCKET_RAM_QUOTA_MB_VALUE = "101";
    private static final String SETUP_BUCKET_TYPE_KEY = "bucketType";
    private static final String SETUP_BUCKET_TYPE_VALUE = "membase";
    private static final String SETUP_BUCKET_NAME_KEY = "name";

    // Setup Admin
    private static final String SETUP_ADMIN_URL = "settings/web";
    private static final String SETUP_ADMIN_USER_NAME_KEY = "username";
    private static final String SETUP_ADMIN_PASSWORD_KEY = "password"; //NOSONAR
    private static final String SETUP_ADMIN_PORT_KEY = "port";
    private static final String SETUP_ADMIN_PORT_VALUE = "SAME";

    private RestService restService;
    private CouchbaseProperties couchbaseProperties;

    @Autowired
    public CouchbaseDevInitialization(RestService restService, CouchbaseProperties couchbaseProperties) {
        this.couchbaseProperties = couchbaseProperties;
        this.restService = restService;
    }

    @Override
    @PostConstruct
    public void initCouchBase() {

        // TODO check connection with couchbase bucket before starting initialization

        String host = couchbaseProperties.getBootstrapHosts().get(0);
        String baseUrl = MessageFormat.format(BASE_URL_PATTERN, host, COUCHBASE_DEFAULT_PORT);

        // Initialize Node
        MultiValueMap<String, String> multiValueMap = new LinkedMultiValueMap<>();
        multiValueMap.add(INIT_NODE_PATH_KEY, INIT_NODE_PATH_VALUE);
        multiValueMap.add(INIT_NODE_INDEX_PATH_KEY, INIT_NODE_INDEX_PATH_VALUE);

        sendRequest(baseUrl + INIT_NODE_URL, multiValueMap);

        // Rename Node
        multiValueMap = new LinkedMultiValueMap<>();
        multiValueMap.add(RENAME_NODE_HOSTNAME, couchbaseProperties.getBootstrapHosts().get(0));

        sendRequest(baseUrl + RENAME_NODE_URL, multiValueMap);

        // Setup Services
        multiValueMap = new LinkedMultiValueMap<>();
        multiValueMap.add(SETUP_SERVICES_KEY, SETUP_SERVICES_VALUE);

        sendRequest(baseUrl + SETUP_SERVICE_URL, multiValueMap);

        // Setup Bucket
        multiValueMap = new LinkedMultiValueMap<>();
        multiValueMap.add(SETUP_BUCKET_FLUSH_ENABLED_KEY, SETUP_BUCKET_FLUSH_ENABLED_VALUE);
        multiValueMap.add(SETUP_BUCKET_THREADS_NUMBER_KEY, SETUP_BUCKET_THREADS_NUMBER_VALUE);
        multiValueMap.add(SETUP_BUCKET_REPLICA_INDEX_KEY, SETUP_BUCKET_REPLICA_INDEX_VALUE);
        multiValueMap.add(SETUP_BUCKET_REPLICA_NUMBER_KEY, SETUP_BUCKET_REPLICA_NUMBER_VALUE);
        multiValueMap.add(SETUP_BUCKET_EVICTION_POLICY_KEY, SETUP_BUCKET_EVICTION_POLICY_VALUE);
        multiValueMap.add(SETUP_BUCKET_RAM_QUOTA_MB_KEY, SETUP_BUCKET_RAM_QUOTA_MB_VALUE);
        multiValueMap.add(SETUP_BUCKET_TYPE_KEY, SETUP_BUCKET_TYPE_VALUE);
        multiValueMap.add(SETUP_BUCKET_NAME_KEY, couchbaseProperties.getBucket().getName());

        sendRequest(baseUrl + SETUP_BUCKET_URL, multiValueMap);

        // Setup Admin
        multiValueMap = new LinkedMultiValueMap<>();
        multiValueMap.add(SETUP_ADMIN_USER_NAME_KEY, couchbaseProperties.getBucket().getName());
        multiValueMap.add(SETUP_ADMIN_PASSWORD_KEY, couchbaseProperties.getBucket().getPassword());
        multiValueMap.add(SETUP_ADMIN_PORT_KEY, SETUP_ADMIN_PORT_VALUE);

        sendRequest(baseUrl + SETUP_ADMIN_URL, multiValueMap);
    }

    private void sendRequest(String url, MultiValueMap<String, String> multiValueMap) {
        log.info("Request URL : " + url);
        for (Map.Entry<String, List<String>> entry : multiValueMap.entrySet()) {
            log.debug(MessageFormat.format("Key ->:{0}, Value -> {1}", entry.getKey(), entry.getValue()));
        }
        try {
            HttpEntity<MultiValueMap<String, String>> httpEntity = new HttpEntity<>(multiValueMap, authHeader());
            ResponseEntity<Object> responseEntity = restService.getRestTemplate().exchange(url, HttpMethod.POST, httpEntity, Object.class);
            log.info("HTTP response value: {} and phrase: {}", responseEntity.getStatusCode().value(), responseEntity.getStatusCode().getReasonPhrase());
        } catch (RestClientException restClientException) {
            log.warn("Exception while performing POST request [{}] with error :{}", url, restClientException.getMessage());
            log.trace("Exception stack trace : ", restClientException);
        }
    }

    private HttpHeaders authHeader() {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        byte[] plainCredsBytes = COUCHBASE_DEFAULT_ADMIN_CREDENTIALS.getBytes();
        byte[] base64CredsBytes = Base64.encodeBase64(plainCredsBytes);
        String base64Creds = new String(base64CredsBytes);

        headers.add(HEADER_NAME_AUTHORIZATION, "Basic " + base64Creds);
        return headers;
    }
}

