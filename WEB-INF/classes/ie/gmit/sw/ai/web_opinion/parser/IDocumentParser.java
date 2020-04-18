package ie.gmit.sw.ai.web_opinion.parser;

import ie.gmit.sw.ai.web_opinion.utils.FrequencyMap;
import ie.gmit.sw.ai.web_opinion.FuzzyLogicCalculator;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

//public interface IDocumentParser {
//    Elements getEdges(Document doc);
//
//    int getHeuristic(String query, Document doc, FuzzyLogicCalculator fuzzy, FrequencyMap map);
//
//    default void index(String title, List<String> headings, String body, FrequencyMap frequencyMap) {
//
//        List<String> strings = new ArrayList<>();
//        strings.add(title);
//        strings.addAll(headings);
//        strings.add(body);
//        // extract each word from the string and add to the map after filtering
//        // with ignore words...
//        try {
//            StringHelper idx = new StringHelper(strings, frequencyMap);
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//    }
//}
