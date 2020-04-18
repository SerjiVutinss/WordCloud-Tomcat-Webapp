package ie.gmit.sw.ai.web_opinion.selectors;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.List;

public class GenericDocumentSelector implements IDocumentSelector {
    @Override
    public DocumentElements getDocumentElements(Document doc) {

        List<DocumentLink> documentLinks = new ArrayList<>();

        Elements linkElements = doc.select("a");
        for (Element e : linkElements) {
            String url = e.attr("href");
            if (url.startsWith("http")) {
                documentLinks.add(new DocumentLink(url, e.text(), ""));
            }
        }

        List<String> paragraphs = new ArrayList<>();
        Elements bodyEls = doc.body().select("p");
        for (Element e : bodyEls) {
            paragraphs.add(e.text());
        }

        List<String> headingOnes = new ArrayList<>();
        Elements headingOneEls = doc.body().select("h1");
        for (Element e : headingOneEls) {
            headingOnes.add(e.text());
        }

        return new DocumentElements(doc.title(), documentLinks, paragraphs, headingOnes);
    }
}
