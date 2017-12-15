package com.icekredit.rpc.grpc.client.consul;

import com.google.common.collect.Maps;

import java.util.Map;
import java.util.Set;

public abstract class GrpcServerNodeList<T extends GrpcServerNode> {
    protected Map<String,Set<T>> serverNodeMap;

    public GrpcServerNodeList() {
        this.serverNodeMap = Maps.newConcurrentMap();
    }

    public Map<String, Set<T>> getServerNodeMap() {
        return serverNodeMap;
    }

    public void setServerNodeMap(Map<String, Set<T>> serverNodeMap) {
        this.serverNodeMap = serverNodeMap;
    }

    public abstract Set<T> getGrpcServerNodes(String serviceName);
    public abstract Set<T> refreshGrpcServerNodes(String serviceName);
}
