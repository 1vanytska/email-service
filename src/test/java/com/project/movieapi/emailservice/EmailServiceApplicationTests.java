package com.project.movieapi.emailservice;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.testcontainers.containers.RabbitMQContainer;
import org.testcontainers.elasticsearch.ElasticsearchContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.utility.DockerImageName;

@SpringBootTest
@Testcontainers
class EmailServiceApplicationTests {

    @Container
    @ServiceConnection
    static ElasticsearchContainer elasticsearch = new ElasticsearchContainer(
            DockerImageName.parse("elasticsearch:7.17.27")
                    .asCompatibleSubstituteFor("docker.elastic.co/elasticsearch/elasticsearch")
    )
            .withEnv("discovery.type", "single-node")
            .withEnv("xpack.security.enabled", "false");

    @Container
    @ServiceConnection
    static RabbitMQContainer rabbitmq = new RabbitMQContainer("rabbitmq:3-management");

    @MockitoBean
    private JavaMailSender javaMailSender;

//    @Test
//    void contextLoads() {
//        System.out.println("=== DOCKER DIAGNOSTICS ===");
//        System.out.println("DOCKER_HOST env var: " + System.getenv("DOCKER_HOST"));
//        System.out.println("Docker Host config: " + System.getProperty("docker.host"));
//        System.out.println("==========================");
//    }
}