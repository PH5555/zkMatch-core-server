# 1) 베이스 이미지 JDK 21
FROM openjdk:21-jdk-slim

# 2) 컨테이너 내에 JAR을 복사할 디렉토리 생성 및 설정
RUN mkdir -p /app
WORKDIR /app

# 3) 로컬에서 빌드된 JAR 파일을 컨테이너의 /app 폴더로 복사
COPY build/libs/zkMatch-0.0.1-SNAPSHOT.jar app.jar

# 4) 컨테이너가 시작될 때 실행할 명령어
ENTRYPOINT ["java", "-jar", "app.jar"]