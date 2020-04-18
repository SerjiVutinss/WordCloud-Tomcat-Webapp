package ie.gmit.sw.ai.web_opinion.models;

import ie.gmit.sw.ai.web_opinion.selectors.DocumentElements;
import ie.gmit.sw.ai.web_opinion.selectors.DocumentLink;
import ie.gmit.sw.ai.web_opinion.selectors.IDocumentSelector;
import ie.gmit.sw.ai.web_opinion.services.IgnoredWordsService;
import ie.gmit.sw.ai.web_opinion.utils.FrequencyMap;
import org.jsoup.nodes.Document;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ScoredDocument {

    private static final int TITLE_WEIGHT = 5; // 50
    private static final int H1_WEIGHT = 2; // 20
    private static final int P_WEIGHT = 1; // 1

    private final Document document;

    private final String query;
    private final DocumentElements docElements;
    private IDocumentSelector documentSelector;
    private String _url;

    private int titleScore = 0;

    private int headingScore = 0;
    private int bodyScore = 0;

    public ScoredDocument(Document document, String url, String query, IDocumentSelector documentSelector) {
        this.document = document;
        this._url = url;
        this.query = query;
        this.docElements = documentSelector.getDocumentElements(this.document);

        this.calculateScores();
        System.out.println("SCORE: " + this.getScore());
    }

    public Document getDocument() {
        return document;
    }

    public String getUrl() {
        return _url;
    }

    public String getQuery() {
        return query;
    }

    public int getTitleScore() {
        return titleScore;
    }

    public int getHeadingScore() {
        return headingScore;
    }

    public int getBodyScore() {
        return bodyScore;
    }

    public int getScore() {
        return this.titleScore + this.headingScore + this.bodyScore;
    }

    public List<DocumentLink> getEdges() {

        return this.docElements.getLinks();
    }

    @Override
    public String toString() {
        return String.format("Title: %d, Headings: %d, Body: %d", titleScore, headingScore, bodyScore);
    }

    private void calculateScores() {

        System.out.println("Calculating Score for: " + this.docElements.getTitle());

        // Score the title
        this.titleScore = this.getQueryFrequencyInString(query, docElements.getTitle()) * TITLE_WEIGHT;
        // Score all h1s
        for (String h : docElements.getHeadingOnes()) {
            this.headingScore += this.getQueryFrequencyInString(query, h) * H1_WEIGHT;
        }

        // Score all paragraphs
        for (String p : docElements.getParagraphs()) {
            this.bodyScore += this.getQueryFrequencyInString(query, p) * H1_WEIGHT;
        }

    }

    private int getQueryFrequencyInString(String query, String s) {

        int count = 0, startIndex = 0;

        Pattern p = Pattern.compile(query, Pattern.CASE_INSENSITIVE);
        Matcher m = p.matcher(s);

        while (m.find(startIndex)) {
            count++;
            startIndex = m.start() + 1;
        }
        return count;
    }

    public FrequencyMap index() throws IOException {

        // get ALL words in the title, headings and body
        FrequencyMap results = new FrequencyMap();

        List<String> allStrings = new ArrayList<>();
        allStrings.add(this.docElements.getTitle());
        allStrings.addAll(this.docElements.getHeadingOnes());
        allStrings.addAll(this.docElements.getParagraphs());

        Set<String> ignoredWords = IgnoredWordsService.getIgnoredWords();

        for (String s : allStrings) {
            for (String word : splitIntoWords(s)) {
                if (!word.equals(query.toLowerCase()) && !ignoredWords.contains(word) && word.length() > 1)
                    results.put(word);
            }
        }
        return results;
    }

    private List<String> splitIntoWords(String s) {

        List<String> words = new ArrayList<>();

        // Match only words - should update to ignore numerics too...
        String regex = "\\w+";

        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(s);

        String str;
        while (matcher.find()) {
            str = matcher.group().toLowerCase();
            // Remove any completely numeric strings
            try {
                Integer.parseInt(str);
            } catch (NumberFormatException e) {
                words.add(str);
            }
        }
        return words;

    }

}
