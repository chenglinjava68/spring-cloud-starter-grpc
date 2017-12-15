package com.icekredit.rpc.grpc.client;

import com.icekredit.rpc.grpc.client.context.GrpcClientContext;
import com.icekredit.rpc.grpc.client.context.GrpcClientContextFactory;
import com.icekredit.rpc.grpc.client.properties.ConsulProperties;
import com.icekredit.rpc.grpc.client.properties.ConsulPropertiesCondition;
import com.icekredit.rpc.grpc.client.properties.GrpcClientProperties;
import com.icekredit.rpc.grpc.client.properties.GrpcClientPropertiesCondition;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Conditional;
import org.springframework.context.annotation.Configuration;

@Configuration
@Conditional(value = {GrpcClientPropertiesCondition.class, ConsulPropertiesCondition.class})
@EnableConfigurationProperties({GrpcClientProperties.class, ConsulProperties.class})
public class GrpcClientAutoConfiguration {
    @Bean
    public GrpcClientContext grpcClientContext(GrpcClientProperties grpcClientProperties, ConsulProperties consulProperties) {
        return GrpcClientContextFactory.context()
                .withGrpcClientProperties(grpcClientProperties)
                .withConsulProperties(consulProperties);
    }
}
