package ie.gmit.sw.ai.web_opinion.selectors;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.List;

public class DuckDuckGoDocSelector implements IDocumentSelector {

    @Override
    public DocumentElements getDocumentElements(Document doc) {

        List<DocumentLink> documentLinks = new ArrayList<>();
        // Find the div with Id=links and get all of its children with class=results_links
        Elements results_links = doc.getElementById("links").getElementsByClass("results_links");
        // Each element in results_links represents a result, parse each result accordingly
        for (Element result : results_links) {
            // Get the links_main section from the result - there will be only one
            Element links_main = result.getElementsByClass("links_main").first();

            // Get the title div - only one
            Element result_title = links_main.getElementsByClass("result_title").first();
            // Get the hyperlink - only one
            Element hyperlink = result_title.getElementsByTag("a").first();

            // Hyperlink for the result
            String url = hyperlink.attr("href");
            // Title of the Hyperlink
            String title = hyperlink.text();

            // Also get the snippet
            String snippet = links_main.getElementsByClass("result_snippet").first().text();

            documentLinks.add(new DocumentLink(url, title, snippet));
        }

        List<String> paragraphs = new ArrayList<>();
        List<String> headingOnes = new ArrayList<>();


        return new DocumentElements(doc.title(), documentLinks, paragraphs, headingOnes);
    }
}
