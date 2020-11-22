FROM openjdk:8-jdk-alpine
COPY target/provider-0.0.1-SNAPSHOT.jar app.jar

# 如果不是用--net=host 模式 那這要指定的port 會跟application.yml 裡的一樣 
# EXPOSE 50609

# 為了讓不同分支的應用會有不同名子
ENV application-name=ms-provider 

ENTRYPOINT ["java","-jar","/app.jar","--spring.application.name=${application-name}"]
