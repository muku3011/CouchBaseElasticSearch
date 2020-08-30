package com.learn.java.initializer.elasticsearch;

import lombok.extern.slf4j.Slf4j;
import org.elasticsearch.ElasticsearchStatusException;
import org.elasticsearch.action.ActionListener;
import org.elasticsearch.action.admin.indices.alias.Alias;
import org.elasticsearch.action.support.ActiveShardCount;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.client.indices.CreateIndexRequest;
import org.elasticsearch.client.indices.CreateIndexResponse;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.unit.TimeValue;
import org.elasticsearch.common.xcontent.XContentType;
import org.elasticsearch.index.query.QueryBuilders;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

import javax.annotation.PostConstruct;
import java.io.IOException;

@Slf4j
@Profile("dev")
@Configuration("initializeElasticSearch")
public class ElasticSearchDevInitialization implements ElasticSearchInitialization {

    @Autowired
    private RestHighLevelClient highLevelClient;

    @Override
    @PostConstruct
    public void initElasticSearch() throws IOException {

        try {
            // A CreateIndexRequest requires an index argument, The index to create
            CreateIndexRequest request = new CreateIndexRequest("couch-elastic");

            // Each index created can have specific settings associated with it.
            request.settings(Settings.builder()
                .put("index.number_of_shards", 3)
                .put("index.number_of_replicas", 2)
            );

            // An index may be created with mappings for its document types
            request.mapping(
                "{\n" +
                    "      \"properties\": {\n" +
                    "        \"type\": { \"type\": \"keyword\" }, \n" +
                    "        \"name\": { \"type\": \"text\" },\n" +
                    "        \"user_name\": { \"type\": \"keyword\" },\n" +
                    "        \"email\": { \"type\": \"keyword\" },\n" +
                    "        \"content\": { \"type\": \"text\" },\n" +
                    "        \"tweeted_at\": { \"type\": \"date\" }\n" +
                    "      }\n" +
                    "    }",
                XContentType.JSON);

            // Aliases can be set at index creation time
            request.alias(new Alias("couch-elastic_alias").filter(QueryBuilders.termQuery("user", "kimchy")));

            // Timeout to wait for the all the nodes to acknowledge the index creation as a TimeValue
            request.setTimeout(TimeValue.timeValueMinutes(2));

            // Timeout to connect to the master node as a TimeValue
            request.setMasterTimeout(TimeValue.timeValueMinutes(1));

            // The number of active shard copies to wait for before the create index API returns a response
            request.waitForActiveShards(ActiveShardCount.DEFAULT);

            // Synchronous execution
            CreateIndexResponse createIndexResponse = createSyncIndex(request);
            indexCreationResponseHandler(createIndexResponse);

            // As-Synchronous execution
            //createAsyncIndex(request);

        } catch (ElasticsearchStatusException exception) {
            log.info("Elasticsearch's initialization stopped, HTTP response: {}, and message: {}", exception.status().getStatus(), exception.getMessage());
        }
    }

    private CreateIndexResponse createSyncIndex(CreateIndexRequest request) throws IOException {
        return highLevelClient.indices().create(request, RequestOptions.DEFAULT);
    }

    /**
     * As-Synchronous index creation request
     *
     * @param request CreateIndexRequest
     */
    private void createAsyncIndex(CreateIndexRequest request) {

        // Listener for receiving asynchronous response of the request
        ActionListener<CreateIndexResponse> listener =
            new ActionListener<CreateIndexResponse>() {
                @Override
                public void onResponse(CreateIndexResponse createIndexResponse) {
                    indexCreationResponseHandler(createIndexResponse);
                }

                @Override
                public void onFailure(Exception e) {
                    log.error("Index creation failed with message: {}", e.getMessage());
                    log.trace("Detailed exception :", e);
                }
            };

        // A-Synchronous execution
        highLevelClient.indices().createAsync(request, RequestOptions.DEFAULT, listener);
    }

    /**
     * Handle CreateIndexResponse
     *
     * @param createIndexResponse response for index creation
     */
    private void indexCreationResponseHandler(CreateIndexResponse createIndexResponse) {
        log.info("acknowledged: {}", createIndexResponse.isAcknowledged());
        log.info("shardsAcknowledged: {}", createIndexResponse.isShardsAcknowledged());
    }
}
