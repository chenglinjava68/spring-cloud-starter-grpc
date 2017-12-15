package com.icekredit.rpc.grpc.server;

import com.icekredit.rpc.grpc.server.annotation.GrpcService;
import com.icekredit.rpc.grpc.server.context.GrpcServiceContext;
import com.icekredit.rpc.grpc.server.exception.GrpcServiceInstantiateException;
import com.icekredit.rpc.grpc.server.properties.GrpcServerProperties;
import com.icekredit.rpc.grpc.server.properties.GrpcServerPropertiesCondition;
import com.icekredit.rpc.grpc.server.wrapper.GrpcServiceWrapper;
import com.icekredit.rpc.grpc.server.wrapper.GrpcServiceWrapperFactory;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.aop.framework.Advised;
import org.springframework.beans.BeansException;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Conditional;
import org.springframework.context.annotation.Configuration;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * GRPC服务端自动配置
 *
 * @author wenchao
 */
@Configuration
@Conditional(GrpcServerPropertiesCondition.class)
@EnableConfigurationProperties(GrpcServerProperties.class)
public class GrpcServerAutoConfiguration implements ApplicationContextAware{
    private ApplicationContext applicationContext;

    private Logger logger = LoggerFactory.getLogger(GrpcServerAutoConfiguration.class);

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }

    @Bean
    @ConditionalOnMissingBean
    public GrpcServerGroup grpcServerGroup(GrpcServerProperties grpcServerProperties){
        Map<String,Object> grpcServiceBeansMap = applicationContext.getBeansWithAnnotation(GrpcService.class);

        List<GrpcServiceWrapper> grpcServiceWrappers = grpcServiceBeansMap.entrySet()
                .stream()
                .map(entry -> {
                    String beanName = entry.getKey();
                    Object bean = entry.getValue();

                    GrpcService grpcService = bean.getClass().getAnnotation(GrpcService.class);
                    String grpcServiceName = StringUtils.isBlank(grpcService.name()) ? beanName : grpcService.name();
                    String grpcServiceVersion = grpcService.version();

                    if (bean instanceof Advised) {
                        logger.info("GrpcService proxy Created by using cglib...");

                        try {
                            ((Advised) bean).getTargetSource().getTarget();
                        } catch (Exception e) {
                            logger.error("fail to get target from bean:{}", bean);

                            throw new GrpcServiceInstantiateException("fail to instantiate grpc service for " + grpcServiceName);
                        }
                    }

                    return GrpcServiceWrapperFactory.wrap(
                            grpcServerProperties.getServiceId(), grpcServiceName, bean, grpcServiceVersion
                    );
                }).collect(Collectors.toList());

        GrpcServiceContext grpcServiceContext = new GrpcServiceContext(grpcServerProperties,grpcServiceWrappers);
        return new GrpcServerGroup(grpcServiceContext.buildServer());
    }

    @Bean
    @ConditionalOnMissingBean
    public GrpcServerBootstrap grpcServerBootstrap(GrpcServerGroup grpcServerGroup){
        return new GrpcServerBootstrap(grpcServerGroup);
    }
}
