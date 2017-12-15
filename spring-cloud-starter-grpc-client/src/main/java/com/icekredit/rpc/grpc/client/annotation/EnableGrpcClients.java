package com.icekredit.rpc.grpc.client.annotation;

import com.icekredit.rpc.grpc.client.GrpcClientRegistrar;
import org.springframework.context.annotation.Import;
import org.springframework.core.annotation.AliasFor;

import java.lang.annotation.*;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@Documented
@Import(GrpcClientRegistrar.class)
public @interface EnableGrpcClients {
    @AliasFor("basePackages")
    String[] values() default {};

    @AliasFor("values")
    String[] basePackages() default {};
}
