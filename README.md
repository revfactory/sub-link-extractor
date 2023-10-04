# sub-link-extractor

The `DefaultLinkExtractor` is a Java library designed to extract all the links from a given webpage. It utilizes JSoup to parse the HTML content.

## Installation

For those using Gradle, add the following to your `build.gradle` file:

```gradle
implementation 'io.github.revfactory:sub-link-extractor:0.1.1'
```

For Maven users, add the following to your `pom.xml`:
```xml
<dependency>
    <groupId>io.github.revfactory</groupId>
    <artifactId>sub-link-extractor</artifactId>
    <version>0.1.1</version>
</dependency>
```

## Usage

```java
import com.mylib.DefaultLinkExtractor;

// ...

LinkExtractorStrategy extractor = new DefaultLinkExtractor(1500);  // 1.5 second delay
List<String> links = extractor.extractLinks("http://example.com");
```
Remember, the library can also be customized using different strategies by implementing the `LinkExtractorStrategy` interface.


## Error Handling
The `extractLinks` method can throw an `IOException`. Ensure you have proper error handling to manage any network issues or other related problems.

## Licensing
This project is distributed under the [Apache 2.0](LICENSE) license.
