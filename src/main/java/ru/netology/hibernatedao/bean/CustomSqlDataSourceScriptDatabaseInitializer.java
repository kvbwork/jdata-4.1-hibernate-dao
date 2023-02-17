package ru.netology.hibernatedao.bean;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.sql.init.SqlDataSourceScriptDatabaseInitializer;
import org.springframework.boot.autoconfigure.sql.init.SqlInitializationProperties;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.SQLException;

@Primary
@Component
@ConditionalOnProperty(name = "spring.sql.init.mode", havingValue = "always")
public class CustomSqlDataSourceScriptDatabaseInitializer extends SqlDataSourceScriptDatabaseInitializer {
    private static final Logger logger = LoggerFactory.getLogger(CustomSqlDataSourceScriptDatabaseInitializer.class);

    @Value("${app.sql.init.marker-table}")
    private String markerTableName;

    public CustomSqlDataSourceScriptDatabaseInitializer(DataSource dataSource, SqlInitializationProperties properties) {
        super(dataSource, properties);
    }

    private boolean hasTable(String tableName) {
        try (Connection connection = getDataSource().getConnection()) {
            DatabaseMetaData metaData = connection.getMetaData();
            try (ResultSet resultSet = metaData.getTables(null, null, tableName, new String[]{"TABLE"})) {
                return resultSet.next();
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public boolean initializeDatabase() {
        if (hasTable(markerTableName)) {
            logger.info("table '{}' already exists. Initialization was skipped.", markerTableName);
            return true;
        }
        logger.info("table '{}' not found. Initialize database.", markerTableName);
        return super.initializeDatabase();
    }

}
