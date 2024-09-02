Redis Cluster with TLS test code
================================

This project is to test Redis cluster connection with TLS(Transport Layer Security) support.
Before start, you must have Redis cluster with TLS options. 

# How to use

## TLS without host verification 
You must have SpringBoot IDE for test run. 
Without verifying Redis hosts, You can simpley access URL with ```http://SERVERIP/connect/without```. 

## TLS with host verification 
Host verification need proper PKI(Public Key Inferstructure) certificates.
Use ```gen-test-certs.sh``` script from you Redis source code. (after extract Redis source code, you will find ```utils/gen-test-certs.sh```)

When you run this script you will get several files, however we olny need ```client.crt```, ```client.key```, and ```ca.crt``` for host verification using TLS. Use this command for create a keystore convert from certification which generated from ```gen-test-certs.sh```. 

```Shell
# Run on Redis host
openssl pkcs12 -export -in tls/client.crt -inkey tls/client.key -out client.p12 -name client-cert -CAfile tls/ca.crt -caname root

# Run on application(e.g. Tomcat WAS node etc.)
keytool -importkeystore -deststorepass your_keystore_password -destkeypass your_keystore_password -destkeystore client.keystore -srckeystore client.p12 -srcstoretype PKCS12 -srcstorepass your_keystore_password -alias client-cert
```
After run openssl and keytool, you will get ```client.jks``` and ```client.keystore```. copy both files in to ```jre/lib/securiy``` in your ```JAVA_HOME```

-------

## How to convert certificate to JKS

### 1. Convert client certificates to PEM format
```Shell
openssl x509 -in ./ca.crt -out ./jks/ca.pem -outform PEM
```
```Shell
openssl x509 -in ./client.crt -out ./jks/client.pem -outform PEM
openssl rsa -in ./client.key -out ./jks/client-key.pem -outform PEM
```

### 2. Convert client certificates to PKCS12 format from PEM
(if your certificate didn`t have password, delete ```-password```)
```Shell
openssl pkcs12 -export -in ./jks/client.pem -inkey ./jks/client-key.pem -out ./jks/client.p12 -name redis -CAfile ./jks/ca.pem -caname root -password CHANGEIT!
```Shell

### 3. Convert client certificates to JKS format from PKCS12 
(if your certificate didn`t have password, delete ```-srcstorepass``` ```-deststorepass```)

```Shell
keytool -importkeystore -srckeystore ./jks/client.p12 -srcstoretype PKCS12 -destkeystore ./jks/client.jks -deststoretype JKS -srcstorepass CHANGEIT! -deststorepass CHANGEIT!
```
