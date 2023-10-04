package io.github.revfactory;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.*;
import java.util.stream.Collectors;

public class DefaultLinkExtractor implements LinkExtractorStrategy {
    private final int requestDelayMs;

    public DefaultLinkExtractor() {
        this(1000);  // default 1 second delay
    }

    public DefaultLinkExtractor(int requestDelayMs) {
        this.requestDelayMs = requestDelayMs;
    }

    @Override
    public List<String> extractLinks(String sourceUrl) throws IOException, InterruptedException {
        Set<String> visitedLinks = new HashSet<>();
        Queue<String> toVisit = new LinkedList<>();
        Set<String> disallowedPaths = getDisallowedPaths(sourceUrl);

        URL url = new URL(sourceUrl);
        String base = url.getProtocol() + "://" + url.getHost();

        toVisit.add(sourceUrl);

        while (!toVisit.isEmpty()) {
            String currentUrl = toVisit.poll();

            // Remove hashtags from the URL
            String sanitizedUrl = removeHashTag(currentUrl);

            if (visitedLinks.contains(sanitizedUrl) || isDisallowed(new URL(sanitizedUrl).getPath(), disallowedPaths)) {
                continue;
            }

            if (visitedLinks.contains(currentUrl) || isDisallowed(new URL(currentUrl).getPath(), disallowedPaths)) {
                continue;
            }

            try {
                Thread.sleep(requestDelayMs);
                Document document = connectAndParse(currentUrl);
                Elements elements = document.select("a[href]");

                for (var element : elements) {
                    String link = element.attr("abs:href");  // Automatically converts relative paths to absolute paths
                    if (link.startsWith(base) && !visitedLinks.contains(link)) {
                        toVisit.add(link);
                    }
                }

                visitedLinks.add(sanitizedUrl);
            } catch (IOException e) {
                // Handle error or choose to ignore
            }
        }

        return new ArrayList<>(visitedLinks);
    }

    // This method is added to simplify the testing process
    protected Document connectAndParse(String url) throws IOException {
        return Jsoup.connect(url).get();
    }

    private boolean isDisallowed(String path, Set<String> disallowedPaths) {
        return disallowedPaths.stream().anyMatch(path::startsWith);
    }

    private String removeHashTag(String url) {
        int hashIndex = url.indexOf("#");
        return (hashIndex > -1) ? url.substring(0, hashIndex) : url;
    }

    private Set<String> getDisallowedPaths(String sourceUrl) throws IOException {
        Set<String> disallowedPaths = new HashSet<>();
        URL url = new URL(sourceUrl);
        String robotUrl = url.getProtocol() + "://" + url.getHost() + "/robots.txt";
        try {
            List<String> lines = readAllLinesFromUrl(robotUrl);
            boolean isUserAgentAll = false;
            for (String line : lines) {
                if (line.trim().equalsIgnoreCase("User-agent: *")) {
                    isUserAgentAll = true;
                } else if (isUserAgentAll && line.trim().startsWith("Disallow:")) {
                    disallowedPaths.add(line.split(":")[1].trim());
                }
            }
        } catch (Exception ignored) {
            // robots.txt may not exist or there might be other issues accessing it.
        }
        return disallowedPaths;
    }

    private List<String> readAllLinesFromUrl(String targetUrl) throws IOException {
        URL url = new URL(targetUrl);
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(url.openStream()))) {
            return reader.lines().collect(Collectors.toList());
        }
    }
}
