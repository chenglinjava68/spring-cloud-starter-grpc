package com.icekredit.rpc.grpc.client.wrapper;

import io.grpc.Channel;

public class ChannelWrapper {
    private Channel channel;
    private StubWrapper stubWrapper;

    public Channel getChannel() {
        return channel;
    }

    public void setChannel(Channel channel) {
        this.channel = channel;
    }

    public StubWrapper getStubWrapper() {
        return stubWrapper;
    }

    public void setStubWrapper(StubWrapper stubWrapper) {
        this.stubWrapper = stubWrapper;
    }
}
