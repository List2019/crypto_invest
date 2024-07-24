# Crypto invest service

## Assumptions

This application makes the following assumptions:

1. The column order in the CSV file will be as follows: [timestamp, symbol, price].
2. Only one type of crypto can be in one csv file.
3. Some default data are always provided.

## Local run

First of all you need to replace [REPLACE_ME] in application-local.yml

Then run
```  
mvn clean compile
```
and use this IDEA run configuration

```  
--spring.profiles.active=local
```

## Containerization

This command will create JAR file which is using is Dockerfile
```  
mvn clean compile package
```
and after that we are ready to use Dockerfile for containerization

### Perks
[crypto_data_test](crypto_data_test) directory consist file for testing of adding new crypto 
