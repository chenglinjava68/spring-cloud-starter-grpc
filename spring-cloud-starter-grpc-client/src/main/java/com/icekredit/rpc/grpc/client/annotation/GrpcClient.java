package com.icekredit.rpc.grpc.client.annotation;

import org.springframework.core.annotation.AliasFor;

import java.lang.annotation.*;

@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface GrpcClient {
    /**
     * 当前GrpcClient所使用的第三方依赖Grpc服务名称
     * @return 服务名称
     */
    @AliasFor("name")
    String value() default "";

    /**
     * value的一个别名
     * @return
     */
    @AliasFor("value")
    String name() default "";

    Class serviceClass() default void.class;

    GrpcClientStub[] grpcClientStubs() default {};
}
