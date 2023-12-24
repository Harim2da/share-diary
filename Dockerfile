# Use the official OpenJDK base image
FROM openjdk:11-jdk

# Copy the jar file into the image
COPY ./build/libs/diray-0.0.1-SNAPSHOT.jar app.jar

# Set the entry point to start the application
ENTRYPOINT ["java","-jar","/app.jar"]