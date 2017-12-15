package com.icekredit.rpc.grpc.server.exception;

public class NoGrpcInterfaceFoundException extends RuntimeException{
    public NoGrpcInterfaceFoundException(String message) {
        super(message);
    }
}
