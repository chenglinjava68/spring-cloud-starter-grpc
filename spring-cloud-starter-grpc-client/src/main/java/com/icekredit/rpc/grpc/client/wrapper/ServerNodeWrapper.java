package com.icekredit.rpc.grpc.client.wrapper;

import com.icekredit.rpc.grpc.client.consul.GrpcConsulServerNode;

public class ServerNodeWrapper {
    private GrpcConsulServerNode grpcServerNode;
    private ChannelWrapper channelWrapper;

    public ServerNodeWrapper(GrpcConsulServerNode grpcServerNode, ChannelWrapper channelWrapper) {
        this.grpcServerNode = grpcServerNode;
        this.channelWrapper = channelWrapper;
    }

    public GrpcConsulServerNode getGrpcServerNode() {
        return grpcServerNode;
    }

    public void setGrpcServerNode(GrpcConsulServerNode grpcServerNode) {
        this.grpcServerNode = grpcServerNode;
    }

    public ChannelWrapper getChannelWrapper() {
        return channelWrapper;
    }

    public void setChannelWrapper(ChannelWrapper channelWrapper) {
        this.channelWrapper = channelWrapper;
    }
}
