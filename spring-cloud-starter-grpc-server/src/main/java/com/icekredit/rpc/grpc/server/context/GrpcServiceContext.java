package com.icekredit.rpc.grpc.server.context;

import com.icekredit.rpc.grpc.server.properties.GrpcServerProperties;
import com.icekredit.rpc.grpc.server.properties.HsHaProperties;
import com.icekredit.rpc.grpc.server.wrapper.GrpcServiceWrapper;
import io.grpc.BindableService;
import io.grpc.Server;
import io.grpc.ServerBuilder;

import java.util.List;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by icekredit on 17-12-6.
 */
public class GrpcServiceContext extends AbstractGrpcServiceContext{
    public GrpcServiceContext(GrpcServerProperties properties, List<GrpcServiceWrapper> serviceWrappers) {
        super(properties, serviceWrappers);
    }

    @Override
    public Server buildServer() {
        ServerBuilder serverBuilder = ServerBuilder.forPort(properties.getPort());

        for (GrpcServiceWrapper serviceWrapper:serviceWrappers){
            serverBuilder.addService((BindableService) serviceWrapper.getGrpcServiceBean());
        }

        serverBuilder.executor(buildServicePool(properties.getHsHa()));

        return serverBuilder.build();
    }

    private Executor buildServicePool(HsHaProperties hsHa) {
        return new ThreadPoolExecutor(
                hsHa.getCorePoolSize(),
                hsHa.getMaxPoolSize(),
                hsHa.getKeepAliveTimeMs(),
                TimeUnit.MILLISECONDS,
                new LinkedBlockingDeque<>(hsHa.getQueueSize()),
                new ThreadFactory() {
                    private AtomicInteger atomicInteger = new AtomicInteger(0);

                    @Override
                    public Thread newThread(Runnable runnable) {
                        Thread thread = new Thread(runnable);
                        thread.setName("grpc-server-worker-thread-" + atomicInteger.incrementAndGet());
                        return thread;
                    }
                });
    }
}
