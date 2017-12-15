## Grpc Spring Cloud Starter
### Grpc Spring Cloud Client Starter
#### 特点
* 提供声明式客户端支持，并与Spring IOC容器相结合，实现Bean的自动生成以及依赖注入
* 加入基于Consul的服务注册与发现模块，实现Grpc客户端与Grpc服务端的解藕，同时也便于服务端扩展
#### 问题点
##### 问题点1
如果计划同IOC容器也就是SpringBoot应用上下文进行结合，并实现Bean的依赖注入，\
关键在于基于BeanDefinition生成Bean以及Bean的代理(delegate)，但是AbstractStub基类\
没有无参构造方法，且其带参构造方法对构造函数入参均做了非空校验，这样无法获得代理对象(delegate)。
##### 问题点2
Grpc的AbstractStub实现类XXXStub、XXXBlockStub，XXXFutureStub均是final的，通常\
实现代理有两种方式，如果待代理类有显式的接口声明，我们使用JDK动态代理，如果待代理类是Concrete Class,\
那么我们使用Cglib代理。Cglib代理实现方式是通过继承来实现对父类的代理或者说是增强。但是final修饰符
修饰的类是无法被继承的，这样创建Cglib代理是就会引发异常。