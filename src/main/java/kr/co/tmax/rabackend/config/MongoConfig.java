//package kr.co.tmax.rabackend.config;
//
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.data.mongodb.MongoDbFactory;
//
//@Configuration
//@Slf4j
//public class MongoConfig {
//    /**
//     *Mongo client parameter configuration * * @ return
//     */
//    public MongoClientOptions mongoClientOptions(MongoClientOptionProperties properties) {
//        return MongoClientOptions.builder()
//                .connectTimeout(properties.getConnectionTimeoutMs())
//                .socketTimeout(properties.getReadTimeoutMs()).applicationName(properties.getClientName())
//                .heartbeatConnectTimeout(properties.getHeartbeatConnectionTimeoutMs())
//                .heartbeatSocketTimeout(properties.getHeartbeatReadTimeoutMs())
//                .heartbeatFrequency(properties.getHeartbeatFrequencyMs())
//                .minHeartbeatFrequency(properties.getMinHeartbeatFrequencyMs())
//                .maxConnectionIdleTime(properties.getConnectionMaxIdleTimeMs())
//                .maxConnectionLifeTime(properties.getConnectionMaxLifeTimeMs())
//                .maxWaitTime(properties.getPoolMaxWaitTimeMs())
//                .connectionsPerHost(properties.getConnectionsPerHost())
//                .threadsAllowedToBlockForConnectionMultiplier(
//                        properties.getThreadsAllowedToBlockForConnectionMultiplier())
//                .minConnectionsPerHost(properties.getMinConnectionsPerHost()).build();
//    }
//    @Bean
//    public MappingMongoConverter mappingMongoConverter(MongoDbFactory factory, MongoMappingContext context, BeanFactory beanFactory) {
//        DbRefResolver dbRefResolver = new DefaultDbRefResolver(factory);
//        MappingMongoConverter mappingConverter = new MappingMongoConverter(dbRefResolver, context);
//        try {
//            mappingConverter.setCustomConversions(beanFactory.getBean(MongoCustomConversions.class));
//        } catch (NoSuchBeanDefinitionException ignore) {
//        }
//        // Don't save _class to dao
//        mappingConverter.setTypeMapper(new DefaultMongoTypeMapper(null));
//        return mappingConverter;
//    }
//}