#start website
java -Xdebug -Xrunjdwp:server=y,transport=dt_socket,address=5005,suspend=n \
    -jar ./web/target/web-1.0.0.jar
#java -jar ./web/target/web-1.0.0.jar
