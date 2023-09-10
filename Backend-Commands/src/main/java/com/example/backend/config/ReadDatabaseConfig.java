//package com.example.backend.config;
//
//import jakarta.persistence.EntityManagerFactory;
//import org.hibernate.jpa.boot.spi.EntityManagerFactoryBuilder;
//import org.springframework.beans.factory.annotation.Qualifier;
//import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
//import org.springframework.boot.context.properties.ConfigurationProperties;
//import org.springframework.boot.jdbc.DataSourceBuilder;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
//import org.springframework.jdbc.core.JdbcTemplate;
//import org.springframework.orm.jpa.JpaTransactionManager;
//import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
//import org.springframework.transaction.PlatformTransactionManager;
//import org.springframework.transaction.annotation.EnableTransactionManagement;
//
//import javax.sql.DataSource;
//import java.util.Objects;
//
//@Configuration
//@EnableTransactionManagement
//@EnableJpaRepositories(
//        basePackages = "com.example.backend.repository.read",
//        entityManagerFactoryRef = "readEntityManagerFactory",
//        transactionManagerRef = "readTransactionManager"
//)
//public class ReadDatabaseConfig {
//    @Bean
//    @ConfigurationProperties("spring.datasource.read")
//    public DataSourceProperties readDataSourceProperties() {
//        return new DataSourceProperties();
//    }
//
//    @Bean(name = "readDataSource")
//    public DataSource dataSource() {
//        return readDataSourceProperties().initializeDataSourceBuilder().build();
//    }
//
//    @Bean(name = "readEntityManagerFactory")
//    public LocalContainerEntityManagerFactoryBean readEntityManagerFactory(
//            @Qualifier("readDataSource") DataSource dataSource,
//            EntityManagerFactoryBuilder builder) {
//                return builder
//                    .dataSource(dataSource)
//                    .packages("com.example.backend.read")
//                    .build();
//    }
//
//    @Bean(name = "readTransactionManager")
//    public PlatformTransactionManager readTransactionManager(
//            @Qualifier("readEntityManagerFactory") LocalContainerEntityManagerFactoryBean readEntityManagerFactory
//    ) {
//        return new JpaTransactionManager(Objects.requireNonNull(readEntityManagerFactory.getObject()));
//    }
//}