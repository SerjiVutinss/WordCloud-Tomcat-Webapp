package ie.gmit.sw.ai.web_opinion.parser;

import ie.gmit.sw.ai.web_opinion.FuzzyLogicCalculator;
import ie.gmit.sw.ai.web_opinion.models.Enums;
import ie.gmit.sw.ai.web_opinion.utils.FrequencyMap;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.List;

public class HtmlDocumentParser {

//    private static final int TITLE_WEIGHT = 50;
//    private static final int P_WEIGHT = 1;
//    private static final int H1_WEIGHT = 20;
//
//    public Elements getEdges(Document doc) {
//        return doc.select("a[href]");
//    }
//
//    public int getHeuristic(String query, Document doc, FuzzyLogicCalculator fuzzy, FrequencyMap map) {
//
//        String title = doc.title();
//
//        System.out.println("Parsing: " + title);
//
//        int titleScore = StringHelper.getQueryFrequencyInString(query, title, Enums.ParseType.TITLE) * TITLE_WEIGHT;
//
//        Elements headings_1 = doc.select("h1");
//        int headingsScore = 0;
//        List<String> headings = new ArrayList<>();
//        for (Element h : headings_1) {
//            String h1 = h.text();
//            headings.add(h1);
//            headingsScore += StringHelper.getQueryFrequencyInString(query, h1, Enums.ParseType.H1) * H1_WEIGHT;
//        }
//
//
//        int bodyScore = 0;
//        String body = "";
//        if (doc.body() != null) {
//            body = doc.body().text();
//            bodyScore += StringHelper.getQueryFrequencyInString(query, body, Enums.ParseType.BODY) * P_WEIGHT;
//        }
//
//        int heuristicScore = fuzzy.getFuzzyHeuristic(titleScore, headingsScore, bodyScore);
//
//        return heuristicScore;
//    }
}
