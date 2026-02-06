package com.example.config;

import io.quarkus.runtime.StartupEvent;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.event.Observes;
import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.jboss.logging.Logger;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

@ApplicationScoped
public class DatabaseInitializer {

    private static final Logger LOG = Logger.getLogger(DatabaseInitializer.class);

    @ConfigProperty(name = "quarkus.datasource.username")
    String username;

    @ConfigProperty(name = "quarkus.datasource.password")
    String password;

    @ConfigProperty(name = "quarkus.datasource.jdbc.url")
    String jdbcUrl;

    void onStart(@Observes StartupEvent ev) {
        createSchemaIfNotExists();
    }

    private void createSchemaIfNotExists() {
        // Extract schema name from JDBC URL (after currentSchema=)
        String schemaName = extractSchemaName(jdbcUrl);
        
        if (schemaName == null) {
            LOG.info("No schema specified in JDBC URL, using default");
            return;
        }
        
        try (Connection conn = DriverManager.getConnection(jdbcUrl, username, password);
             Statement stmt = conn.createStatement()) {
            
            // Check if schema exists
            String checkQuery = String.format(
                "SELECT 1 FROM information_schema.schemata WHERE schema_name = '%s'", schemaName);
            
            ResultSet rs = stmt.executeQuery(checkQuery);
            
            if (!rs.next()) {
                // Schema doesn't exist, create it
                String createQuery = String.format("CREATE SCHEMA %s", schemaName);
                stmt.executeUpdate(createQuery);
                LOG.infof("Schema '%s' created successfully", schemaName);
            } else {
                LOG.infof("Schema '%s' already exists", schemaName);
            }
            
        } catch (Exception e) {
            LOG.errorf("Error creating schema: %s", e.getMessage());
        }
    }

    private String extractSchemaName(String jdbcUrl) {
        // Extract schema name from jdbc:postgresql://localhost:5432/projedata?currentSchema=inventory_db
        int schemaIndex = jdbcUrl.indexOf("currentSchema=");
        if (schemaIndex < 0) {
            return null;
        }
        
        int start = schemaIndex + "currentSchema=".length();
        int end = jdbcUrl.indexOf('&', start);
        
        if (end > 0) {
            return jdbcUrl.substring(start, end);
        }
        return jdbcUrl.substring(start);
    }
}
