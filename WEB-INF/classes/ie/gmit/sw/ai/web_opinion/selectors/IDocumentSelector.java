package ie.gmit.sw.ai.web_opinion.selectors;

import org.jsoup.nodes.Document;

public interface IDocumentSelector {

    DocumentElements getDocumentElements(Document doc);

}
