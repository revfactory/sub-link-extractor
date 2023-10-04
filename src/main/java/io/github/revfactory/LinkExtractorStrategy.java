package io.github.revfactory;

import java.io.IOException;
import java.util.List;

public interface LinkExtractorStrategy {
    List<String> extractLinks(String sourceUrl) throws IOException, InterruptedException;
}
