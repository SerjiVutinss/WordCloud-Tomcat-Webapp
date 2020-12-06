package ie.gmit.sw.ai.web_opinion.services;

import ie.gmit.sw.ai.cloud.WordFrequency;
import ie.gmit.sw.ai.web_opinion.models.ScoredDocument;
import ie.gmit.sw.ai.web_opinion.models.SearchQuery;
import ie.gmit.sw.ai.web_opinion.search.BeamSearch;
import ie.gmit.sw.ai.web_opinion.search.BestFirstSearch;
import ie.gmit.sw.ai.web_opinion.search.ISearch;
import ie.gmit.sw.ai.web_opinion.utils.FrequencyMap;
import ie.gmit.sw.ai.web_opinion.utils.IFrequencyMap;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

/**
 * Processes a SearchQuery object
 */
public class QueryRunner {

    private SearchQuery _query; // query to be processed
    private IFrequencyMap<String> frequencyMap; // all processed results are placed in this map
    private WordFrequency[] results; // the final result array, sorted and trimmed to size

    private final String TAG = "[QUERY RUNNER] ";

    public QueryRunner(SearchQuery searchQuery) {
        this._query = searchQuery;
        this.frequencyMap = new FrequencyMap<>();
        this.run();
    }

    public void run() {

        Document rootDocument = null;
        try {
            rootDocument = Jsoup.connect(_query.getUrl()).get();

            // Since this is a 'root' document (i.e. search engine results), be sure to pass in the search model's document selector
            ScoredDocument scoredRootDocument = new ScoredDocument(rootDocument, _query.getUrl(), _query.getQuery(), _query.getSearchModel().getDocumentSelector());

            // Create a BFS service and submit the document and search model from the query

            ISearch searchService;
            switch (_query.getSearchModel().getHeuristicSearchType()) {
                case BEAM:
                    searchService = new BeamSearch();
                    break;
                default:
                    searchService = new BestFirstSearch();
                    break;
            }

            searchService.search(scoredRootDocument, _query.getSearchModel());

            this.frequencyMap = searchService.getFrequencyMap();
            this.results = sortAndTrim(_query.getMaxResults());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private WordFrequency[] sortAndTrim(int limit) {
        List<WordFrequency> words = new ArrayList<>();
        for (Map.Entry<String, Integer> e : frequencyMap.entrySet()) {
            words.add(new WordFrequency(e.getKey(), e.getValue()));
        }
        words.sort(Comparator.comparing(WordFrequency::getFrequency).reversed());
        WordFrequency w;
        int max = words.size() < limit ? words.size() : limit;
        WordFrequency[] wordFrequencies = new WordFrequency[max];
        for (int i = 0; i < max; i++) {
            w = words.get(i);
            wordFrequencies[i] = w;
        }
        return wordFrequencies;
    }

    public WordFrequency[] getResults() {
        return results;
    }
}
