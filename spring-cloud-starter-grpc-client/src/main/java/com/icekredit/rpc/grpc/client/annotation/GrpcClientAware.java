package com.icekredit.rpc.grpc.client.annotation;

public interface GrpcClientAware<T,Stub,BlockingStub,FutureStub> {
    Class<T> outerClass();
    Stub stub();
    BlockingStub blockingStub();
    FutureStub futureStub();
}
