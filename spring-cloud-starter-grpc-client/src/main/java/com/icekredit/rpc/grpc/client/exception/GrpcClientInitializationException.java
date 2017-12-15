package com.icekredit.rpc.grpc.client.exception;

public class GrpcClientInitializationException extends RuntimeException{
    public GrpcClientInitializationException(String message) {
        super(message);
    }
}
