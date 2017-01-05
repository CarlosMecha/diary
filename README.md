# My Diary

## Requirements
- Java 1.8
- Postgres 9.5

## Run

```bash
java -jar diary-<version>-SNAPSHOT.jar --server.ssl.key-store-password=mypass > `date +%Y-%m-%d-%H-%M`.log
```
### Self-signed certificate
 
Make sure that the certificate keystore is located in the same folder than the jar file.

```bash
 keytool -genkey -alias cloud -storetype PKCS12 -keyalg RSA -keysize 2048 -keystore keystore.p12 -validity 3650
```

### Docker database
 
```bash
docker build --rm -t carlosmecha/diary-devdb:latest .
docker run --name diary-database -p '5432:5432' -e POSTGRES_PASSWORD=mypass -e POSTGRES_USER=diary -e POSTGRES_DB=diary carlosmecha/diary-devdb
```


## Backups

### Cron
 
```cron
0 10 * * * /bin/bash backup.sh
```
