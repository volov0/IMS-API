# IMS-API
IMS API demonstration

This repository contains sample applications accessing IMS resources.

### tm-ims-ivt-app
Simple java command line application accessing IVTNO or IVTCM transaction via TM resource adapter.

### tm-ims-ivt-api
Simple Springboot API accessing IVTNO or IVTCM transaction via TM resource adapter.

### tm-ims-ivt-zowe-api
Same as tm-ims-ivt-api but this time enabled in ZOWE API ML.

### tm-ims-ivt-zowe-api-passticket
tm-ims-ivt-zowe-api with PassTicket support.

### db-ims-zowe-api
Springboot API enabled in ZOWE API ML with the same output as tm-ims-ivt-zowe-api but this time underlying mechanism is JDBC instead of TM resource adapter.
