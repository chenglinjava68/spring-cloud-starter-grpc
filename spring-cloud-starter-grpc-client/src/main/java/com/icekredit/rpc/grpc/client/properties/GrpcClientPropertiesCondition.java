package com.icekredit.rpc.grpc.client.properties;

import org.apache.commons.collections4.MapUtils;
import org.springframework.boot.autoconfigure.condition.ConditionOutcome;
import org.springframework.boot.autoconfigure.condition.SpringBootCondition;
import org.springframework.boot.bind.RelaxedPropertyResolver;
import org.springframework.context.annotation.ConditionContext;
import org.springframework.core.type.AnnotatedTypeMetadata;

import java.util.Map;

public class GrpcClientPropertiesCondition extends SpringBootCondition {
    private static final String GRPC_CLIENT_PROPERTIES_PREFIX = "spring.cloud.grpc.client.";

    @Override
    public ConditionOutcome getMatchOutcome(ConditionContext conditionContext, AnnotatedTypeMetadata annotatedTypeMetadata) {
        RelaxedPropertyResolver resolver = new RelaxedPropertyResolver(conditionContext.getEnvironment());
        Map<String,Object> grpcClientConfiguration = resolver.getSubProperties(GRPC_CLIENT_PROPERTIES_PREFIX);

        return new ConditionOutcome(
                MapUtils.isNotEmpty(grpcClientConfiguration),
                "Grpc client configuration is enabled!"
        );
    }
}
