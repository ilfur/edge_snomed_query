package com.oracle.edge.snomed;

import com.oracle.edge.database.util.EntityManagerFactoryBuilder;
import io.helidon.config.Config;
import io.helidon.config.ConfigValue;

import jakarta.annotation.PostConstruct;

import jakarta.enterprise.context.ApplicationScoped;

import jakarta.enterprise.inject.Produces;

import jakarta.inject.Inject;

import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;

import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 */
@ApplicationScoped
public class SnomedQueriesConfig {
    
    private static final Logger LOGGER = LoggerFactory.getLogger(
            SnomedQueriesConfig.class);
    
    private Config config;
    
    @Inject
    public SnomedQueriesConfig(final Config theConfig) {
        config = theConfig;
    }

    @Produces
    private DatabaseAccessHandler dbAccessHandler;
    
    @PostConstruct
    public void postConstruct() {
        LOGGER.debug("postConstruct() ...");
        
        try {
           EntityManagerFactory entityManagerFactory
                    = new EntityManagerFactoryBuilder("SnomedUnit")
                            .helidonConfig(config)
                            .build();

            LOGGER.debug("postConstruct(): entityManagerFactory created");

            dbAccessHandler = new DatabaseAccessHandler(
                    entityManagerFactory);
            

            LOGGER.debug("postConstruct(): dbAccessHandler created");
        } catch (Exception e) {
            LOGGER.error("SnomedQueriesConfig.postConstruct() failed.", e);
            throw e;
        }

        LOGGER.debug("postContstruct() ... done.");
    }
}
