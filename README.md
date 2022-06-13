# RA-Backend

## Pre-requisites
* JDK 8+
* docker
* Jenkins

**애플리케이션 실행**
```bash
./gradlew assemble docker dockerRun
```

**애플리케이션 중지**
```bash
./gradlew dockerStop
```

**테스트 커버리지 확인**
```bash
./gradlew --console verbose test jacocoTestReport jacocoTestCoverageVerification
```

