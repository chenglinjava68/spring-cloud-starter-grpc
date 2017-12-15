package com.icekredit.rpc.grpc.server;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.SmartLifecycle;

import java.io.IOException;
import java.util.concurrent.atomic.AtomicInteger;

public class GrpcServerBootstrap implements SmartLifecycle {
    private GrpcServerGroup grpcServerGroup;

    private Logger logger = LoggerFactory.getLogger(GrpcServerBootstrap.class);

    public GrpcServerBootstrap(GrpcServerGroup grpcServerGroup) {
        this.grpcServerGroup = grpcServerGroup;
    }

    @Override
    public boolean isAutoStartup() {
        return true;
    }

    @Override
    public void stop(Runnable runnable) {
        if (isRunning()) {
            logger.info("shutdown grpc server...");
            grpcServerGroup.getServers().forEach(server -> {
                server.shutdown();
                try {
                    server.awaitTermination();
                } catch (Exception e) {
                    logger.error("shutdown server has encountered a problem:{}", e);
                }

                logger.info("shutdown server success...");
            });

            if (runnable != null) {
                runnable.run();
            }
        }
    }

    @Override
    public void start() {
        logger.info("Starting grpc server...");
        AtomicInteger serverIndex = new AtomicInteger(0);
        grpcServerGroup.getServers().forEach(server -> new Thread(() -> {
            try {
                server.start();
                logger.info("grpc server starting for grpc-server-{} has succeeded!server running on port:{}",serverIndex.incrementAndGet(), server.getPort());
            } catch (IOException e) {
                logger.error("grpc server starting for grpc-server-{} has encountered a problem:{}",serverIndex.incrementAndGet(), e);
            }
        }).start());
    }

    @Override
    public void stop() {
        stop(null);
    }

    @Override
    public boolean isRunning() {
        return grpcServerGroup.getServers().stream()
                .anyMatch(server -> !server.isShutdown() && server.isTerminated());
    }

    @Override
    public int getPhase() {
        return Integer.MAX_VALUE;
    }
}
