package com.win.config;

import com.mongodb.MongoClient;
import org.apache.commons.dbcp.BasicDataSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.MongoDbFactory;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.SimpleMongoDbFactory;
import org.springframework.instrument.classloading.InstrumentationLoadTimeWeaver;
import org.springframework.instrument.classloading.LoadTimeWeaver;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;

import javax.sql.DataSource;

@Configuration
public class AppConfig {

    @Bean
    public MongoDbFactory mongoDbFactory()
            throws Exception
    {
        return new SimpleMongoDbFactory(new MongoClient("mongo-0-slots",27017), "dev_slots");
    }

    @Bean
    public MongoTemplate mongoTemplate()
            throws Exception
    {
        MongoTemplate mongoTemplate = new MongoTemplate(mongoDbFactory());
        return mongoTemplate;
    }

    @Bean
    public MongoTemplate saleEventsMongoTemplate()
            throws Exception
    {
        MongoTemplate mongoTemplate = new MongoTemplate(mongoDbFactory());
        return mongoTemplate;
    }

    @Bean
    public LocalContainerEntityManagerFactoryBean entityManagerFactory() {
        LocalContainerEntityManagerFactoryBean lcemfb = new LocalContainerEntityManagerFactoryBean();
        lcemfb.setDataSource(dataSource());
        lcemfb.setPersistenceUnitName("persistenceUnit");
        LoadTimeWeaver loadTimeWeaver = new InstrumentationLoadTimeWeaver();
        lcemfb.setLoadTimeWeaver(loadTimeWeaver);
        return lcemfb;
    }

    @Bean
    public DataSource dataSource() {
        BasicDataSource dataSource = new BasicDataSource();
        dataSource.setDriverClassName("org.postgresql.Driver");
        dataSource.setUrl("jdbc:postgresql://psql-0-slots:5432/dev_slots_admin_db");
        dataSource.setUsername("slots");
        dataSource.setPassword("slots");
        return dataSource;
    }

}
