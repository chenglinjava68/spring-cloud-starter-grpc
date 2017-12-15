## progrium/consul
### 启动单机上的单个实例
```
$ docker run -p 8400:8400 -p 8500:8500 -p 8600:53/udp -h node1 progrium/consul -server -bootstrap
```          
### 如果想通过web-ui进行访问（加上-ui-dir参数）
```
$ docker run -p 8400:8400 -p 8500:8500 -p 8600:53/udp -h node1 progrium/consul -server -bootstrap -ui-dir /ui
```
### 启动单机上的多个实例
#### 启动第一个节点
启动第一个节点时，我们不需要指定-bootstrap参数，而是指定-bootstrap-expect=3,说明我们整个集群需要三个server节点
```
$ docker run -d --name node1 -h node1 progrium/consul -server -bootstrap-expect 3 -ui-dir /ui
```
#### 启动第一个节点之后
我们启动第一个节点之后，我们执行命令获取到第一个节点的docker内部ip地址，然后方便第二个，第三个节点加入（join）到第一个节点中
```
$ JOIN_IP="$(docker inspect -f '{{.NetworkSettings.IPAddress}}' node1)"
```

#### 启动第二个节点
启动第二个节点，并指定join参数，让第二个节点加入到第一个节点的集群中
```
$ docker run -d --name node2 -h node2 progrium/consul -server -ui-dir /ui -join $JOIN_IP
```
####启动第三个节点
我们用同样的方式来启动第三个节点
```
$ docker run -d --name node3 -h node3 progrium/consul -server -ui-dir /ui -join $JOIN_IP
```

#### 启动第四个节点
前面三个节点都是按照server方式启动的，但是现在有一个问题，这三个节点所在的docker容器并没有暴露任何的端口，无法进行访问
现在我们如果想要访问这个集群，我们可以添加第四个节点，并且让第四个节点以client节点的方式加入到集群中。此外需要注意client节点只是单纯的做数据转发，所有服务信息只会在server节点进行持久化
```
$ docker run -d -p 8400:8400 -p 8500:8500 -p 8600:53/udp --name node4 -h node4 progrium/consul -join $JOIN_IP
```
现在访问HOST：8500就可以看到consul的web界面了


#### 启动单个节点
docker run -d -p 8400:8400 -p 8500:8500 -p 8600:53/udp --name consul-server -h consul-server progrium/consul -server -bootstrap-expect 1 -ui-dir /ui -advertise 192.168.1.40 

## gliderlabs/registrator
docker run -d -v /var/run/docker.sock:/tmp/docker.sock --name registrator -h registrator  gliderlabs/registrator:latest consul://192.168.1.40:8500

### 引用
* https://hub.docker.com/r/progrium/consul/

docker build -t test-server .
docker run -d -p 50051:50051 --name test-server-01 \
    -e "SERVICE_50051_NAME=grpc-rpc-test" \
    -e "SERVICE_50051_CHECK_TCP=/" \
    -e "SERVICE_50051_CHECK_INTERVAL=30s" \
    -e "SERVICE_50051_CHECK_TIMEOUT=3s" \
    -e "SERVICE_50051_TAGS=grpc-rpc-test-50051" \
     test-server