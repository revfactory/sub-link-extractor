package io.github.revfactory;

import static org.mockito.Mockito.*;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertFalse;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.io.IOException;
import java.util.List;

public class DefaultLinkExtractorTest {

    @Mock
    private Document mockDocument;

    @BeforeEach
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);

        // HTML Content for Mocking
        String htmlContent = "<!DOCTYPE html><html><head><title>Test Page</title></head><body>"
                + "<a href=\"https://www.example.com/page1\">Link 1</a>"
                + "<a href=\"https://www.example.com/page2\">Link 2</a>"
                + "<a href=\"https://www.example2.com\">External Link</a>"
                + "<a href=\"/page3\">Relative Link</a>"
                + "</body></html>";

        when(mockDocument.select("a[href]")).thenReturn(Jsoup.parse(htmlContent, "https://www.example.com").select("a[href]"));
    }

    @Test
    public void testExtractLinks() throws IOException, InterruptedException {
        DefaultLinkExtractor extractor = new DefaultLinkExtractor(0) {
            @Override
            public Document connectAndParse(String url) throws IOException {
                return mockDocument;
            }
        };

        List<String> links = extractor.extractLinks("https://www.example.com");
        assertTrue(links.contains("https://www.example.com/page1"));
        assertTrue(links.contains("https://www.example.com/page2"));
        assertFalse(links.contains("https://www.example2.com"));  // 외부 링크는 제외
        assertTrue(links.contains("https://www.example.com/page3"));  // 상대 링크가 절대 링크로 변환되어야 함
    }
}
