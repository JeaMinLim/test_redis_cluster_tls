# Redis Cluster with TLS test code #
This project is to test Redis cluster connection with TLS(Transport Layer Security) support.
Before start, you must have Redis cluster with TLS options. 

## How to use
You must have SpringBoot IDE for test run. 
Without verifying Redis hosts, You can simpley enable .withVerifyPeer(false) on Controller.java file. 