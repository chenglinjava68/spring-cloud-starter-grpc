package com.icekredit.rpc.grpc.client.consul;

import com.google.common.collect.Sets;
import com.orbitz.consul.Consul;
import com.orbitz.consul.HealthClient;
import com.orbitz.consul.model.health.HealthCheck;
import com.orbitz.consul.model.health.Node;
import com.orbitz.consul.model.health.Service;
import com.orbitz.consul.model.health.ServiceHealth;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.collections4.MapUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.Inet6Address;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class GrpcConsulServerNodeList extends GrpcServerNodeList<GrpcConsulServerNode> {
    private Logger logger = LoggerFactory.getLogger(getClass());

    private HealthClient healthClient;

    private GrpcConsulServerNodeList(Consul consul) {
        this.healthClient = consul.healthClient();
    }

    private static volatile GrpcConsulServerNodeList grpcConsulServerNodeList;

    public static GrpcConsulServerNodeList getSingleInstance(Consul consul){
        if (grpcConsulServerNodeList == null){
            synchronized (GrpcConsulServerNodeList.class){
                if (grpcConsulServerNodeList == null){
                    grpcConsulServerNodeList = new GrpcConsulServerNodeList(consul);
                }
            }
        }

        return grpcConsulServerNodeList;
    }

    @Override
    public Set<GrpcConsulServerNode> getGrpcServerNodes(String serviceName) {
        Set<GrpcConsulServerNode> allServerNodes;

        if (MapUtils.isNotEmpty(serverNodeMap) || serverNodeMap.containsKey(serviceName)){
            allServerNodes = serverNodeMap.get(serviceName);
        } else {
            allServerNodes = refreshGrpcServerNodes(serviceName);
        }

        return allServerNodes.stream()
                .filter(GrpcConsulServerNode::isHealth)
                .collect(Collectors.toSet());
    }

    @Override
    public Set<GrpcConsulServerNode> refreshGrpcServerNodes(String serviceName) {
        Set<GrpcConsulServerNode> serverNodes = Sets.newHashSet();

        List<ServiceHealth> serviceHealths = healthClient.getAllServiceInstances(serviceName).getResponse();

        GrpcConsulServerNode grpcConsulServerNode;
        Service service;
        Node node;
        List<HealthCheck> healthChecks;
        for (ServiceHealth serviceHealth:serviceHealths){
            service = serviceHealth.getService();
            node = serviceHealth.getNode();
            healthChecks = serviceHealth.getChecks();

            grpcConsulServerNode = new GrpcConsulServerNode();

            grpcConsulServerNode.setAddress(service.getAddress());
            grpcConsulServerNode.setHost(getHost(service,node));
            grpcConsulServerNode.setPort(service.getPort());
            grpcConsulServerNode.setServiceId(service.getService());
            grpcConsulServerNode.setTags(service.getTags());
            grpcConsulServerNode.setHealth(isHealth(healthChecks));

            serverNodes.add(grpcConsulServerNode);
        }

        if (CollectionUtils.isNotEmpty(serverNodes)){
            this.serverNodeMap.put(serviceName,serverNodes);
        }

        return serverNodes;
    }

    private boolean isHealth(List<HealthCheck> healthChecks) {
        if (healthChecks == null || healthChecks.isEmpty()){
            return false;
        }

        for (HealthCheck healthCheck:healthChecks){
            if (!healthCheck.getStatus().equals("passing")){
                return false;
            }
        }

        return true;
    }

    private String getHost(Service service, Node node) {
        if (StringUtils.isNotBlank(service.getAddress())){
            return parseHostWithIpv6Compatibility(service.getAddress());
        }

        return parseHostWithIpv6Compatibility(node.getAddress());
    }

    /**
     * 在一个URL中使用一文本IPv6地址，文本地址应该用符号“[”和“]”来封闭。
     * @param primitiveAddress 原始地址
     * @return 如果是IpV6类型的地址，用[]包裹后返回，否则直接返回原始地址
     */
    private String parseHostWithIpv6Compatibility(String primitiveAddress){
        try {
            InetAddress inetAddress = InetAddress.getByName(primitiveAddress);

            if (inetAddress instanceof Inet6Address){
                return "[" + inetAddress.getHostName() + "]";
            }

            return primitiveAddress;
        } catch (UnknownHostException e) {
            logger.error("Sorry!Unknown host {} founded!",primitiveAddress);

            return primitiveAddress;
        }
    }
}
