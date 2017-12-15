package com.icekredit.rpc.grpc.client.wrapper;

import com.icekredit.rpc.grpc.client.consul.GrpcServerNode;

/**
 * Created by icekredit on 17-12-14.
 */
public class ServerNodeWrapper {
    private GrpcServerNode grpcServerNode;
    private ChannelWrapper channelWrapper;

    public ServerNodeWrapper(GrpcServerNode grpcServerNode, ChannelWrapper channelWrapper) {
        this.grpcServerNode = grpcServerNode;
        this.channelWrapper = channelWrapper;
    }

    public GrpcServerNode getGrpcServerNode() {
        return grpcServerNode;
    }

    public void setGrpcServerNode(GrpcServerNode grpcServerNode) {
        this.grpcServerNode = grpcServerNode;
    }

    public ChannelWrapper getChannelWrapper() {
        return channelWrapper;
    }

    public void setChannelWrapper(ChannelWrapper channelWrapper) {
        this.channelWrapper = channelWrapper;
    }
}
