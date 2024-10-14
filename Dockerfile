FROM openjdk:22

EXPOSE 8080

ADD target/FriendBook1-0.0.1-SNAPSHOT.jar FriendBook1-0.0.1-SNAPSHOT.jar

ENTRYPOINT [ "java", "-jar", "FriendBook1-0.0.1-SNAPSHOT.jar" ]
