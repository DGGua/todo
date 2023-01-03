#
# BUILD STAGE
#
FROM registry.cn-hangzhou.aliyuncs.com/acs/maven AS build  
COPY src /usr/src/app/src  
COPY pom.xml /usr/src/app  
RUN mvn -f /usr/src/app/pom.xml clean package

#
# PACKAGE STAGE
#
FROM openjdk
COPY --from=build /usr/src/app/target/todo-1.0-SNAPSHOT.jar /usr/app/todo-1.0-SNAPSHOT.jar
EXPOSE 8080  
CMD ["java","-jar","/usr/app/todo-1.0-SNAPSHOT.jar"]  