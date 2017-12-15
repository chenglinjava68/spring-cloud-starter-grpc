FROM registry.icekredit.com:443/base/jre8:122
ADD ../target/test-server.jar test-server.jar
ENTRYPOINT ["java", "-jar", "test-server.jar"]