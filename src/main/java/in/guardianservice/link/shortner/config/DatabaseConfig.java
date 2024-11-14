package in.guardianservice.link.shortner.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.MongoDatabaseFactory;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.SimpleMongoClientDatabaseFactory;
import org.springframework.data.mongodb.core.convert.DbRefResolver;
import org.springframework.data.mongodb.core.convert.DefaultDbRefResolver;
import org.springframework.data.mongodb.core.convert.MappingMongoConverter;
import org.springframework.data.mongodb.core.mapping.MongoMappingContext;
import org.springframework.data.mongodb.gridfs.GridFsTemplate;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

@Configuration
public class DatabaseConfig {

    private static final Logger logger = LoggerFactory.getLogger(DatabaseConfig.class);


    //  Mongo DB connection
    private static String mongoUri = "";
    public static final String MONGO_URI = "MONGO_URI";
    private static final String URL_SHORTNER_PROPERTIES_PATH = "/opt/configs/urlShortner.properties";

    @Value("${mongo.spring.uri:}")
    private String mongoDevUri;

    static {
        Properties properties = fetchProperties(URL_SHORTNER_PROPERTIES_PATH);
        if (null != properties) {
            mongoUri = properties.getProperty(MONGO_URI);
        }
    }

    private static Properties fetchProperties(String urlShortnerPropertiesPath) {

        Properties properties = new Properties();
        try {
            properties.load(new FileInputStream(urlShortnerPropertiesPath));
            return properties;
        } catch (IOException e) {
            logger.error("Exception occurred while getting url short config with probable cause - ", e);
            return null;
        }
    }

    @Bean
    public MongoDatabaseFactory mongoDbFactory() {
        String effectiveMongoUri = (mongoUri != null && !mongoUri.isEmpty()) ? mongoUri : mongoDevUri;
        if (effectiveMongoUri == null || effectiveMongoUri.isEmpty()) {
            throw new IllegalArgumentException("MongoDB URI not specified in properties file or profile configuration.");
        }
        return new SimpleMongoClientDatabaseFactory(effectiveMongoUri);
    }

    @Bean
    public MongoTemplate mongoTemplate() {
        return new MongoTemplate(mongoDbFactory());
    }

    @Bean
    public GridFsTemplate gridFsTemplate() throws Exception {
        return new GridFsTemplate(mongoDbFactory(), mappingMongoConverter());
    }

    @Bean
    public MappingMongoConverter mappingMongoConverter() throws Exception {
        DbRefResolver dbRefResolver = new DefaultDbRefResolver(mongoDbFactory());
        MappingMongoConverter converter = new MappingMongoConverter(dbRefResolver, new MongoMappingContext());
        // Add any custom conversions if required
        return converter;
    }
}
