package ie.gmit.sw.ai.web_opinion.models;

import ie.gmit.sw.ai.web_opinion.selectors.DefaultDocumentSelector;
import ie.gmit.sw.ai.web_opinion.selectors.DocumentElements;
import ie.gmit.sw.ai.web_opinion.selectors.IDocumentSelector;
import ie.gmit.sw.ai.web_opinion.selectors.IEdge;
import ie.gmit.sw.ai.web_opinion.services.IgnoredWordsService;
import ie.gmit.sw.ai.web_opinion.utils.FrequencyMap;
import ie.gmit.sw.ai.web_opinion.utils.IMergeableFrequencyMap;
import ie.gmit.sw.ai.web_opinion.utils.StringUtils;
import org.jsoup.nodes.Document;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * Implementation of IScorableDocument.
 *
 * Creates a scored document via its constructor parameters.  Scores the document based on the document, the query and
 * an instance of IDocumentSelector which allows for scoring documents from different sources appropriately.
 */
public class ScoredDocument implements IScorableDocument {

    private static final int TITLE_WEIGHT = 5; // 50
    private static final int H1_WEIGHT = 2; // 20
    private static final int P_WEIGHT = 1; // 1

    private final Document document;

    private final String query;
    private final DocumentElements docElements;
    private String _url;

    private int titleScore = 0;

    private int headingScore = 0;
    private int bodyScore = 0;

    /**
     * Constructor allows specific IDocumentSelector instance to be supplied.
     * @param document document to be scored.
     * @param identifier identifier for the document, e.g. URL if HTML document.
     * @param query search term to score the document against.
     * @param documentSelector an instance of IDocumentSelector which determines how the document elements are scored.
     */
    public ScoredDocument(Document document, String identifier, String query, IDocumentSelector documentSelector) {
        this.document = document;
        this._url = identifier;
        this.query = query;
        this.docElements = documentSelector.getDocumentElements(this.document);

        // Score the title
        this.titleScore = StringUtils.getQueryFrequencyInString(query, docElements.getTitle()) * TITLE_WEIGHT;
        // Score all h1s
        for (String h : docElements.getHeadingOnes()) {
            this.headingScore += StringUtils.getQueryFrequencyInString(query, h) * H1_WEIGHT;
        }

        // Score all paragraphs
        for (String p : docElements.getParagraphs()) {
            this.bodyScore += StringUtils.getQueryFrequencyInString(query, p) * P_WEIGHT;
        }
    }

    /**
     * Constructor uses DefaultDocumentSelector instance of IDocumentSelector.
     * @param document document to be scored.
     * @param identifier identifier for the document, e.g. URL if HTML document.
     * @param query search term to score the document against.
     */
    public ScoredDocument(Document document, String identifier, String query) {
        this(document, identifier, query, new DefaultDocumentSelector());
    }

    //<editor-fold title="Implementation of IScoreableDocument">
    @Override
    public String getQuery() {
        return query;
    }

    @Override
    public String getIdentifier() {
        return _url;
    }

    @Override
    public int getTitleScore() {
        return titleScore;
    }

    @Override
    public int getHeadingScore() {
        return headingScore;
    }

    @Override
    public int getBodyScore() {
        return bodyScore;
    }

    @Override
    public int getCombinedScore() {
        return this.titleScore + this.headingScore + this.bodyScore;
    }

    @Override
    public List<IEdge> getEdges() {

        return this.docElements.getLinks();
    }

    @Override
    public String toString() {
        return String.format("Title: %d, Headings: %d, Body: %d", titleScore, headingScore, bodyScore);
    }
    //</editor-fold>

    //<editor-fold title="Implementation of Indexable">
    @Override
    public IMergeableFrequencyMap<String> index() {

        // get ALL words in the title, headings and body
        IMergeableFrequencyMap<String> results = new FrequencyMap();

        List<String> allStrings = new ArrayList<>();
        allStrings.add(this.docElements.getTitle());
        allStrings.addAll(this.docElements.getHeadingOnes());
        allStrings.addAll(this.docElements.getParagraphs());

        Set<String> ignoredWords = IgnoredWordsService.getIgnoredWords();

        for (String s : allStrings) {
            for (String word : StringUtils.splitIntoWords(s)) {
                if (!word.equals(query.toLowerCase()) && !ignoredWords.contains(word) && word.length() > 1)
                    results.put(word);
            }
        }
        return results;
    }
    //</editor-fold>
}
