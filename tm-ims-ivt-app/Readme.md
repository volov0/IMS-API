# tm-ims-ivt-app
Simple or command line application accessing IVTNO or IVTCM transaction via IMS TM Resource adapter.


![standalone_app_ivtno.png](standalone_app_ivtno.png?raw=true)

Build jar:
```
mvn clean compile assembly:single
```

Run the app:
```
java -jar target/tm-ims-IVT-app-0.0.1-SNAPSHOT-jar-with-dependencies.jar LAST3
```
