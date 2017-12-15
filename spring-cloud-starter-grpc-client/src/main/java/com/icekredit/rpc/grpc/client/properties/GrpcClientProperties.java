package com.icekredit.rpc.grpc.client.properties;


import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "spring.cloud.grpc.client")
public class GrpcClientProperties {
}
