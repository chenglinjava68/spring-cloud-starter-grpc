package com.icekredit.rpc.grpc.server.wrapper;

import com.google.common.base.Objects;

public class GrpcServiceWrapper {
    private String serviceName;
    private Class<?> type;
    private Object grpcServiceBean;
    private String version;

    private Class<?> grpcServiceBase;
    private String grpcServiceSignature;

    private static final String DEFAULT_VERSION = "1.0";

    public GrpcServiceWrapper(String serviceName, Class<?> type, Object grpcServiceBean) {
        this(serviceName,type,grpcServiceBean,DEFAULT_VERSION);
    }

    public GrpcServiceWrapper(String serviceName, Class<?> type, Object grpcServiceBean, String version) {
        this.serviceName = serviceName;
        this.type = type;
        this.grpcServiceBean = grpcServiceBean;
        this.version = version;
    }

    public String getServiceName() {
        return serviceName;
    }

    public void setServiceName(String serviceName) {
        this.serviceName = serviceName;
    }

    public Object getGrpcServiceBean() {
        return grpcServiceBean;
    }

    public void setGrpcServiceBean(Object grpcServiceBean) {
        this.grpcServiceBean = grpcServiceBean;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public void setType(Class<?> type) {
        this.type = type;
    }

    public Class<?> getGrpcServiceBase() {
        return grpcServiceBase;
    }

    public void setGrpcServiceBase(Class<?> grpcServiceBase) {
        this.grpcServiceBase = grpcServiceBase;
    }

    public String getGrpcServiceSignature() {
        return grpcServiceSignature;
    }

    public void setGrpcServiceSignature(String grpcServiceSignature) {
        this.grpcServiceSignature = grpcServiceSignature;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        GrpcServiceWrapper that = (GrpcServiceWrapper) o;
        return Objects.equal(serviceName, that.serviceName) &&
                Objects.equal(type, that.type) &&
                Objects.equal(grpcServiceBean, that.grpcServiceBean) &&
                Objects.equal(version, that.version) &&
                Objects.equal(grpcServiceBase, that.grpcServiceBase) &&
                Objects.equal(grpcServiceSignature, that.grpcServiceSignature);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(serviceName, type, grpcServiceBean, version, grpcServiceBase, grpcServiceSignature);
    }

    @Override
    public String toString() {
        return "GrpcServiceWrapper{" +
                "serviceName='" + serviceName + '\'' +
                ", type=" + type +
                ", grpcServiceBean=" + grpcServiceBean +
                ", version='" + version + '\'' +
                ", grpcServiceBase=" + grpcServiceBase +
                ", grpcServiceSignature='" + grpcServiceSignature + '\'' +
                '}';
    }
}
