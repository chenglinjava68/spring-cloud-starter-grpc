package com.icekredit.rpc.grpc.client.consul;

import com.google.common.base.Objects;

import java.util.List;

public class GrpcConsulServerNode extends GrpcServerNode {
    private String node;

    private String serviceId;

    private List<String> tags;

    private String host;

    private int port;

    private String address;

    private boolean isHealth;

    public String getNode() {
        return node;
    }

    public void setNode(String node) {
        this.node = node;
    }

    public String getServiceId() {
        return serviceId;
    }

    public void setServiceId(String serviceId) {
        this.serviceId = serviceId;
    }

    public List<String> getTags() {
        return tags;
    }

    public void setTags(List<String> tags) {
        this.tags = tags;
    }

    @Override
    public String getHost() {
        return host;
    }

    @Override
    public void setHost(String host) {
        this.host = host;
    }

    @Override
    public int getPort() {
        return port;
    }

    @Override
    public void setPort(int port) {
        this.port = port;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public boolean isHealth() {
        return isHealth;
    }

    public void setHealth(boolean health) {
        isHealth = health;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        GrpcConsulServerNode that = (GrpcConsulServerNode) o;
        return port == that.port &&
                isHealth == that.isHealth &&
                Objects.equal(node, that.node) &&
                Objects.equal(serviceId, that.serviceId) &&
                Objects.equal(tags, that.tags) &&
                Objects.equal(host, that.host) &&
                Objects.equal(address, that.address);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(node, serviceId, tags, host, port, address, isHealth);
    }

    @Override
    public String toString() {
        return "GrpcConsulServerNode{" +
                "node='" + node + '\'' +
                ", serviceId='" + serviceId + '\'' +
                ", tags=" + tags +
                ", host='" + host + '\'' +
                ", port=" + port +
                ", address='" + address + '\'' +
                ", isHealth=" + isHealth +
                '}';
    }
}
