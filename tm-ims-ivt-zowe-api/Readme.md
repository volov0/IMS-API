# tm-ims-ivt-zowe-api
Simple Springboot API accessing IVTNO or IVTCM transaction via IMS TM Resource adapter enabled in ZOWE API ML.


![apiml_app_ivtno.png](apiml_app_ivtno.png?raw=true)

### Prerequisites
Following jars are part of Java on demand feature of IMS. They are located in usr/lpp/ims/ims15/ico 
```
mvn install:install-file -Dfile=imsico.jar -DgroupId=com.ibm.ims -DartifactId=imsico -Dversion=15.1.2 -Dpackaging=jar
mvn install:install-file -Dfile=ccf2.jar -DgroupId=com.ibm.ims -DartifactId=ccf2 -Dversion=15.1.2 -Dpackaging=jar
mvn install:install-file -Dfile=IMSLogin.jar -DgroupId=com.ibm.ims -DartifactId=IMSLogin -Dversion=15.1.2 -Dpackaging=jar
mvn install:install-file -Dfile=CWYBS_AdapterFoundation.jar -DgroupId=com.ibm.ims -DartifactId=CWYBS_AdapterFoundation -Dversion=15.1.2 -Dpackaging=jar
```

Build jar:
```
mvn clean package
```

Run the app:  
```
mvn spring-boot:run 
```
or after building a jar:  
```
java -jar target/tm-ims-ivt-zowe-api-0.0.1-SNAPSHOT.jar
```

