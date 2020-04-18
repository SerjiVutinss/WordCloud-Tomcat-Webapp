package ie.gmit.sw.ai.web_opinion.parser;

import org.jsoup.nodes.Document;

public class ParsedNode {

    private Document doc;
    private int score;

    public ParsedNode(Document doc, int score) {
        this.doc = doc;
        this.score = score;
    }

    public Document getDoc() {
        return doc;
    }

    public int getScore() {
        return score;
    }
}
