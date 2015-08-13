package com.github.lambda;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.jdbc.datasource.init.DatabasePopulator;
import org.springframework.jdbc.datasource.init.DatabasePopulatorUtils;
import org.springframework.jdbc.datasource.init.ResourceDatabasePopulator;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@Configuration
@EnableTransactionManagement(proxyTargetClass = true)
@ComponentScan("com.github.lambda")
public class TestAppConfig {

    @Bean
    public DriverManagerDataSource getDataSource() {
        DriverManagerDataSource source = createDataSource();
        DatabasePopulatorUtils.execute(createDatabasePopulator(), source);
        return source;
    }

    private DriverManagerDataSource createDataSource() {
        DriverManagerDataSource source = new DriverManagerDataSource();
        source.setDriverClassName("org.h2.Driver");
        source.setUrl("jdbc:h2:mem:spring;DB_CLOSE_DELAY=-1");
        source.setUsername("sa");
        source.setPassword("");

        return source;
    }

    private DatabasePopulator createDatabasePopulator() {
        ResourceDatabasePopulator databasePopulator = new ResourceDatabasePopulator();
        databasePopulator.setContinueOnError(true);
        databasePopulator.addScript(new ClassPathResource("schema.sql"));
        return databasePopulator;
    }

    @Bean(name = "transactionManager")
    public DataSourceTransactionManager getTransactionManager() {
        DataSourceTransactionManager manager = new DataSourceTransactionManager();
        manager.setDataSource(getDataSource());
        return manager;
    }

    @Bean
    public JdbcTemplate getJdbcTemplate() {
        return new JdbcTemplate(getDataSource());
    }
}
