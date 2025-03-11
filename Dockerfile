FROM eclipse-temurin:17-jdk-jammy as build
WORKDIR /app

#Copiar Maven wrapper y pom.xml
COPY .mvn/ .mvn/
COPY mvnw pom.xml ./

#Descargar dependencias
RUN ./mvnw dependency:go-offline -B

#Copiar código fuente
COPY src ./src

#Construir la aplicación
RUN ./mvnw package -DskipTests

#Etapa de ejecución
FROM eclipse-temurin:17-jre-jammy
WORKDIR /app

#Copiar JAR desde etapa de build
COPY --from=build /app/target/*.jar Adoptamena.jar

#Exponer puerto
EXPOSE 8080

#Ejecutar la aplicación
ENTRYPOINT ["java","-jar","Adoptamena.jar"]