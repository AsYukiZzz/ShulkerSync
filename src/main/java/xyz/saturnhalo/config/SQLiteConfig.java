package xyz.saturnhalo.config;

import com.zaxxer.hikari.HikariDataSource;
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;

/**
 * SQLite 配置
 */
@Configuration
public class SQLiteConfig {

    @Bean
    public DataSource dataSource(DataSourceProperties properties) {
        HikariDataSource dataSource = new HikariDataSource();
        dataSource.setJdbcUrl(properties.getUrl());
        dataSource.setDriverClassName(properties.getDriverClassName());
        dataSource.setUsername(dataSource.getUsername());
        dataSource.setPassword(dataSource.getPassword());

        // 1. SQLite日志模式：WAL
        dataSource.addDataSourceProperty("journal_mode", "WAL");

        // 2. SQLite同步模式：Normal
        dataSource.addDataSourceProperty("synchronous", "NORMAL");

        return dataSource;
    }
}