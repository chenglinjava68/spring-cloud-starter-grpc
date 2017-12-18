package com.icekredit.rpc.grpc.client.scanner;

import io.grpc.stub.AbstractStub;

public class GrpcClientStubDefinition {
    private String clientStubQualifier;
    private Class<? extends AbstractStub> clientStubClass;

    public GrpcClientStubDefinition(String clientStubQualifier, Class<? extends AbstractStub> clientStubClass) {
        this.clientStubQualifier = clientStubQualifier;
        this.clientStubClass = clientStubClass;
    }

    public String getClientStubQualifier() {
        return clientStubQualifier;
    }

    public void setClientStubQualifier(String clientStubQualifier) {
        this.clientStubQualifier = clientStubQualifier;
    }

    public Class<? extends AbstractStub> getClientStubClass() {
        return clientStubClass;
    }

    public void setClientStubClass(Class<? extends AbstractStub> clientStubClass) {
        this.clientStubClass = clientStubClass;
    }
}
