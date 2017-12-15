package com.icekredit.rpc.grpc.server.properties;

/**
 * Created by icekredit on 17-12-7.
 */
public class HsHaProperties {
    private int corePoolSize;
    private int maxPoolSize;
    private int queueSize;
    private int keepAliveTimeMs;

    public int getCorePoolSize() {
        return corePoolSize;
    }

    public void setCorePoolSize(int corePoolSize) {
        this.corePoolSize = corePoolSize;
    }

    public int getMaxPoolSize() {
        return maxPoolSize;
    }

    public void setMaxPoolSize(int maxPoolSize) {
        this.maxPoolSize = maxPoolSize;
    }

    public int getQueueSize() {
        return queueSize;
    }

    public void setQueueSize(int queueSize) {
        this.queueSize = queueSize;
    }

    public int getKeepAliveTimeMs() {
        return keepAliveTimeMs;
    }

    public void setKeepAliveTimeMs(int keepAliveTimeMs) {
        this.keepAliveTimeMs = keepAliveTimeMs;
    }
}
