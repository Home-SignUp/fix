package com.win.test.execution.listener;

import com.mongodb.BasicDBList;
import com.mongodb.util.JSON;
import org.bson.types.BasicBSONList;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.test.context.TestContext;
import org.springframework.test.context.support.AbstractTestExecutionListener;
import org.springframework.util.FileCopyUtils;

import java.io.FileReader;


public class MongoDBExecutionListener extends AbstractTestExecutionListener {

    private MongoTemplate mongoTemplate;
    private ResourceLoader resourceLoader;

    @Override
    public void prepareTestInstance(TestContext testContext) throws Exception {
        mongoTemplate = testContext.getApplicationContext().getBean(MongoTemplate.class);
    }

    @Override
    public void beforeTestMethod(TestContext testContext) throws Exception {
        DataSet dataSet = getDataSet(testContext);
        Collection collection = getCollection(testContext);
        if (dataSet != null && collection != null) {
            mongoTemplate.dropCollection(collection.name());
            BasicDBList parsed = (BasicDBList) JSON.parse(FileCopyUtils.copyToString(new FileReader(
                    new ClassPathResource("data/sets/events/special/collection.json").getFile())));
            mongoTemplate.insert(parsed, collection.name());
        }
    }

    private DataSet getDataSet(TestContext testContext) {
        if (testContext.getTestMethod().isAnnotationPresent(DataSet.class)) {
            return testContext.getTestMethod().getAnnotation(DataSet.class);
        } else if (testContext.getTestClass().isAnnotationPresent(DataSet.class)) {
            return testContext.getTestClass().getAnnotation(DataSet.class);
        } else {
            return null;
        }
    }

    private Collection getCollection(TestContext testContext) {
        if (testContext.getTestMethod().isAnnotationPresent(Collection.class)) {
            return testContext.getTestMethod().getAnnotation(Collection.class);
        } else if (testContext.getTestClass().isAnnotationPresent(Collection.class)) {
            return testContext.getTestClass().getAnnotation(Collection.class);
        } else {
            return null;
        }
    }

    @Override
    public void afterTestMethod(TestContext testContext) throws Exception {
        Collection collection = getCollection(testContext);
        if (collection != null) {
            mongoTemplate.dropCollection(collection.name());
        }
    }
}
