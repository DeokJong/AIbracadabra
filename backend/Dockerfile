#=================================================================
# BUILD STAGE 1 - JRE 추출
#=================================================================
FROM amazoncorretto:17-alpine3.18 AS builder-jre

# binutils 설치 (jlink용)
RUN apk add --no-cache binutils

# jlink로 최소화된 JRE 생성
RUN $JAVA_HOME/bin/jlink \
    --module-path "$JAVA_HOME/jmods" \
    --verbose \
    --add-modules ALL-MODULE-PATH \
    --strip-debug \
    --no-man-pages \
    --no-header-files \
    --compress=2 \
    --output /jre

#=================================================================
# BUILD STAGE 2 - Build the application using Maven
#=================================================================
FROM maven:3.9.6-eclipse-temurin-17-alpine AS builder-app

WORKDIR /workspace

# 먼저 pom.xml만 복사해서 의존성 캐싱
COPY pom.xml .

# 소스 복사
COPY src ./src

# 패키징 (테스트 스킵)
RUN mvn clean package -DskipTests

#=================================================================
# BUILD STAGE 3 - RUN APP
#=================================================================
FROM alpine:3.18.4

# 최소화된 JRE 경로 설정
ENV JAVA_HOME=/jre
ENV PATH="$JAVA_HOME/bin:$PATH"

# jre 복사
COPY --from=builder-jre /jre $JAVA_HOME

# 작업 디렉토리 설정
WORKDIR /app

# Maven 빌드 결과물 복사
COPY --from=builder-app /workspace/target/*.jar app.jar

# 리소스 디렉토리 생성 및 권한 설정 (필요시)
RUN mkdir -p /app/resources && chmod -R 755 /app/resources

# 컨테이너 포트 노출
EXPOSE 8080

# 애플리케이션 실행
ENTRYPOINT ["java", "-jar", "app.jar"]
