package com.icekredit.rpc.grpc.server.properties;

import org.apache.commons.collections4.MapUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.boot.autoconfigure.condition.ConditionOutcome;
import org.springframework.boot.autoconfigure.condition.SpringBootCondition;
import org.springframework.boot.bind.RelaxedPropertyResolver;
import org.springframework.context.annotation.ConditionContext;
import org.springframework.core.type.AnnotatedTypeMetadata;

import java.util.Map;

/**
 * Grpc sever相关配置属性启用条件
 *
 * @author wenchao
 */
public class GrpcServerPropertiesCondition extends SpringBootCondition {
    private static final String GRPC_SERVICE_PROPERTIES_PREFIX = "spring.grpc.server.";
    private static final String GRPC_SERVICE_PROPERTY_SERVICE_ID = "service-id";

    @Override
    public ConditionOutcome getMatchOutcome(ConditionContext conditionContext, AnnotatedTypeMetadata annotatedTypeMetadata) {
        RelaxedPropertyResolver resolver = new RelaxedPropertyResolver(conditionContext.getEnvironment());
        Map<String, Object> properties = resolver.getSubProperties(GRPC_SERVICE_PROPERTIES_PREFIX);

        return new ConditionOutcome(MapUtils.isNotEmpty(properties)
                && properties.containsKey(GRPC_SERVICE_PROPERTY_SERVICE_ID)
                && StringUtils.isNotBlank(MapUtils.getString(properties, GRPC_SERVICE_PROPERTY_SERVICE_ID)),
                "Grpc properties founded！service id： " + MapUtils.getString(properties, GRPC_SERVICE_PROPERTY_SERVICE_ID));
    }
}
