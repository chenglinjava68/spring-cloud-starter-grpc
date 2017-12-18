package com.icekredit.rpc.grpc.client.annotation;

import io.grpc.stub.AbstractStub;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 *
 */
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface GrpcClientStub {
    String name() default "";
    String value() default "";
    Class<? extends AbstractStub> clientStubClass() default AbstractStub.class;
}
