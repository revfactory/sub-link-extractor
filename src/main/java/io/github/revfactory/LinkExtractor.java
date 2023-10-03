package io.github.revfactory;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class LinkExtractor {
    public static List<String> extractLinks(String sourceUrl) throws IOException {
        List<String> links = new ArrayList<>();

        URL url = new URL(sourceUrl);
        String base = url.getProtocol() + "://" + url.getHost();
        String path = url.getPath();

        Document document = Jsoup.connect(sourceUrl).get();
        Elements elements = document.select("a[href]");

        for (var element : elements) {
            String link = element.attr("abs:href");

            if (link.startsWith(base + path)) {
                links.add(link);
            }
        }
        return links;
    }
}
