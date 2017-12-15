package com.icekredit.rpc.grpc.server.context;

import com.icekredit.rpc.grpc.server.properties.GrpcServerProperties;
import com.icekredit.rpc.grpc.server.wrapper.GrpcServiceWrapper;
import io.grpc.Server;

import java.util.List;

public abstract class AbstractGrpcServiceContext {
    protected GrpcServerProperties properties;
    protected List<GrpcServiceWrapper> serviceWrappers;

    public AbstractGrpcServiceContext(GrpcServerProperties properties, List<GrpcServiceWrapper> serviceWrappers) {
        this.properties = properties;
        this.serviceWrappers = serviceWrappers;
    }

    public abstract Server buildServer();
}
