package com.mycompany.hotel.db.config;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import liquibase.Liquibase;
import liquibase.database.Database;
import liquibase.database.DatabaseFactory;
import liquibase.database.jvm.JdbcConnection;
import liquibase.resource.ClassLoaderResourceAccessor;

import javax.sql.DataSource;
import java.sql.Connection;


public class DatabaseConfig {
    
    private final HikariDataSource dataSource;
    
    /**
     * Конструктор инициализирует connection pool и схему БД
     * @param jdbcUrl URL подключения (например: jdbc:postgresql://localhost:5432/mydb)
     * @param username имя пользователя БД
     * @param password пароль
     */
    public DatabaseConfig(String jdbcUrl, String username, String password) {
        HikariConfig config = new HikariConfig();
        config.setJdbcUrl(jdbcUrl);
        config.setUsername(username);
        config.setPassword(password);
        
        
        config.setMaximumPoolSize(10);           
        config.setMinimumIdle(2);                
        config.setConnectionTimeout(30000);      
        config.setIdleTimeout(600000);           
        config.setMaxLifetime(1800000);          
        
        
        config.setAutoCommit(true);
        config.setConnectionTestQuery("SELECT 1"); 
        

        this.dataSource = new HikariDataSource(config);
        

        initializeSchema();
    }
    
    
    private void initializeSchema() {
        try (Connection connection = dataSource.getConnection()) {
            Database database = DatabaseFactory.getInstance()
                .findCorrectDatabaseImplementation(new JdbcConnection(connection));
            
            Liquibase liquibase = new Liquibase(
                "db/changelog/db-changelog-master.xml",
                new ClassLoaderResourceAccessor(),
                database
            );
            
            liquibase.update("");
            
            System.out.println("Database schema initialized successfully");
            
        } catch (Exception e) {
            throw new RuntimeException("Failed to initialize database schema: " + e.getMessage(), e);
        }
    }
    
    public DataSource getDataSource() {
        return dataSource;
    }
    
    
    public void close() {
        if (dataSource != null && !dataSource.isClosed()) {
            dataSource.close();
            System.out.println("Database connection pool closed");
        }
    }
}
