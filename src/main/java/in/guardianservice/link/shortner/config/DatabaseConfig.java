package in.guardianservice.link.shortner.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import javax.sql.DataSource;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

import org.springframework.jdbc.datasource.DriverManagerDataSource;


@Configuration
public class DatabaseConfig {

    private static final Logger logger = LoggerFactory.getLogger(DatabaseConfig.class);


    private static String postgresUri = "";
    private static String username = "";
    private static String password = "";
    public static final String POSTGRES_URI = "POSTGRES_URI";
    public static final String USER_NAME = "USER_NAME";
    public static final String RAW_PASSWORD = "RAW_PASSWORD";
    private static final String URL_SHORTNER_PROPERTIES_PATH = "/opt/configs/urlShortner.properties";

    static {
        Properties properties = fetchProperties();
        if (null != properties) {
            postgresUri = properties.getProperty(POSTGRES_URI);
            username = properties.getProperty(USER_NAME);
            password = properties.getProperty(RAW_PASSWORD);
        }
    }

    private static Properties fetchProperties() {

        Properties properties = new Properties();
        try {
            properties.load(new FileInputStream(DatabaseConfig.URL_SHORTNER_PROPERTIES_PATH));
            return properties;
        } catch (IOException e) {
            logger.error("Exception occurred while getting url short config with probable cause - ", e);
            return null;
        }
    }

    @Bean
    public DataSource dataSource() {
        DriverManagerDataSource dataSource = new DriverManagerDataSource();

        dataSource.setDriverClassName("org.postgresql.Driver");
        dataSource.setUrl(postgresUri);
        dataSource.setUsername(username);
        dataSource.setPassword(password);

        return dataSource;
    }
}
