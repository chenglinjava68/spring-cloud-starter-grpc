package com.icekredit.rpc.grpc.client.wrapper;

import io.grpc.stub.AbstractStub;
import org.apache.commons.lang3.StringUtils;

import java.util.HashMap;
import java.util.Map;

public class StubWrapper {
    private Map<String,AbstractStub> stubMap;

    private static final String STUB = "stub";
    private static final String BLOCKING_STUB = "blockingStub";
    private static final String FUTURE_STUB = "futureStub";

    public StubWrapper() {
        this.stubMap = new HashMap<>(2);
    }

    public void setStub(AbstractStub stub){
        this.stubMap.put(STUB,stub);
    }

    public void setBlockingStub(AbstractStub blockingStub){
        this.stubMap.put(BLOCKING_STUB,blockingStub);
    }

    public void setFutureStub(AbstractStub futureStub){
        this.stubMap.put(FUTURE_STUB,futureStub);
    }

    public AbstractStub getCorrespondingStub(String stubType){
        if (StringUtils.isBlank(stubType)) {
            return null;
        }

        if(!stubMap.containsKey(stubType)){
            return null;
        }

        return stubMap.get(stubType);
    }
}
