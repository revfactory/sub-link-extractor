# sub-link-extractor

`LinkExtractor`는 주어진 웹페이지에서 모든 링크를 추출하는 Java 라이브러리입니다. 이 라이브러리는 JSoup를 사용하여 HTML을 파싱합니다.

## 설치 방법

Gradle을 사용하는 경우, `build.gradle` 파일에 다음을 추가하세요:

```gradle
implementation 'com.mylib:linkextractor:1.0.0'
```

Maven을 사용하는 경우, pom.xml 파일에 다음을 추가하세요:
```xml
<dependency>
    <groupId>com.mylib</groupId>
    <artifactId>linkextractor</artifactId>
    <version>1.0.0</version>
</dependency>
```

## 사용 방법

```java
import com.mylib.LinkExtractor;

// ...

List<String> links = LinkExtractor.extractLinks("http://example.com");
```

## 예외 처리
extractLinks 메소드는 IOException을 던질 수 있습니다. 적절한 예외 처리를 통해 네트워크 에러나 기타 문제를 핸들링하세요.

## 라이선스
이 프로젝트는 [Apache 2.0](LICENSE) 라이선스 하에 배포됩니다.
