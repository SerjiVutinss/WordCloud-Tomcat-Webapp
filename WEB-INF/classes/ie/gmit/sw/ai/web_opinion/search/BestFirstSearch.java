package ie.gmit.sw.ai.web_opinion.search;

import ie.gmit.sw.ai.web_opinion.models.IScorableDocument;
import ie.gmit.sw.ai.web_opinion.models.ScoredDocument;
import ie.gmit.sw.ai.web_opinion.models.WebSearchType;
import ie.gmit.sw.ai.web_opinion.selectors.IEdge;
import ie.gmit.sw.ai.web_opinion.services.FuzzyDocumentService;
import ie.gmit.sw.ai.web_opinion.services.IFuzzyService;
import ie.gmit.sw.ai.web_opinion.utils.FrequencyMap;
import ie.gmit.sw.ai.web_opinion.utils.IMergeableFrequencyMap;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.IOException;
import java.util.*;
import java.util.concurrent.ConcurrentSkipListSet;

public class BestFirstSearch implements ISearch<String> {

    private final String TAG = "[BFS SERVICE] ";

    private IScorableDocument rootDoc;
    private IFuzzyService fuzzy;
    private WebSearchType _Web_search;

    private Set<String> closed;
    private Queue<IScorableDocument> queue;

    private IMergeableFrequencyMap<String> frequencyMap = new FrequencyMap();

    @Override
    public IMergeableFrequencyMap<String> getFrequencyMap() {
        return frequencyMap;
    }

    public BestFirstSearch() {
        closed = new ConcurrentSkipListSet<>();
        queue = new PriorityQueue<>(Comparator.comparing(IScorableDocument::getCombinedScore));
    }

    @Override
    public void search(IScorableDocument rootDoc, WebSearchType webSearchType) {

        closed.add(rootDoc.getIdentifier());
        queue.offer(rootDoc);

        this.fuzzy = new FuzzyDocumentService();
        this.rootDoc = rootDoc;
        this._Web_search = webSearchType;


        process();
    }

    private void process() {
        while (!queue.isEmpty() && closed.size() <= _Web_search.getMaxDepth()) {

            IScorableDocument node = queue.poll();
            List<IEdge> docLinks = node.getEdges();

            for (IEdge e : docLinks) {

                String linkUrl = e.getIdentifier();
                if (closed.size() <= _Web_search.getMaxDepth() && linkUrl != null && !closed.contains(linkUrl)) {
                    closed.add(linkUrl);
                    Document childDoc = null;
                    try {
                        childDoc = Jsoup.connect(linkUrl).get();

                        IScorableDocument scoredChild = new ScoredDocument(childDoc, linkUrl, rootDoc.getQuery());
                        int fuzzyScore = fuzzy.getScore(scoredChild);

                        if (fuzzyScore >= _Web_search.getThreshold()) {
                            // index and add this doc's words and frequencies to the map
                            this.frequencyMap.merge(scoredChild.index());
                        }

                        queue.offer(scoredChild);
                    } catch (IOException ex) {
                    }
                }
            }
        }
    }

}
