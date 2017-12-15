package com.icekredit.rpc.grpc.client.factory;

import com.icekredit.rpc.grpc.client.exception.GrpcClientDefinitionException;
import com.icekredit.rpc.grpc.client.scanner.GrpcClientStubDefinition;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.beans.factory.InitializingBean;

import java.lang.reflect.Proxy;
import java.util.List;

public class GrpcClientFactoryBean implements FactoryBean, InitializingBean {
    private Class beanClass;
    private String beanClassName;
    private String serviceName;
    private List<GrpcClientStubDefinition> stubDefinitions;

    private Logger logger = LoggerFactory.getLogger(getClass());

    @Override
    public Object getObject() throws Exception {
        if (!beanClass.isInterface()){
            throw new GrpcClientDefinitionException("@GrpcClient can only been specified on an interface!");
        }

        GrpcClientInvocationHandler invocationHandler = new GrpcClientInvocationHandler(
                serviceName,stubDefinitions
        );
        return Proxy.newProxyInstance(getClass().getClassLoader(),new Class[]{beanClass},invocationHandler);
    }

    @Override
    public Class<?> getObjectType() {
        if (beanClass != null){
            return beanClass;
        }

        if(StringUtils.isBlank(beanClassName)){
            return null;
        }

        try {
            beanClass = Class.forName(beanClassName);
        } catch (ClassNotFoundException e){
            logger.error("No class found for {}",beanClassName);
        }

        return null;
    }

    @Override
    public boolean isSingleton() {
        return true;
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        logger.info("GrpcClientFactoryBean.afterPropertiesSet：all properties injected successfully!");
    }

    public Class getBeanClass() {
        return beanClass;
    }

    public void setBeanClass(Class beanClass) {
        this.beanClass = beanClass;
    }

    public String getBeanClassName() {
        return beanClassName;
    }

    public void setBeanClassName(String beanClassName) {
        this.beanClassName = beanClassName;
    }

    public String getServiceName() {
        return serviceName;
    }

    public void setServiceName(String serviceName) {
        this.serviceName = serviceName;
    }

    public List<GrpcClientStubDefinition> getStubDefinitions() {
        return stubDefinitions;
    }

    public void setStubDefinitions(List<GrpcClientStubDefinition> stubDefinitions) {
        this.stubDefinitions = stubDefinitions;
    }
}
