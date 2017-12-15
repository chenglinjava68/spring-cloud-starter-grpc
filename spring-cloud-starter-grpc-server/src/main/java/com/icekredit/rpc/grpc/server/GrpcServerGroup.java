package com.icekredit.rpc.grpc.server;

import io.grpc.Server;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @author wenchao
 */
public class GrpcServerGroup {
    private List<Server> servers;

    public GrpcServerGroup(Server... server) {
        if (server == null || server.length == 0){
            return;
        }

        if (servers == null){
            servers = new ArrayList<>();
        }

        servers.addAll(Arrays.asList(server));
    }

    public GrpcServerGroup(List<Server> servers){
        if (servers == null || servers.isEmpty()){
            return;
        }

        if (this.servers == null){
            this.servers = new ArrayList<>();
        }

        this.servers.addAll(servers);
    }

    public void addServer(Server server){
        if (server == null){
            return;
        }

        this.servers.add(server);
    }

    public void addServers(List<Server> servers){
        if (servers == null || servers.isEmpty()){
            return;
        }

        this.servers.addAll(servers);
    }

    public List<Server> getServers() {
        return servers;
    }
}
