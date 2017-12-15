package com.icekredit.rpc.grpc.server.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties("spring.grpc.server")
public class GrpcServerProperties {
    private String serviceId;

    private int port;

    private HsHaProperties hsHa;

    public String getServiceId() {
        return serviceId;
    }

    public void setServiceId(String serviceId) {
        this.serviceId = serviceId;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public HsHaProperties getHsHa() {
        return hsHa;
    }

    public void setHsHa(HsHaProperties hsHa) {
        this.hsHa = hsHa;
    }
}
