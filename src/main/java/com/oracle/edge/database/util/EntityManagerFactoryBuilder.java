package com.oracle.edge.database.util;

import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;

import java.util.HashMap;
import java.util.Map;

import io.helidon.config.Config;
import io.helidon.config.ConfigValue;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * This utility class simplifies the creation of an {@code EntityManagerFactory}
 * from simple key/value pairs or a <i>Helidon</i> {@code Config} object.
 */
public final class EntityManagerFactoryBuilder {
    
    private static final Logger LOGGER = LoggerFactory.getLogger(EntityManagerFactoryBuilder.class);
    
    static final String JDBC_DRIVER = "jakarta.persistence.jdbc.driver";
    static final String JDBC_URL = "jakarta.persistence.jdbc.url";
    static final String JDBC_USERNAME = "jakarta.persistence.jdbc.user";
    static final String JDBC_PASSWORD = "jakarta.persistence.jdbc.password";
    
    private static final String HELIDON_DATASOURCE_PREFIX = "javax.sql.DataSource";
    private static final String HELIDON_DATASOURCE_DRIVERCLASSNAME_SUFFIX = "driverClassName";
    private static final String HELIDON_DATASOURCE_URL_SUFFIX = "url";
    private static final String HELIDON_DATASOURCE_USER_SUFFIX = "user";
    private static final String HELIDON_DATASOURCE_PASSWORD_SUFFIX = "password";
    private static final String HELIDON_DATASOURCE_ADDITIONAL_ELEMENTS_SUFFIX = "_additionalElements";
    
    private final String dataSourceName;
    
    private final Map<String, String> persistenceConfig = new HashMap<>();
    
    public EntityManagerFactoryBuilder(final String theDataSourceName) {
        dataSourceName = theDataSourceName;
    }
    
    public EntityManagerFactoryBuilder property(
            final String key, final String value) {
        persistenceConfig.put(key, value);
        return this;
    }
    
    private String helidonConfigPropertyName(final String suffix) {
        return String.format("%s.%s.%s",
                HELIDON_DATASOURCE_PREFIX,
                dataSourceName,
                suffix);
    }
    
    private void helidonConfigProperty(
            final Config config,
            final String helidonConfigPropertyName,
            final String persistenceConfigPropertyName) {
        ConfigValue<String> helidonConfigValue
                = config.get(helidonConfigPropertyName).asString();
        if (helidonConfigValue.isPresent()) {
            property(persistenceConfigPropertyName, 
                    helidonConfigValue.get());
        }
    }
    
    public EntityManagerFactoryBuilder helidonConfig(final Config config) {
        helidonConfigProperty(
                config,
                helidonConfigPropertyName(HELIDON_DATASOURCE_DRIVERCLASSNAME_SUFFIX),
                JDBC_DRIVER);
        helidonConfigProperty(
                config,
                helidonConfigPropertyName(HELIDON_DATASOURCE_URL_SUFFIX),
                JDBC_URL);
        helidonConfigProperty(
                config,
                helidonConfigPropertyName(HELIDON_DATASOURCE_USER_SUFFIX),
                JDBC_USERNAME);
        helidonConfigProperty(
                config,
                helidonConfigPropertyName(HELIDON_DATASOURCE_PASSWORD_SUFFIX),
                JDBC_PASSWORD);
        
        ConfigValue<String> additionalElements
                = config.get(helidonConfigPropertyName(
                        HELIDON_DATASOURCE_ADDITIONAL_ELEMENTS_SUFFIX)).asString();
        if (additionalElements.isPresent()) {
            for (String additionalElement : additionalElements.get().split(",")) {
                helidonConfigProperty(
                        config,
                        helidonConfigPropertyName(additionalElement),
                        additionalElement);
            }
        }
        return this;
    }
    
    public EntityManagerFactory build() {
        LOGGER.debug("build() ...");
        try {
            return Persistence.createEntityManagerFactory(
                    dataSourceName, persistenceConfig);
        } finally {
            LOGGER.debug("build() ... done.");
        }
    }
    
}
