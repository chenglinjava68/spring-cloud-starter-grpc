package com.icekredit.examples.test.client.service;

import com.icekredit.rpc.grpc.server.annotation.GrpcService;
import com.icekredit.phone.AddPhoneToUserRequest;
import com.icekredit.phone.AddPhoneToUserResponse;
import com.icekredit.phone.PhoneServiceGrpc;
import io.grpc.stub.StreamObserver;

@GrpcService(name = "phone-grpc-service")
public class PhoneServiceImpl extends PhoneServiceGrpc.PhoneServiceImplBase {
    @Override
    public void addPhoneToUser(AddPhoneToUserRequest request, StreamObserver<AddPhoneToUserResponse> responseObserver) {
        long startTime = System.currentTimeMillis();
        AddPhoneToUserResponse response;
        if (request.getPhoneNumber().length() == 11) {
            System.out.printf("uid = %s , phone type is %s, nubmer is %s\n", request.getUid(), request.getPhoneType(), request.getPhoneNumber());
            response = AddPhoneToUserResponse.newBuilder().setResult(true).build();
        } else {
            System.out.printf("The phone nubmer %s is wrong!\n", request.getPhoneNumber());
            response = AddPhoneToUserResponse.newBuilder().setResult(false).build();
        }
        responseObserver.onNext(response);
        responseObserver.onCompleted();
        System.out.println(System.currentTimeMillis() - startTime);
    }
}
