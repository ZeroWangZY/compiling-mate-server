FROM java:8
COPY ./build/libs/api.jar /usr/src/myapp/
WORKDIR /usr/src/myapp
CMD ["java", "-jar", "api.jar"]