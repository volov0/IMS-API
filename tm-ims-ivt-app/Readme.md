# tm-ims-ivt-app
Simple or command line application accessing IVTNO or IVTCM transaction via IMS TM Resource adapter.


![standalone_app_ivtno.png](standalone_app_ivtno.png?raw=true)

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
mvn clean compile assembly:single
```

Run the app:
```
java -jar target/tm-ims-IVT-app-0.0.1-SNAPSHOT-jar-with-dependencies.jar LAST3
```
