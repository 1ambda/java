package com.github.lambda;

import com.github.lambda.dao.UserDao;
import com.github.lambda.dao.UserDaoJdbc;
import com.github.lambda.service.NameMatchClassMethodPointcut;
import com.github.lambda.service.TestUserServiceImpl;
import com.github.lambda.service.TransactionAdvice;
import com.github.lambda.service.UserServiceImpl;
import org.springframework.aop.framework.autoproxy.DefaultAdvisorAutoProxyCreator;
import org.springframework.aop.support.DefaultPointcutAdvisor;
import org.springframework.aop.support.NameMatchMethodPointcut;
import org.springframework.beans.factory.annotation.Autowired;
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
import org.springframework.mail.MailSender;

@Configuration
@ComponentScan("com.github.lambda")
public class TestAppConfig {

    @Autowired DriverManagerDataSource dataSource;
    @Autowired UserDao userDao;
    @Autowired MailSender mailSender;
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
    public DefaultAdvisorAutoProxyCreator defaultAdvisorAutoProxyCreator(){
        DefaultAdvisorAutoProxyCreator creator = new DefaultAdvisorAutoProxyCreator();
        creator.setProxyTargetClass(true);

        return creator;
    }

    @Bean(name = "transactionPointcut")
    public NameMatchClassMethodPointcut getTransactionPointcut() {
        NameMatchClassMethodPointcut pointcut = new NameMatchClassMethodPointcut();
        pointcut.setMappedClassName("*ServiceImpl");
        pointcut.setMappedName("upgrade*");

        return pointcut;
    }

    @Bean(name = "transactionAdvisor")
    public DefaultPointcutAdvisor getTransactionAdvisor() {
        DefaultPointcutAdvisor advisor = new DefaultPointcutAdvisor();
        advisor.setPointcut(transactionPointcut);
        advisor.setAdvice(transactionAdvice);

        return advisor;
    }

}
