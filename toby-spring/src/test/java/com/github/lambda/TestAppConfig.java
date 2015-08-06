package com.github.lambda;

import com.github.lambda.dao.UserDao;
import com.github.lambda.dao.UserDaoJdbc;
import com.github.lambda.service.TxFactoryBean;
import com.github.lambda.service.UserService;
import com.github.lambda.service.UserServiceImpl;
import com.github.lambda.util.DummyMailSender;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.jdbc.datasource.init.DatabasePopulator;
import org.springframework.jdbc.datasource.init.DatabasePopulatorUtils;
import org.springframework.jdbc.datasource.init.ResourceDatabasePopulator;
import org.springframework.mail.MailSender;

@Configuration
public class TestAppConfig {
    @Bean(name = "dataSource")
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

    @Bean(name = "userDao")
    public UserDao getUserDao() { return new UserDaoJdbc(); }

    @Bean(name = "useService")
    public UserService getUserService() throws Exception {
        TxFactoryBean factory = new TxFactoryBean();
        factory.setTarget(getUserServiceImpl());
        factory.setTxManager(getTransactionManager());
        factory.setServiceInterface(UserService.class);

        return (UserService) factory.getObject();
    };

    @Bean(name = "getUserService")
    public UserService getUserServiceImpl() {
        UserServiceImpl service = new UserServiceImpl();
        service.setMailSender(getMailSender());
        service.setUserDao(getUserDao());

        return service;
    }

    @Bean(name = "mailSender")
    public MailSender getMailSender() {
        return new DummyMailSender();
    }

    @Bean(name = "transactionManager")
    public DataSourceTransactionManager getTransactionManager() {
        DataSourceTransactionManager manager = new DataSourceTransactionManager();
        manager.setDataSource(getDataSource());

        return manager;
    }

    @Bean(name = "jdbcTemplate")
    public JdbcTemplate getJdbcTemplate() {
        DriverManagerDataSource source = getDataSource();
        return new JdbcTemplate(source);
    }
}
