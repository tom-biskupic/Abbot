package com.runcible.abbot.application;

import java.util.Properties;

import javax.persistence.EntityManagerFactory;
import javax.sql.DataSource;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.dao.annotation.PersistenceExceptionTranslationPostProcessor;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.JpaVendorAdapter;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

@Configuration
@EnableTransactionManagement
@EnableJpaRepositories(basePackages = {"com.runcible.abbot.repository"})
//@ConfigurationProperties(value="classpath:application.properties")
class PersistenceContext 
{
    @Bean
    public LocalContainerEntityManagerFactoryBean entityManagerFactory(Environment env) 
    {
        LocalContainerEntityManagerFactoryBean em = new LocalContainerEntityManagerFactoryBean();
        em.setDataSource(dataSource(env));
        em.setPackagesToScan(new String[] { "com.runcible.abbot" });
    
        JpaVendorAdapter vendorAdapter = new HibernateJpaVendorAdapter();
        em.setJpaVendorAdapter(vendorAdapter);
        em.setJpaProperties(additionalProperties(env));

        return em;
    }
    
    @Bean(destroyMethod = "close")
    public DataSource dataSource(Environment env)
    {
        HikariConfig hikariConfig = new HikariConfig();
        hikariConfig.setDriverClassName(env.getRequiredProperty("db.driver"));
        hikariConfig.setJdbcUrl(env.getRequiredProperty("db.url")); 
        hikariConfig.setUsername(env.getRequiredProperty("db.username"));
        hikariConfig.setPassword(env.getRequiredProperty("db.password"));

        hikariConfig.setMaximumPoolSize(5);
        hikariConfig.setConnectionTestQuery("SELECT 1");
        hikariConfig.setPoolName("springHikariCP");
        hikariConfig.setMaximumPoolSize(5);
        
        hikariConfig.addDataSourceProperty("dataSource.cachePrepStmts", "true");
        hikariConfig.addDataSourceProperty("dataSource.prepStmtCacheSize", "250");
        hikariConfig.addDataSourceProperty("dataSource.prepStmtCacheSqlLimit", "2048");
        hikariConfig.addDataSourceProperty("dataSource.useServerPrepStmts", "true");

        HikariDataSource dataSource = new HikariDataSource(hikariConfig);

        return dataSource;
    }

    @Bean
    public PlatformTransactionManager transactionManager(EntityManagerFactory emf)
    {
       JpaTransactionManager transactionManager = new JpaTransactionManager();
       transactionManager.setEntityManagerFactory(emf);
  
       return transactionManager;
    }
  
    @Bean
    public PersistenceExceptionTranslationPostProcessor exceptionTranslation()
    {
       return new PersistenceExceptionTranslationPostProcessor();
    }
  
    Properties additionalProperties(Environment env) 
    {
       Properties properties = new Properties();
       properties.setProperty("hibernate.hbm2ddl.auto", env.getRequiredProperty("hibernate.hbm2ddl.auto"));
       properties.setProperty("hibernate.dialect", env.getRequiredProperty("hibernate.dialect"));
       properties.setProperty("hibernate.show_sql", env.getRequiredProperty("hibernate.show_sql"));
       
       return properties;
    }
}