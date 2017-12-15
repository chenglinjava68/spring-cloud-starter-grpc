package com.icekredit.rpc.grpc.server.exception;

/**
 * Created by icekredit on 17-12-6.
 */
public class GrpcServiceInstantiateException extends RuntimeException {
    public GrpcServiceInstantiateException(String message) {
        super(message);
    }
}
