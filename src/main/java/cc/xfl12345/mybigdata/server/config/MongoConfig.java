package cc.xfl12345.mybigdata.server.config;

import com.mongodb.MongoCommandException;
import com.mongodb.ReadConcern;
import com.mongodb.TransactionOptions;
import com.mongodb.WriteConcern;
import com.mongodb.client.MongoClient;
import dev.morphia.Datastore;
import dev.morphia.Morphia;
import lombok.extern.slf4j.Slf4j;
import org.bson.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.mongo.MongoProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@Slf4j
public class MongoConfig {
    @Bean(name = "mongoDatastore")
    public Datastore getDatastore(MongoClient mongoClient, MongoProperties mongoProperties) {
        Datastore datastore = Morphia.createDatastore(
            mongoClient,
            mongoProperties.getMongoClientDatabase()
        );
        // tell Morphia where to find your classes
        // can be called multiple times with different packages or classes
        datastore.getMapper().mapPackage(
            cc.xfl12345.mybigdata.server.model.database.table.pojo.StringContent.class.getPackageName()
        );

        datastore.ensureIndexes();

        return datastore;
    }

    @Bean(name = "mongodbDefaultTransactionOptions")
    public TransactionOptions getTransactionOptions(MongoClient mongoClient) {
        TransactionOptions.Builder builder = TransactionOptions.builder();
        try {
            Document replSetGetStatus = mongoClient.getDatabase("admin").runCommand(
                new Document("replSetGetStatus",1)
            );

            if (replSetGetStatus.getInteger("writeMajorityCount") > 0) {
                builder.writeConcern(WriteConcern.MAJORITY).readConcern(ReadConcern.MAJORITY);
            }
        } catch (MongoCommandException e) {
            switch (e.getErrorCode()) {
                case 76:
                    log.info("MongoDB is not running with '--replSet'. So it not support 'WriteConcern.MAJORITY'.");
                    break;
            }
            builder.readConcern(ReadConcern.SNAPSHOT);
        }

        return builder.build();
    }
}
