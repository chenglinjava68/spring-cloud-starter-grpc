package com.icekredit.rpc.grpc.client.factory;

import com.google.common.util.concurrent.ThreadFactoryBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;

public class ServerNodeUpdaterImpl implements ServerNodeUpdater {
    private final AtomicBoolean isStarted = new AtomicBoolean(false);
    private volatile ScheduledFuture<?> scheduledFuture;

    private final long initialDelayMs;
    private static final long DEFAULT_INITIAL_DELAY_MS = 30000;

    private final long refreshIntervalMs;
    private static final long DEFAULT_REFRESH_INTERVAL_MS = 30000;

    private Logger logger = LoggerFactory.getLogger(ServerNodeUpdaterImpl.class);

    public ServerNodeUpdaterImpl() {
        this(DEFAULT_REFRESH_INTERVAL_MS);
    }

    public ServerNodeUpdaterImpl(long refreshIntervalMs) {
        this(DEFAULT_INITIAL_DELAY_MS, refreshIntervalMs);
    }

    public ServerNodeUpdaterImpl(long initialDelayMs, long refreshIntervalMs) {
        this.initialDelayMs = initialDelayMs;
        this.refreshIntervalMs = refreshIntervalMs;
    }

    private static class LazyHolder {
        private static final int CORE_UPDATE_THREAD = 2;
        private static Thread shutdownThread;
        private static ScheduledThreadPoolExecutor scheduledThreadPoolExecutor;

        private static Logger logger = LoggerFactory.getLogger(LazyHolder.class);

        static {
            ThreadFactory threadFactory =
                    new ThreadFactoryBuilder()
                            .setNameFormat("GrpcServerUpdater-%d")
                            .setDaemon(true)
                            .build();

            scheduledThreadPoolExecutor = new ScheduledThreadPoolExecutor(CORE_UPDATE_THREAD, threadFactory);

            shutdownThread = new Thread(() -> {
                logger.info("Shutdown the grpc server node update ScheduledThreadPoolExecutor!");

                shutdownScheduledThreadPoolExecutor();
            });

            Runtime.getRuntime().addShutdownHook(shutdownThread);
        }

        private static void shutdownScheduledThreadPoolExecutor() {
            if (scheduledThreadPoolExecutor == null) {
                return;
            }

            if (shutdownThread != null) {
                try {
                    Runtime.getRuntime().removeShutdownHook(shutdownThread);
                } catch (Exception e) {
                    logger.error("Failed to shutdown the grpc server node update ScheduledThreadPoolExecutor!");
                }
            }
        }

        static ScheduledThreadPoolExecutor getServerNodeUpdateExecutor() {
            return scheduledThreadPoolExecutor;
        }
    }

    @Override
    public void start(UpdateAction updateAction) {
        if (isStarted.compareAndSet(false, true)) {
            Runnable serverNodeUpdateRunnable = () -> {
                if (!isStarted.get()) {
                    if (scheduledFuture != null) {
                        scheduledFuture.cancel(true);
                    }

                    return;
                }

                try {
                    updateAction.update();
                } catch (Exception e) {
                    logger.error("Execute server node update has encountered a problem", e);
                }
            };

            scheduledFuture = LazyHolder.getServerNodeUpdateExecutor().scheduleWithFixedDelay(
                    serverNodeUpdateRunnable,
                    initialDelayMs,
                    refreshIntervalMs,
                    TimeUnit.MILLISECONDS
            );
        } else {
            logger.error("The grpc server node update executor already started!");
        }
    }

    @Override
    public void stop() {
        if (isStarted.compareAndSet(true,false)){
            if (scheduledFuture != null){
                scheduledFuture.cancel(true);
            }
        } else {
            logger.error("The grpc server node update executor already Stopped!");
        }
    }
}
