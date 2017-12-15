package com.icekredit.rpc.grpc.client.factory;

public interface ServerNodeUpdater {
    void start(UpdateAction updateAction);

    void stop();
}
