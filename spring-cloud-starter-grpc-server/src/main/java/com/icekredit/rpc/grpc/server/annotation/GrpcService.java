package com.icekredit.rpc.grpc.server.annotation;

import org.springframework.core.annotation.AliasFor;
import org.springframework.stereotype.Component;

import java.lang.annotation.*;

/**
 * Grpc服务端点注解
 *
 * @author wenchao
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Inherited
@Component
public @interface GrpcService {
    @AliasFor("value")
    String name() default "";

    @AliasFor("name")
    String value() default "";

    String version() default "1.0";
}
