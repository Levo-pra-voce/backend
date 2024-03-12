package com.levopravoce.backend.config;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.autoconfigure.orm.jpa.JpaProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.orm.jpa.EntityManagerFactoryBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ResourceLoader;
import org.springframework.dao.annotation.PersistenceExceptionTranslationPostProcessor;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.persistenceunit.PersistenceManagedTypes;
import org.springframework.orm.jpa.persistenceunit.PersistenceManagedTypesScanner;
import org.springframework.orm.jpa.persistenceunit.PersistenceUnitManager;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.sql.DataSource;

@Configuration
@EnableJpaRepositories(basePackages = "com.levopravoce.backend.repository")
@EnableTransactionManagement
@EntityScan(basePackages = "com.levopravoce.backend.*")
@ComponentScan(basePackages = "com.levopravoce.backend.*")
public class DatabaseConfiguration {
    @Bean
    @ConfigurationProperties("spring.datasource")
    HikariConfig dbProps() {
        return new HikariConfig();
    }

    @Bean
    public DataSource dataSource(HikariConfig hikariConfig) {
        return new HikariDataSource(hikariConfig);
    }

    @Bean
    LocalContainerEntityManagerFactoryBean entityManagerFactory(
            DataSource dataSource,
            EntityManagerFactoryBuilder builder,
            PersistenceManagedTypes persistenceManagedTypes
    ) {
        return builder.dataSource(dataSource)
                .packages("com.levopravoce.backend.entities.*")
                .managedTypes(persistenceManagedTypes)
                .build();
    }

    @Bean
    PlatformTransactionManager transactionManager(
            LocalContainerEntityManagerFactoryBean entityManagerFactory) {
        JpaTransactionManager transactionManager = new JpaTransactionManager();
        transactionManager.setEntityManagerFactory(entityManagerFactory.getObject());
        return transactionManager;
    }

    @Bean
    public PersistenceExceptionTranslationPostProcessor exceptionTranslation() {
        return new PersistenceExceptionTranslationPostProcessor();
    }

    @Bean
    public EntityManagerFactoryBuilder builder(ObjectProvider<PersistenceUnitManager> persistenceUnitManager,
                                               JpaProperties jpaProperties
    ) {
        return new EntityManagerFactoryBuilder(new HibernateJpaVendorAdapter(),
                jpaProperties.getProperties(),
                persistenceUnitManager.getIfAvailable()
        );
    }

    @Bean
    @ConfigurationProperties(prefix = "spring")
    JpaProperties jpaProperties() {
        return new JpaProperties();
    }

    @Bean
    PersistenceManagedTypes persistenceManagedTypes(
            ResourceLoader resourceLoader
    ) {
        return new PersistenceManagedTypesScanner(resourceLoader)
                .scan("com.levopravoce.backend.entities");
    }
}
