package com.icekredit.examples.test.client.service;

import com.icekredit.phone.PhoneType;

public interface TestService {
    String addPhoneToUser(int uid, PhoneType phoneType, String phoneNubmer);
}
