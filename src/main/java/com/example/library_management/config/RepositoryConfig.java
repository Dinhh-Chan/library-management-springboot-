package com.example.library_management.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.elasticsearch.repository.config.EnableElasticsearchRepositories;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@Configuration
@EnableJpaRepositories(basePackages = "com.example.library_management.repository.jpa")
@EnableElasticsearchRepositories(basePackages = "com.example.library_management.repository.elasticsearch")
public class RepositoryConfig {
}
