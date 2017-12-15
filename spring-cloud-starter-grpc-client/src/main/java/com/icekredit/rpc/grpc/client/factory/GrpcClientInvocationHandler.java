package com.icekredit.rpc.grpc.client.factory;

import com.icekredit.rpc.grpc.client.scanner.GrpcClientStubDefinition;
import com.icekredit.rpc.grpc.client.wrapper.ServerNodeWrapper;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.util.List;

public class GrpcClientInvocationHandler implements InvocationHandler {
    private ServerNodeUpdateAction serverNodeUpdateAction;

    public GrpcClientInvocationHandler(String serviceName, List<GrpcClientStubDefinition> stubDefinitions) {
        serverNodeUpdateAction = new ServerNodeUpdateAction(serviceName,stubDefinitions);
        new ServerNodeUpdaterImpl().start(serverNodeUpdateAction);
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        List<ServerNodeWrapper> serverNodeWrappers = serverNodeUpdateAction.getServerNodeWrappers();

        int size = serverNodeWrappers.size();

        ServerNodeWrapper serverNodeWrapper = serverNodeWrappers.get((int) (Math.random() * size));

        return serverNodeWrapper.getChannelWrapper().getStubWrapper().getCorrespondingStub(method.getName());
    }
}
