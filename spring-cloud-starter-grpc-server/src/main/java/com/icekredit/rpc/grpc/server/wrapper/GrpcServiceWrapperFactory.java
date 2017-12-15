package com.icekredit.rpc.grpc.server.wrapper;


import com.icekredit.rpc.grpc.server.exception.NoGrpcInterfaceFoundException;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.regex.Pattern;

public class GrpcServiceWrapperFactory {
    private static final Pattern PATTERN_FOR_GRPC_IMPL_BASE = Pattern.compile(".+Grpc\\$.+Base$");

    private static Logger logger = LoggerFactory.getLogger(GrpcServiceWrapperFactory.class);

    public static GrpcServiceWrapper wrap(String microServiceName,String grpcServiceName,Object grpcServiceBean,String version){
        GrpcServiceWrapper grpcServiceWrapper;

        if (StringUtils.isBlank(version)){
            grpcServiceWrapper = new GrpcServiceWrapper(microServiceName,grpcServiceBean.getClass(),grpcServiceBean);
        } else {
            grpcServiceWrapper = new GrpcServiceWrapper(microServiceName,grpcServiceBean.getClass(),grpcServiceBean,version);
        }

        Class superClass = grpcServiceBean.getClass().getSuperclass();
        boolean isExtendFromServiceImplBase = false;
        while (true){
            if (superClass.getName().equals(Object.class.getName())){
                break;
            }

            if (PATTERN_FOR_GRPC_IMPL_BASE.matcher(superClass.getName()).matches()){
                isExtendFromServiceImplBase = true;

                break;
            }
        }

        if (!isExtendFromServiceImplBase){
            logger.error("No grpc interface founded for grpc service:{}！",grpcServiceName);

            throw new NoGrpcInterfaceFoundException("No grpc interface founded for grpc service:" + grpcServiceName);
        }

        grpcServiceWrapper.setGrpcServiceBase(superClass);
        String serviceSignature = StringUtils.join(new String[]{microServiceName,superClass.getSimpleName(),version},"$");
        grpcServiceWrapper.setGrpcServiceSignature(serviceSignature);

        return grpcServiceWrapper;
    }
}
