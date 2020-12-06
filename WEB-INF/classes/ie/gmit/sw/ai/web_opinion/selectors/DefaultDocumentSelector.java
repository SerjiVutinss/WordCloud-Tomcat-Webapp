package ie.gmit.sw.ai.web_opinion.selectors;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.List;

/**
 * Default document selector used to parse generic web pages
 */
public class DefaultDocumentSelector implements IDocumentSelector {
    @Override
    public DocumentElements getDocumentElements(Document doc) {

        List<DocumentLink> documentLinks = new ArrayList<>();

        try {
            Elements linkElements = doc.select("a");
            for (Element e : linkElements) {
                String url = e.attr("href");
                if (url.startsWith("http")) {
                    documentLinks.add(new DocumentLink(url, e.text(), ""));
                }
            }
        } catch (NullPointerException e) {

        }

        List<String> paragraphs = new ArrayList<>();
        try {
            Elements bodyEls = doc.body().select("p");
            for (Element e : bodyEls) {
                paragraphs.add(e.text());
            }
        } catch (NullPointerException e) {

        }

        List<String> headingOnes = new ArrayList<>();
        try {
            Elements headingOneEls = doc.body().select("h1");
            for (Element e : headingOneEls) {
                headingOnes.add(e.text());
            }
        } catch (NullPointerException e) {

        }


        return new DocumentElements(doc.title(), documentLinks, paragraphs, headingOnes);
    }
}
