# RA-Backend

## Pre-requisites
* JDK 8+
* docker
* docker compose

##  애플리케이션 실행

**몽고 디비 실행**
``` bash
docker-compose up -d
```

**애플리케이션 실행**
```bash
./gradlew assemble docker dockerRun
```

**애플리케이션 중지**
```bash
./gradlew dockerStop
```

## 기능
* [소셜 로그인](md/Social-Login.md)