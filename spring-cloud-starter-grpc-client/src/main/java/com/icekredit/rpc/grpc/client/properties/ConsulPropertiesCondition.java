package com.icekredit.rpc.grpc.client.properties;

import org.apache.commons.collections4.MapUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.boot.autoconfigure.condition.ConditionOutcome;
import org.springframework.boot.autoconfigure.condition.SpringBootCondition;
import org.springframework.boot.bind.RelaxedPropertyResolver;
import org.springframework.context.annotation.ConditionContext;
import org.springframework.core.type.AnnotatedTypeMetadata;

import java.util.Map;

public class ConsulPropertiesCondition extends SpringBootCondition {
    private static final String SPRING_CLOUD_CONSUL_PREFIX = "spring.cloud.consul.";
    private static final String CONSUL_SERVER_HOST = "host";
    private static final String CONSUL_SERVER_PORT = "port";
    private static final String CONSUL_SERVER_ADDRESS_TEMPLATE = "%s:%d";

    @Override
    public ConditionOutcome getMatchOutcome(ConditionContext conditionContext, AnnotatedTypeMetadata annotatedTypeMetadata) {
        RelaxedPropertyResolver resolver = new RelaxedPropertyResolver(conditionContext.getEnvironment());
        Map<String,Object> consulProperties = resolver.getSubProperties(SPRING_CLOUD_CONSUL_PREFIX);

        return new ConditionOutcome(
                MapUtils.isNotEmpty(consulProperties) &&
                        consulProperties.containsKey(CONSUL_SERVER_HOST) &&
                        !StringUtils.isBlank(MapUtils.getString(consulProperties,CONSUL_SERVER_HOST)) &&
                        consulProperties.containsKey(CONSUL_SERVER_PORT) &&
                        isPortNumberValid(MapUtils.getInteger(consulProperties,CONSUL_SERVER_PORT)),
                "configured consul server at " + String.format(
                        CONSUL_SERVER_ADDRESS_TEMPLATE,
                        MapUtils.getString(consulProperties,CONSUL_SERVER_HOST),
                        MapUtils.getInteger(consulProperties,CONSUL_SERVER_PORT)
                )
        );
    }

    private boolean isPortNumberValid(Integer port) {
        if (port == null){
            return false;
        }

        if (port < 0){
            return false;
        }

        if (port > 65535){
            return false;
        }

        return true;
    }
}
