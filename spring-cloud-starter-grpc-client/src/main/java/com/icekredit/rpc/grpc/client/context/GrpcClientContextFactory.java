package com.icekredit.rpc.grpc.client.context;

public class GrpcClientContextFactory {
    private static GrpcClientContext grpcClientContext;

    public static GrpcClientContext context(){
        if (grpcClientContext == null){
            grpcClientContext = new GrpcClientContext();
        }

        return grpcClientContext;
    }
}
