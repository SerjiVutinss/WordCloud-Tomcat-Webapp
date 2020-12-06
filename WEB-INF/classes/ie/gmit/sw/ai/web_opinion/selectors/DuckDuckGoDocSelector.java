package ie.gmit.sw.ai.web_opinion.selectors;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.List;

/**
 * Duck Duck Go document selector used to parse search results.
 */
public class DuckDuckGoDocSelector implements IDocumentSelector {

    @Override
    public DocumentElements getDocumentElements(Document doc) {

        List<DocumentLink> documentLinks = new ArrayList<>();

        // All search results are contained within a <div id="links">
        //  Each search result is contained within a <div class="results_links">
        Elements results_links = doc.getElementById("links").getElementsByClass("results_links");

        // Parse each search result
        for (Element result_link : results_links) documentLinks.add(parseResult(result_link));

        // This is a search result page, so there are no paras and headings
        List<String> paragraphs = new ArrayList<>();
        List<String> headingOnes = new ArrayList<>();

        return new DocumentElements(doc.title(), documentLinks, paragraphs, headingOnes);
    }

    private DocumentLink parseResult(Element result_link) {

        // The first hyperlink within the result is the URL
        Element link = result_link.getElementsByClass("links_main").first().getElementsByTag("a").first();
        String url = link.attr("href");
        // Parse title and snippet
        String title = link.text();
        String snippet = result_link.getElementsByClass("result__snippet").first().text();

        return new DocumentLink(url, title, snippet);
    }
}
