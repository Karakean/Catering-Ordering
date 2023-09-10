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
//import org.springframework.context.annotation.Primary;
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
//        basePackages = "com.example.backend.repository.write",
//        entityManagerFactoryRef = "writeEntityManagerFactory",
//        transactionManagerRef = "writeTransactionManager"
//)
//@Primary
//public class WriteDatabaseConfig {
//    @Bean
//    @ConfigurationProperties("spring.datasource.write")
//    @Primary
//    public DataSourceProperties writeDataSourceProperties() {
//        return new DataSourceProperties();
//    }
//
//    @Bean(name = "writeDataSource")
//    @Primary
//    public DataSource dataSource() {
//        return writeDataSourceProperties().initializeDataSourceBuilder().build();
//    }
//
//    @Bean(name = "writeEntityManagerFactory")
//    @Primary
//    public LocalContainerEntityManagerFactoryBean writeEntityManagerFactory(
//            @Qualifier("writeDataSource") DataSource dataSource,
//            EntityManagerFactoryBuilder builder) {
//        return builder
//                .dataSource(dataSource)
//                .packages("com.example.backend.write")
//                .build();
//    }
//
//    @Bean(name = "writeTransactionManager")
//    @Primary
//    public PlatformTransactionManager writeTransactionManager(
//            @Qualifier("writeEntityManagerFactory") LocalContainerEntityManagerFactoryBean writeEntityManagerFactory
//    ) {
//        return new JpaTransactionManager(Objects.requireNonNull(writeEntityManagerFactory.getObject()));
//    }
//}
