package com.levopravoce.backend.config;

import com.levopravoce.backend.user.mock.UserMock;
import java.util.List;
import javax.sql.DataSource;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;

@TestConfiguration
public class CustomTestConfiguration {

  @Primary
  @Bean
  public UserDetailsService userDetailsService() {
    return new InMemoryUserDetailsManager(List.of(UserMock.DELIVERY_USER, UserMock.CLIENT_USER));
  }

  @Value("${spring.datasource.url}")
  private String dbUrl;

  @Value("${spring.datasource.driver-class-name}")
  private String driverClassName;

  @Value("${spring.datasource.username}")
  private String username;

  @Value("${spring.datasource.password}")
  private String password;

  @Bean
  public DataSource inMemoryDataSource() {
    DriverManagerDataSource dataSource = new DriverManagerDataSource();
    dataSource.setDriverClassName(driverClassName);
    dataSource.setUrl(dbUrl);
    dataSource.setUsername(username);
    dataSource.setPassword(password);

    return dataSource;
  }
}
