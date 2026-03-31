FROM eclipse-temurin:17-jre
WORKDIR /app

RUN groupadd -r app && useradd -r -g app -u 10001 app
COPY --chown=app:app build/libs/*.jar app.jar

EXPOSE 8080
ENV JAVA_OPTS=""
USER app
ENTRYPOINT ["sh", "-c", "java $JAVA_OPTS -jar app.jar"]