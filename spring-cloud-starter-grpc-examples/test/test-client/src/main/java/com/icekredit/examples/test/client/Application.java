package com.icekredit.examples.test.client;

import com.icekredit.rpc.grpc.client.annotation.EnableGrpcClients;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@EnableGrpcClients(basePackages = {"com.icekredit.examples.test.client.grpc"})
public class Application {
    public static void main(String[] args) {
        SpringApplication.run(Application.class,args);
    }
}
