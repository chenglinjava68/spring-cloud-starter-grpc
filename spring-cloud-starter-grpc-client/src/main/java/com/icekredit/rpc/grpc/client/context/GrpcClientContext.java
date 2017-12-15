package com.icekredit.rpc.grpc.client.context;

import com.icekredit.rpc.grpc.client.properties.ConsulProperties;
import com.icekredit.rpc.grpc.client.properties.GrpcClientProperties;

public class GrpcClientContext {
    private ConsulProperties consulProperties;

    private GrpcClientProperties grpcClientProperties;

    public GrpcClientContext(){

    }

    public ConsulProperties getConsulProperties() {
        return consulProperties;
    }

    public GrpcClientContext withConsulProperties(ConsulProperties consulProperties) {
        this.consulProperties = consulProperties;

        return this;
    }

    public GrpcClientProperties getGrpcClientProperties() {
        return grpcClientProperties;
    }

    public GrpcClientContext withGrpcClientProperties(GrpcClientProperties grpcClientProperties) {
        this.grpcClientProperties = grpcClientProperties;

        return this;
    }
}
