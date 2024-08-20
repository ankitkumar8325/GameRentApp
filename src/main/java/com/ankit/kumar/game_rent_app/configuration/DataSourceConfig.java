package com.ankit.kumar.game_rent_app.configuration;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;

@Configuration
public class DataSourceConfig {
    @Bean
    @Qualifier("myDataSource")
    public DataSource dataSource() {
        return DataSourceBuilder.create()
                .url("jdbc:h2:file:~/ankitKumarDb2")
                .build();
    }
}
