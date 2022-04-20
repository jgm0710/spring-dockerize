# Spring Deployment setting with docker

## 파일 구조

기본 spring 프로젝트에 대한 설명은 생략합니다.

### Dockerfile

`docker-compose.yml` file 이 실행될 때 해당 build 된 spring jar file 을 사용하여 컨테이너 이미지를 생성하기 위한 설정입니다.

#### Code

```
FROM openjdk:11
EXPOSE 8080
ARG JAR_FILE=build/libs/*SNAPSHOT.jar
COPY ${JAR_FILE} app.jar
```

#### Description

spring jar file 을 실행할 수 있도록, container 내부적으로 jdk image 가 필요합니다.

```
FROM openjdk:11
```

Expose port 는 8080 port 로 지정합니다.

```
EXPOSE 8080
```

`JAR_FILE` 변수에 `build/libs/*SNAPSHOT.jar` 를 할당합니다.

spring boot 2.5.x 이후의 버전에서는 gradle build 시 `~SNAPSHOT.jar` 파일과 `SNAPSHOT-plain.jar` 이 생성되므로 `*.jar` 형식이 아닌 `*SNAPSHOT.jar` 형식으로 file path 를 지정합니다. 

```
ARG JAR_FILE=build/libs/*SNAPSHOT.jar
```

`build/libs/*SNAPSHOT.jar` 파일을 `app.jar` 파일명으로 copy 합니다.
해당 `app.jar` 파일은 이후 `docker-compose.yml` 파일에서 `entrypoint` option 실행시킬 파일이 됩니다.

```
COPY ${JAR_FILE} app.jar
```


### docker-compose.yml

#### Code

```
version: '3'
services:
  application:
    container_name: sample-spring-container
    build:
      context: .
      dockerfile: Dockerfile
    ports:
      - "9191:8080"
    restart: always
    entrypoint: java -jar -Dspring.profiles.active=default app.jar
#    volumes:
#      - /home/ubuntu/upload:/home/ubuntu/upload
```

#### Description

`docker-compose.yml` 을 실행시킬 때 build 할 이미지인 `Dockerfile` 의 위치와 파일명을 지정합니다.

```
build:
      context: .
      dockerfile: Dockerfile
```

spring container 의 실행 port 를 9191 port 로 지정합니다.

spring 기본 실행 port 는 8080 port 이므로 ubuntu local pc 의 9191 port 와 spring container 의 8080 port 를 마운트 해줍니다.

```
ports:
      - "9191:8080"
```

entrypoint option 을 사용하여 build 된 spring jar file 을 실행합니다.

실행하려는 실행 환경이 default 이기 때문에 `-Dspring.profiles.active` option 은 생략이 가능하지만, 
실행하려는 profile option 이 달라질 경우 profile 을 지정하여 실행할 수 있습니다. 

```
entrypoint: java -jar -Dspring.profiles.active=default app.jar
```

다음 옵션은 볼륨 마운팅을 진행할 경우에 대한 추가 설정입니다.

spring container 내부의 directory 와 ubuntu local pc 의 볼륨을 일치시킬 필요가 있을 경우 설정합니다.

다음의 경우 파일을 저장할 경우 파일이 저장될 directory 에 대해 볼륨 마운팅 설정을 한 경우입니다.

```
#    volumes:
#      - /home/ubuntu/upload:/home/ubuntu/upload
```

## 실행

프로젝트를 실행 시키기 위해서는 우선 build/libs directory 에 build 된 spring jar file 이 존재해야 합니다.

gradlew 를 사용하여 프로젝트를 build 해 줍니다.

기존에 build 되었던 class file 이나, 리소스 파일이 남아 있을 수 있으므로 clean 명령어도 함께 사용합니다.

```
./gradlew clean build
```

프로젝트가 정상적으로 build 되었다면 아래 명령어를 사용하여 build 된 jar 파일을 간단하게 docker-compose 명령어를 사용하여 실행 시킬 수 있습니다.

```
docker-compose up -d --build
```

컨테이너가 정상적으로 실행되고 있는지 확인하기 위해서 다음 명령어를 사용합니다.

```
docker ps
```

다음 명령어를 사용하여 실행 중인 docker container 의 실행 로그를 확인할 수 있습니다.

```
docker-compose logs -f
```

프로젝트 실행을 중지하기 위해서는 다음 명령어를 실행합니다.

```
docker-compose down
```
