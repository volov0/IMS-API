# db-ims-zowe-api
Springboot API enabled in ZOWE API ML with the same output as tm-ims-ivt-zowe-api but this time underlying mechanism is JDBC instead of TM resource adapter.

![apiml_jdbcapp.png](apiml_jdbcapp.png?raw=true)

### Prerequisites
imsudb.jar (IMS Universal JDBC/DLI driver) is part of Java on demand feature of IMS. They are located in usr/lpp/ims/ims15/imsjava
```
mvn install:install-file -Dfile=imsudb.jar -DgroupId=com.ibm.ims -DartifactId=imsudb -Dversion=14 -Dpackaging=jar
```

### Build jar:
```
mvn clean package
```

### Run the app:  
```
mvn spring-boot:run 
```
or after building a jar:  
```
java -jar target/db-ims-api-0.0.1-SNAPSHOT.jar
```

