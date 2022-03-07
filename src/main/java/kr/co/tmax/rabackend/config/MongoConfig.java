//package kr.co.tmax.rabackend.config;
//
//import com.mongodb.ConnectionString;
//import com.mongodb.MongoClientSettings;
//import com.mongodb.client.MongoClient;
//import com.mongodb.client.MongoClients;
//import kr.co.tmax.rabackend.domain.simulation.SimulationService;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.ComponentScan;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.data.mongodb.MongoDatabaseFactory;
//import org.springframework.data.mongodb.MongoTransactionManager;
//import org.springframework.data.mongodb.config.AbstractMongoClientConfiguration;
//import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;
//
//@Configuration
////@ComponentScan(basePackages = "kr.co.tmax.rabackend.domain")
////@EnableMongoRepositories(basePackages = "kr.co.tmax.rabackend.domain")
//public class MongoConfig extends AbstractMongoClientConfiguration {
//
//    @Bean
//    MongoTransactionManager transactionManager(MongoDatabaseFactory dbFactory) {
//        return new MongoTransactionManager(dbFactory);
//    }
//
//    @Override
//    protected String getDatabaseName() {
//        return "ra";
//    }
//
//    @Override
//    public MongoClient mongoClient() {
//        final ConnectionString connectionString = new ConnectionString("mongodb://mongo1:27017,mongo2:27018,mongo3:27019/?replicaSet=rs0&retryWrties=true");
//        final MongoClientSettings mongoClientSettings = MongoClientSettings.builder()
//                .applyConnectionString(connectionString)
//                .build();
//        return MongoClients.create(mongoClientSettings);
//    }
//}
