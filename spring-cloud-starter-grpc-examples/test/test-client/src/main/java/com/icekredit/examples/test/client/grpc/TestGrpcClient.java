package com.icekredit.examples.test.client.grpc;

import com.icekredit.phone.PhoneServiceGrpc;
import com.icekredit.rpc.grpc.client.annotation.GrpcClient;
import com.icekredit.rpc.grpc.client.annotation.GrpcClientAware;
import com.icekredit.rpc.grpc.client.annotation.GrpcClientStub;

@GrpcClient(
        name = "grpc-rpc-test",
        serviceClass=PhoneServiceGrpc.class,
        grpcClientStubs = {
                @GrpcClientStub(
                        name = "phoneServiceStub",
                        clientStubClass = PhoneServiceGrpc.PhoneServiceStub.class
                ),
                @GrpcClientStub(
                        name = "phoneServiceBlockingStub",
                        clientStubClass = PhoneServiceGrpc.PhoneServiceBlockingStub.class
                ),
                @GrpcClientStub(
                        name = "phoneServiceFutureStub",
                        clientStubClass = PhoneServiceGrpc.PhoneServiceFutureStub.class
                ),
        })
public interface TestGrpcClient extends GrpcClientAware<
        PhoneServiceGrpc,PhoneServiceGrpc.PhoneServiceStub,
        PhoneServiceGrpc.PhoneServiceBlockingStub,PhoneServiceGrpc.PhoneServiceFutureStub> {
}
