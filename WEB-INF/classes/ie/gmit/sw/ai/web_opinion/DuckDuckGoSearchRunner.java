package ie.gmit.sw.ai.web_opinion;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class DuckDuckGoSearchRunner {

    private final static String DUCKDUCKGO_SEARCH_QUERY_URL = "https://duckduckgo.com/html/?q=";

    public static void getSearchResults(String query) {
        Document doc = null;

        Map<String, String> paramMap = new HashMap<>();

        try {
            doc = Jsoup.connect(DUCKDUCKGO_SEARCH_QUERY_URL + query).get();
            Elements results = doc.getElementById("links").getElementsByClass("results_links");

            for (Element result : results) {

                Element title = result.getElementsByClass("links_main").first().getElementsByTag("a").first();

                System.out.println("\nURL:" + title.attr("href"));
                System.out.println("Title:" + title.text());
                System.out.println("Snippet:" + result.getElementsByClass("result__snippet").first().text());
            }

            Element navLink = doc.getElementsByClass("nav-link").first();

            Elements params = navLink.getElementsByTag("form").first().getElementsByTag("input");
            for (Element p : params) {
                String name = p.attr("name");
                if ((name != null && name.length() > 0)) {
                    String value = p.attr("value");
                    if (value != null && value.length() > 0) {
                        paramMap.put(name, value);
                    }
                }
            }

            StringBuilder sb = new StringBuilder("https://duckduckgo.com/html/?");
            for (Map.Entry k : paramMap.entrySet()) {
                sb.append(k.getKey() + "=" + k.getValue() + "&");
            }

            System.out.println(sb.toString());

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
