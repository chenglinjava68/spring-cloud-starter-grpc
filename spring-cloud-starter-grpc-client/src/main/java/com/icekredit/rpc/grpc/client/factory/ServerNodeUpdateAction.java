package com.icekredit.rpc.grpc.client.factory;

import com.icekredit.rpc.grpc.client.consul.GrpcConsulServerNode;
import com.icekredit.rpc.grpc.client.consul.GrpcConsulServerNodeList;
import com.icekredit.rpc.grpc.client.context.GrpcClientContextFactory;
import com.icekredit.rpc.grpc.client.scanner.GrpcClientStubDefinition;
import com.icekredit.rpc.grpc.client.wrapper.ChannelWrapper;
import com.icekredit.rpc.grpc.client.wrapper.ServerNodeWrapper;
import com.icekredit.rpc.grpc.client.wrapper.StubWrapper;
import com.orbitz.consul.Consul;
import io.grpc.Channel;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import io.grpc.stub.AbstractStub;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;

import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.Vector;

public class ServerNodeUpdateAction implements UpdateAction {
    private volatile GrpcConsulServerNodeList serverNodeList;

    private String serviceName;
    private List<GrpcClientStubDefinition> stubDefinitions;

    private List<ServerNodeWrapper> serverNodeWrappers;

    private Logger logger = LoggerFactory.getLogger(ServerNodeUpdateAction.class);

    ServerNodeUpdateAction(String serviceName, List<GrpcClientStubDefinition> stubDefinitions) {
        this.serviceName = serviceName;
        this.stubDefinitions = stubDefinitions;

        serverNodeWrappers = new Vector<>();
    }

    @Override
    public void update() {
        if (serverNodeList == null) {
            synchronized (ServerNodeUpdateAction.class) {
                if (serverNodeList == null) {
                    String consulHost = GrpcClientContextFactory.context().getConsulProperties().getHost();
                    int consulPort = GrpcClientContextFactory.context().getConsulProperties().getPort();

                    //if application context has not start up,omit current update action
                    if (StringUtils.isBlank(consulHost)){
                        return;
                    }

                    logger.info("Initialize the consul agent with host:{} and port:{}",consulHost,consulPort);

                    Consul consul;
                    try {
                        consul = Consul.builder().withUrl(String.format("http://%s:%s",consulHost,consulPort)).build();
                    } catch (Exception e) {
                        logger.error("Building consul agent has encountered a problem!",e);

                        return;
                    }

                    if (consul == null){
                        logger.error("Can not get consul agent!");

                        return;
                    }

                    //拿到全部的ServerNode
                    serverNodeList = GrpcConsulServerNodeList.getSingleInstance(consul);
                }
            }
        }

        logger.info("Execute server node list update...");
        Set<GrpcConsulServerNode> grpcServerNodes = serverNodeList.getGrpcServerNodes(serviceName);

        //如果是join节点
        grpcServerNodes.stream()
                .filter(serverNode -> {
                    for (ServerNodeWrapper serverNodeWrapper : serverNodeWrappers) {
                        if (serverNodeWrapper.getGrpcServerNode().equals(serverNode)) {
                            return false;
                        }
                    }

                    return true;
                })
                .forEach(serverNode -> {
                    logger.info("New server node join in with host:{} and port:{}", serverNode.getHost(), serverNode.getPort());

                    ManagedChannel channel = ManagedChannelBuilder.forAddress(
                            serverNode.getHost(),
                            serverNode.getPort()
                    ).usePlaintext(true).build();

                    StubWrapper stubWrapper = new StubWrapper();
                    AbstractStub abstractStub;
                    for (GrpcClientStubDefinition stubDefinition : stubDefinitions) {
                        Constructor<? extends AbstractStub> stubConstructor;
                        try {
                            stubConstructor = stubDefinition.getClientStubClass().getDeclaredConstructor(Channel.class);
                            stubConstructor.setAccessible(true);
                        } catch (NoSuchMethodException e) {
                            logger.error("Can not get constructor for stub class:{}", stubDefinition.getClientStubClass().getName());

                            continue;
                        }

                        abstractStub = BeanUtils.instantiateClass(stubConstructor, channel);

                        String stubClassName = stubDefinition.getClientStubClass().getSimpleName();
                        if (stubClassName.endsWith("BlockingStub")) {
                            stubWrapper.setBlockingStub(abstractStub);
                        } else if (stubClassName.endsWith("FutureStub")) {
                            stubWrapper.setFutureStub(abstractStub);
                        } else if (stubClassName.endsWith("Stub")) {
                            stubWrapper.setStub(abstractStub);
                        }
                    }

                    ChannelWrapper channelWrapper = new ChannelWrapper();
                    channelWrapper.setChannel(channel);
                    channelWrapper.setStubWrapper(stubWrapper);

                    ServerNodeWrapper serverNodeWrapper = new ServerNodeWrapper(serverNode, channelWrapper);
                    serverNodeWrappers.add(serverNodeWrapper);
                });


        //如果是leave节点
        List<ServerNodeWrapper> leaveServerNodeWrappers = new ArrayList<>();
        serverNodeWrappers.stream()
                .filter(serverNodeWrapper -> !grpcServerNodes.contains(serverNodeWrapper.getGrpcServerNode()))
                .forEach(serverNodeWrapper -> {
                    logger.info("Old server node left with host:{} and port:{}",
                            serverNodeWrapper.getGrpcServerNode().getHost(),
                            serverNodeWrapper.getGrpcServerNode().getPort());

                    logger.info("Shutdown the managed channel for server node with host:{} and port:{}",
                            serverNodeWrapper.getGrpcServerNode().getHost(),
                            serverNodeWrapper.getGrpcServerNode().getPort());

                    //关闭channel
                    ((ManagedChannel) serverNodeWrapper.getChannelWrapper().getChannel()).shutdown();

                    leaveServerNodeWrappers.add(serverNodeWrapper);
                });
        //删除节点列表中的已离开节点
        serverNodeWrappers.removeAll(leaveServerNodeWrappers);
    }

    public List<ServerNodeWrapper> getServerNodeWrappers() {
        return serverNodeWrappers;
    }
}
