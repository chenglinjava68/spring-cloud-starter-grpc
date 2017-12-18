package com.icekredit.rpc.grpc.client;

import com.icekredit.rpc.grpc.client.annotation.EnableGrpcClients;
import com.icekredit.rpc.grpc.client.scanner.GrpcClientBeanScanner;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.boot.bind.RelaxedPropertyResolver;
import org.springframework.context.EnvironmentAware;
import org.springframework.context.ResourceLoaderAware;
import org.springframework.context.annotation.AnnotationBeanNameGenerator;
import org.springframework.context.annotation.ImportBeanDefinitionRegistrar;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.core.env.Environment;
import org.springframework.core.io.ResourceLoader;
import org.springframework.core.type.AnnotationMetadata;
import org.springframework.core.type.StandardAnnotationMetadata;
import org.springframework.util.ClassUtils;

import java.util.*;
import java.util.stream.Collectors;

public class GrpcClientRegistrar implements ImportBeanDefinitionRegistrar,ResourceLoaderAware, EnvironmentAware {
    private ResourceLoader resourceLoader;

    private Environment environment;

    private static final String SPRING_GRPC_CLIENT_PACKAGE_TO_SCAN = "spring.cloud.grpc.client.package-to-scan";

    private Logger logger = LoggerFactory.getLogger(GrpcClientRegistrar.class);

    @Override
    public void setEnvironment(Environment environment) {
        this.environment = environment;
    }

    @Override
    public void setResourceLoader(ResourceLoader resourceLoader) {
        this.resourceLoader = resourceLoader;
    }

    @Override
    public void registerBeanDefinitions(AnnotationMetadata metadata, BeanDefinitionRegistry beanDefinitionRegistry) {
        if (!(metadata instanceof StandardAnnotationMetadata)) {
            return;
        }

        GrpcClientBeanScanner scanner = new GrpcClientBeanScanner(beanDefinitionRegistry);
        scanner.setResourceLoader(resourceLoader);
        scanner.setBeanNameGenerator(new AnnotationBeanNameGenerator());
        scanner.setScopedProxyMode(ScopedProxyMode.INTERFACES);

        String[] configuredBasePackages = getConfiguredBasePackages();
        String[] annotatedBasePackages = getAnnotatedBasePackages((StandardAnnotationMetadata) metadata);
        String rootPackage = getRootPackage((StandardAnnotationMetadata) metadata);

        //如果配置文件中指定了basePackages，那么直接扫描配置文件中指定的basePackages
        //否则扫描注解中指定的basePackages，
        //如果注解中仍然没有指定basePackages，那么扫描主类所在根包package
        if (ArrayUtils.isEmpty(configuredBasePackages)
                && ArrayUtils.isEmpty(annotatedBasePackages)){
            logger.info("No base packages specified for grpc client!We will scan the root package:{}",rootPackage);

            scanner.scan(rootPackage);
        } else if (ArrayUtils.isNotEmpty(configuredBasePackages)){
            logger.info("Configured base packages founded:{}",Arrays.toString(configuredBasePackages));

            scanner.scan(configuredBasePackages);
        } else {
            logger.info("Annotated base packages founded:{}",Arrays.toString(annotatedBasePackages));

            scanner.scan(annotatedBasePackages);
        }
    }

    /**
     * 获取由配置文件指定的basePackages，由spring.cloud.grpc.client.package-to-scan指定，并使用`,`进行分隔
     *
     * @return 配置文件中指定的basePackages
     */
    private String[] getConfiguredBasePackages() {
        RelaxedPropertyResolver resolver = new RelaxedPropertyResolver(environment);
        String basePackagesStr = resolver.getProperty(SPRING_GRPC_CLIENT_PACKAGE_TO_SCAN);
        if (StringUtils.isBlank(basePackagesStr)) {
            return new String[0];
        }

        String[] basePackagesParts = basePackagesStr.split(",");

        return Arrays.stream(basePackagesParts)
                .filter(StringUtils::isNotBlank)
                .map(String::trim)
                .distinct()
                .collect(Collectors.toSet()).toArray(new String[0]);
    }

    /**
     * 根据主类上面的EnableGrpcClients注解获取需要扫描的包名
     *
     * @param standardAnnotationMetadata 主类上面的注解信息元数据
     * @return 注解指定的basePackages
     */
    private String[] getAnnotatedBasePackages(StandardAnnotationMetadata standardAnnotationMetadata) {
        String[] basePackages = null;

        if (standardAnnotationMetadata.hasAnnotation(EnableGrpcClients.class.getName())) {
            Map<String, Object> attrs = standardAnnotationMetadata
                    .getAnnotationAttributes(EnableGrpcClients.class.getName());

            basePackages = (String[]) attrs.get("values");
            basePackages = basePackages.length == 0 ? (String[]) attrs.get("basePackages") : basePackages;
        }

        return basePackages;
    }

    private String getRootPackage(StandardAnnotationMetadata standardAnnotationMetadata){
        return ClassUtils.getPackageName(standardAnnotationMetadata.getIntrospectedClass());
    }
}
