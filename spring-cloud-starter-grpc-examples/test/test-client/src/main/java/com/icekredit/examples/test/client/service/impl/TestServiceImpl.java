package com.icekredit.examples.test.client.service.impl;

import com.icekredit.examples.test.client.grpc.TestGrpcClient;
import com.icekredit.examples.test.client.service.TestService;
import com.icekredit.phone.AddPhoneToUserRequest;
import com.icekredit.phone.AddPhoneToUserResponse;
import com.icekredit.phone.PhoneType;
import io.grpc.StatusRuntimeException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TestServiceImpl implements TestService {
    @Autowired
    private TestGrpcClient testGrpcClient;

    private Logger logger = LoggerFactory.getLogger(TestGrpcClient.class);

    @Override
    public String addPhoneToUser(int uid, PhoneType phoneType, String phoneNubmer){
        logger.info("Will try to add phone to user " + uid);
        AddPhoneToUserRequest request = AddPhoneToUserRequest.newBuilder().setUid(uid).setPhoneType(phoneType)
                .setPhoneNumber(phoneNubmer).build();
        AddPhoneToUserResponse response = null;
        try {
            response = testGrpcClient.blockingStub().addPhoneToUser(request);
            logger.info("Result: " + response.getResult());

            return String.valueOf(response.getResult());
        } catch (StatusRuntimeException e) {
            logger.info("RPC failed", e);
        }

        return null;
    }
}
