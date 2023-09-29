package io.github.revfactory;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class LinkExtractor {
    public static List<String> extractLinks(String url) throws IOException {
        List<String> links = new ArrayList<>();
        Document document = Jsoup.connect(url).get();
        Elements elements = document.select("a[href]");

        for (var element : elements) {
            String link = element.attr("abs:href");
            if (!link.isBlank()) {
                links.add(link);
            }
        }
        return links;
    }
}
