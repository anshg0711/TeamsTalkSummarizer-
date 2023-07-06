package com.teamsapi.service;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.springframework.stereotype.Service;

@Service
public class HtmlToTextConverter {
    public static String convertHtmlToText(String html) {
        Document doc = Jsoup.parse(html);
        return doc.text();
    }
}

