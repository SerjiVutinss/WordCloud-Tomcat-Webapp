package ie.gmit.sw.ai.web_opinion;

import ie.gmit.sw.ai.cloud.WordFrequency;
import net.sourceforge.jFuzzyLogic.FIS;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentSkipListSet;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class NodeParserExample {

    private static final int MAX_LINKS = 100;
    private static final int MAX_WORDS = 20;
    private static final int TITLE_WEIGHT = 50;
    private static final int H1_WEIGHT = 20;
    private static final int P_WEIGHT = 1;

    private String url;
    private String query;

    private final int THRESHOLD = 15;

    private Set<String> closed;
    private Queue<ParsedNode> queue;

    private Map<String, Integer> resultMap = new ConcurrentHashMap<>();
    private FrequencyMap frequencyMap = new FrequencyMap();

    List<WordFrequency> wordFrequencies = new ArrayList<>();

    public NodeParserExample() {


        closed = new ConcurrentSkipListSet<>();
        queue = new PriorityQueue<>(Comparator.comparing(ParsedNode::getScore));
    }

    public void run(String url, String query) throws IOException {

        this.url = url;
        this.query = query;

        Document doc = Jsoup.connect(url).get();
        int score = getHeuristicScore(doc);
        closed.add(url);
        queue.offer(new ParsedNode(doc, score));

        process();
    }

    public WordFrequency[] getResults() {
//
//        WordFrequency[] test = new WordFrequency[3];
//        test[0] = new WordFrequency("Hello", 300);
//        test[1] = new WordFrequency("World", 200);
//        test[2] = new WordFrequency("Hi", 100);
//
//
//        return  test;
        return wordFrequencies.toArray(new WordFrequency[wordFrequencies.size()]);
    }

    private void process() {
        while (!queue.isEmpty() && closed.size() <= MAX_LINKS) {

            ParsedNode node = queue.poll();
            Document doc = node.getDoc();

            Elements edges = doc.select("a[href]"); // a with href
            for (Element e : edges) {
                String link = e.absUrl("href");

                if (closed.size() <= MAX_WORDS && link != null && !closed.contains(link)) {

                    closed.add(link);

                    Document child = null;
                    try {

                        child = Jsoup.connect(link).get();
                        int score = getHeuristicScore(child);
                        queue.offer(new ParsedNode(child, score));

                    } catch (IOException ex) {
//                        ex.printStackTrace();
                    }
                }
            }

        }

        System.out.println("Done PARSING");

        List<WordFrequency> words = new ArrayList<>();
        for (Map.Entry<String, Integer> e : frequencyMap.entrySet()) {
            words.add(new WordFrequency(e.getKey(), e.getValue()));
        }

        this.wordFrequencies = new ArrayList<>();
        words.sort(Comparator.comparing(WordFrequency::getFrequency).reversed());
        WordFrequency w;
        for (int i = 0; i < MAX_WORDS; i++) {
            w = words.get(i);
//            System.out.println(w.getWord() + ": " + w.getFrequency());
            this.wordFrequencies.add(w);
        }
    }

    private int getHeuristicScore(Document doc) {

        String title = doc.title();
        System.out.println(closed.size() + "-->" + title);
        int titleScore = getFrequency(title, ParseType.TITLE) * TITLE_WEIGHT;

        Elements headings_1 = doc.select("h1");
        int headingsScore = 0;
        List<String> headings = new ArrayList<>();
        for (Element h : headings_1) {
            String h1 = h.text();
            headings.add(h1);
//            System.out.println("\tH1 -->" + h1);
            headingsScore += getFrequency(h1, ParseType.H1) * H1_WEIGHT;
        }


        int bodyScore = 0;
        String body = "";
        if (doc.body() != null) {
            body = doc.body().text();
//            System.out.println("\tBody -->" + body);
            bodyScore += getFrequency(body, ParseType.BODY) * P_WEIGHT;
        }

        int heuristicScore = getFuzzyHeuristic(titleScore, headingsScore, bodyScore);

        if (heuristicScore >= THRESHOLD) {
            index(title, headings, body);
        }

//        System.out.println("Heuristic Score: " + heuristicScore);
        return heuristicScore;
    }

    public enum ParseType {
        TITLE,
        H1,
        BODY
    }

    private int getFrequency(String s, ParseType t) {
        // Check for searchTerm in s

        int count = 0, startIndex = 0;

        Pattern p = Pattern.compile(query, Pattern.CASE_INSENSITIVE);
        Matcher m = p.matcher(s);

        while (m.find(startIndex)) {
            count++;
            startIndex = m.start() + 1;
        }

//        System.out.println("\t" + t + ": occurrences: " + count);
        return count;
    }

    private int getFuzzyHeuristic(int titleScore, int headingsScore, int bodyScore) {
        //
        // Load from 'FCL' file
//        String fileName = "./res/my-fcl.fcl";
        String fileName = Config.FCL_FILE_LOCATION;
        FIS fis = FIS.load(fileName, true);

        // Error while loading?
        if (fis == null) {
            System.err.println("Can't load file: '" + fileName + "'");
            return 0;
        }

        // Set inputs
        fis.setVariable("title", titleScore);
        fis.setVariable("headings", headingsScore);
        fis.setVariable("body", bodyScore);

        // if TITLE is significant and headings is relevant and
        // body is frequent then score is high

        // Evaluate
        fis.evaluate();

        // if fuzzy score is high, call index on the title, headings and body

        int relevancy = (int) fis.getVariable("relevancy").getLatestDefuzzifiedValue();

        // Print ruleSet
//        System.out.println(fis);

        return relevancy;
    }

    private void index(String title, List<String> headings, String body) {

        List<String> strings = new ArrayList<>();
        strings.add(title);
        strings.addAll(headings);
        strings.add(body);
        // extract each word from the string and add to the map after filtering
        // with ignore words...
        try {
            WordSplitter idx = new WordSplitter(strings, frequencyMap);
        } catch (IOException e) {
            e.printStackTrace();
        }


    }

//    private class DocumentNode {
//
//        private Document doc;
//        private int score;
//
//        public DocumentNode(Document doc, int score) {
//            this.doc = doc;
//            this.score = score;
//        }
//
//        public Document getDoc() {
//            return doc;
//        }
//
//        public int getScore() {
//            return score;
//        }
//    }
}

