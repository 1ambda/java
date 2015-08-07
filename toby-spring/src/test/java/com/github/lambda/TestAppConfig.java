package com.github.lambda;

import com.github.lambda.dao.UserDao;
import com.github.lambda.dao.UserDaoJdbc;
import com.github.lambda.service.TransactionAdvice;
import com.github.lambda.service.UserServiceImpl;
import com.github.lambda.util.DummyMailSender;
import org.springframework.aop.framework.ProxyFactoryBean;
import org.springframework.aop.support.DefaultPointcutAdvisor;
import org.springframework.aop.support.NameMatchMethodPointcut;
import org.springframework.beans.factory.annotation.Autowired;
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

    @Autowired DriverManagerDataSource dataSource;
    @Autowired UserDao userDao;
    @Autowired MailSender mailSender;
    @Autowired UserServiceImpl userServiceImpl;
    @Autowired NameMatchMethodPointcut transactionPointcut;
    @Autowired TransactionAdvice transactionAdvice;

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

    @Bean(name = "userService")
    public ProxyFactoryBean getUserService() throws Exception {
        ProxyFactoryBean pfBean = new ProxyFactoryBean();
        pfBean.setTarget(userServiceImpl);
        pfBean.setInterceptorNames("transactionAdvisor");

        return pfBean;
    }

    @Bean(name = "getUserService")
    public UserServiceImpl getUserServiceImpl() {
        UserServiceImpl service = new UserServiceImpl();
        service.setMailSender(mailSender);
        service.setUserDao(userDao);

        return service;
    }

    @Bean(name = "mailSender")
    public MailSender getMailSender() {
        return new DummyMailSender();
    }

    @Bean(name = "transactionManager")
    public DataSourceTransactionManager getTransactionManager() {
        DataSourceTransactionManager manager = new DataSourceTransactionManager();
        manager.setDataSource(dataSource);
        return manager;
    }

    @Bean(name = "jdbcTemplate")
    public JdbcTemplate getJdbcTemplate() {
        return new JdbcTemplate(dataSource);
    }

    @Bean
    public NameMatchMethodPointcut getTransactionPointcut() {
        NameMatchMethodPointcut pointcut = new NameMatchMethodPointcut();
        pointcut.setMappedName("upgrade*");
        return pointcut;
    }

    @Bean
    public TransactionAdvice getTransactionAdvice() {
        return new TransactionAdvice();
    }

    @Bean(name = "transactionAdvisor")
    public DefaultPointcutAdvisor getTransactionAdvisor() {
        DefaultPointcutAdvisor advisor = new DefaultPointcutAdvisor();
        advisor.setPointcut(transactionPointcut);
        advisor.setAdvice(transactionAdvice);

        return advisor;
    }
}
