package com.icekredit.rpc.grpc.client.scanner;

public class GrpcClientStubDefinition {
    private String clientStubQualifier;
    private Class clientStubClass;

    public GrpcClientStubDefinition(String clientStubQualifier, Class clientStubClass) {
        this.clientStubQualifier = clientStubQualifier;
        this.clientStubClass = clientStubClass;
    }

    public String getClientStubQualifier() {
        return clientStubQualifier;
    }

    public void setClientStubQualifier(String clientStubQualifier) {
        this.clientStubQualifier = clientStubQualifier;
    }

    public Class getClientStubClass() {
        return clientStubClass;
    }

    public void setClientStubClass(Class clientStubClass) {
        this.clientStubClass = clientStubClass;
    }
}
