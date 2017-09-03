package com.win.server.config;

import com.mongodb.Mongo;
import com.mongodb.MongoClient;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

import java.net.UnknownHostException;

@Configuration
@EnableMongoRepositories(basePackages = {"com.win.slots.backend.dao", "com.win.server.backend.common.dao"})
public class MongoLocalTestConfiguration {

    private String dbName = "test_slots";
    private String dbHost = "localhost";

    @Bean
    public Mongo mongo() throws UnknownHostException {
        return new MongoClient(dbHost);
    }

    @Bean
    @Primary
    public MongoTemplate mongoTemplate()
            throws UnknownHostException
    {
        return new MongoTemplate(mongo(), dbName);
    }

    @Bean @Qualifier("saleEventsMongoTemplate")
    public MongoTemplate saleEventsMongoTemplate()
            throws UnknownHostException
    {
        return new MongoTemplate(mongo(), dbName);
    }

}
