package com.icekredit.rpc.grpc.client.scanner;

import com.icekredit.rpc.grpc.client.annotation.GrpcClient;
import com.icekredit.rpc.grpc.client.annotation.GrpcClientStub;
import com.icekredit.rpc.grpc.client.exception.GrpcClientDefinitionException;
import com.icekredit.rpc.grpc.client.factory.GrpcClientFactoryBean;
import io.grpc.stub.AbstractStub;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.AnnotatedBeanDefinition;
import org.springframework.beans.factory.config.BeanDefinitionHolder;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.GenericBeanDefinition;
import org.springframework.context.annotation.ClassPathBeanDefinitionScanner;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.core.type.AnnotationMetadata;
import org.springframework.core.type.filter.AnnotationTypeFilter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;

public class GrpcClientBeanScanner extends ClassPathBeanDefinitionScanner{
    private Logger logger = LoggerFactory.getLogger(getClass());

    public GrpcClientBeanScanner(BeanDefinitionRegistry registry) {
        super(registry);
    }

    @Override
    protected void registerDefaultFilters() {
        this.addIncludeFilter(new AnnotationTypeFilter(GrpcClient.class));
    }

    @Override
    protected Set<BeanDefinitionHolder> doScan(String... basePackages) {
        Set<BeanDefinitionHolder> definitionHolders = super.doScan(basePackages);

        logger.info("The packages to scan:{}", Arrays.toString(basePackages));

        for (BeanDefinitionHolder definitionHolder:definitionHolders){
            GenericBeanDefinition genericBeanDefinition = (GenericBeanDefinition) definitionHolder.getBeanDefinition();

            logger.info("Registered Grpc Client founded with bean name {} and type {}",
                    definitionHolder.getBeanName(),genericBeanDefinition.getBeanClassName());

            Class beanClass;
            try {
                beanClass = Class.forName(genericBeanDefinition.getBeanClassName());
            } catch (ClassNotFoundException e){
                logger.error("Class not found for bean class:{}",genericBeanDefinition.getBeanClassName());

                continue;
            }

            GrpcClient grpcClient = AnnotationUtils.findAnnotation(
                    beanClass,GrpcClient.class
            );
            if (grpcClient == null){
                logger.error("Sorry!Grpc Client definition is not found!");

                continue;
            }

            String serviceName = grpcClient.name();
            serviceName = StringUtils.isBlank(serviceName) ? grpcClient.value() : serviceName;
            if (StringUtils.isBlank(serviceName)){
                String msg = String.format("Neither name nor value has been specified for GrpcClient:%s",genericBeanDefinition.getBeanClassName());

                logger.error(msg);

                throw new GrpcClientDefinitionException(msg);
            }

            GrpcClientStub[] grpcClientStubs = grpcClient.grpcClientStubs();
            if (ArrayUtils.isEmpty(grpcClientStubs)){
                String msg = String.format("No client stub has been specified for GrpcClient:%s",genericBeanDefinition.getBeanClassName());

                logger.error(msg);

                throw new GrpcClientDefinitionException(msg);
            }

            List<GrpcClientStubDefinition> stubDefinitions = new ArrayList<>();
            for (GrpcClientStub grpcClientStub:grpcClientStubs){
                Class<? extends AbstractStub> clientStubClass = grpcClientStub.clientStubClass();
                if (clientStubClass.equals(AbstractStub.class)){
                    logger.error("No client stub class has been specified for GrpcClientStub in GrpcClient:{}",genericBeanDefinition.getBeanClassName());
                }

                String qualifier = grpcClientStub.name();
                qualifier = StringUtils.isBlank(qualifier) ? grpcClientStub.value() : qualifier;
                qualifier = StringUtils.isBlank(qualifier) ? StringUtils.uncapitalize(clientStubClass.getSimpleName()) : qualifier;

                stubDefinitions.add(new GrpcClientStubDefinition(qualifier,clientStubClass));
            }

            genericBeanDefinition.getPropertyValues().addPropertyValue("beanClass",beanClass);
            genericBeanDefinition.getPropertyValues().addPropertyValue("beanClassName",genericBeanDefinition.getBeanClassName());
            genericBeanDefinition.getPropertyValues().addPropertyValue("serviceName",serviceName);
            genericBeanDefinition.getPropertyValues().addPropertyValue("stubDefinitions",stubDefinitions);

            genericBeanDefinition.setBeanClass(GrpcClientFactoryBean.class);
        }

        return definitionHolders;
    }

    @Override
    protected boolean isCandidateComponent(AnnotatedBeanDefinition beanDefinition) {
        AnnotationMetadata metadata = beanDefinition.getMetadata();
        return metadata.hasAnnotation(GrpcClient.class.getName()) && metadata.isInterface();
    }
}
