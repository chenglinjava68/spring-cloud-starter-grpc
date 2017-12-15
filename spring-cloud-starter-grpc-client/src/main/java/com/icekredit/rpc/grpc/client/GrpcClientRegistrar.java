package com.icekredit.rpc.grpc.client;

import com.icekredit.rpc.grpc.client.scanner.GrpcClientBeanScanner;
import org.apache.commons.collections4.MapUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.BeanClassLoaderAware;
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
import org.springframework.util.ClassUtils;

import java.util.*;

public class GrpcClientRegistrar implements ImportBeanDefinitionRegistrar,
        ResourceLoaderAware, BeanClassLoaderAware, EnvironmentAware {
    private ResourceLoader resourceLoader;

    private ClassLoader beanClassLoader;

    private Environment environment;

    private static final String SPRING_GRPC_CLIENT_PACKAGE_TO_SCAN = "spring.cloud.grpc.client.package-to-scan";

    private Logger logger = LoggerFactory.getLogger(GrpcClientRegistrar.class);

    @Override
    public void setBeanClassLoader(ClassLoader classLoader) {
        this.beanClassLoader = classLoader;
    }

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
        GrpcClientBeanScanner scanner = new GrpcClientBeanScanner(beanDefinitionRegistry);
        scanner.setResourceLoader(resourceLoader);
        scanner.setBeanNameGenerator(new AnnotationBeanNameGenerator());
        scanner.setScopedProxyMode(ScopedProxyMode.INTERFACES);

        RelaxedPropertyResolver resolver = new RelaxedPropertyResolver(environment);
        setScannedPackages(scanner, resolver.getProperty(SPRING_GRPC_CLIENT_PACKAGE_TO_SCAN));
    }

    private void setScannedPackages(GrpcClientBeanScanner beanScanner, String basePackages) {
        if (StringUtils.isBlank(basePackages)) {
            logger.error("the package to scan has not been specified!");
            return;
        }

        int delimiterIndex = StringUtils.indexOf(basePackages, ",");
        if (delimiterIndex > -1) {
            StringTokenizer tokenizer = new StringTokenizer(basePackages, ",");
            Set<String> packageToScanSet = new HashSet<>();
            while (tokenizer.hasMoreTokens()) {
                String subPackage = tokenizer.nextToken();
                packageToScanSet.add(subPackage);
                logger.info("Subpackage {} is to be scanned by {}", subPackage, beanScanner);
            }

            List<String> packageToScanList = new ArrayList<>(packageToScanSet);
            String[] packagesToScan = packageToScanList.toArray(new String[packageToScanList.size()]);
            beanScanner.scan(packagesToScan);
        } else {
            logger.info("Base package {} is to be scanned with {}", basePackages, beanScanner);
            beanScanner.scan(basePackages);
        }
    }

    private Set<String> getBasePackages(Map<String, Object> annotationAttributes, Class<? extends AnnotationMetadata> annotationMetadataClass) {
        Set<String> basePackages = new HashSet<>();

        if (MapUtils.isEmpty(annotationAttributes)) {
            return basePackages;
        }

        for (String basePackage : (String[]) annotationAttributes.get("values")) {
            if (StringUtils.isNotBlank(basePackage)) {
                basePackages.add(basePackage);
            }
        }

        for (String basePackage : (String[]) annotationAttributes.get("basePackages")) {
            if (StringUtils.isNotBlank(basePackage)) {
                basePackages.add(basePackage);
            }
        }

        if (basePackages.isEmpty()) {
            basePackages.add(ClassUtils.getPackageName(annotationMetadataClass));
        }

        return basePackages;
    }
}
