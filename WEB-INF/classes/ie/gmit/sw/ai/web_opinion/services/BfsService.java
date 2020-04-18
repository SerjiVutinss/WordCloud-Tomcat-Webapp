package ie.gmit.sw.ai.web_opinion.services;

import ie.gmit.sw.ai.web_opinion.models.ScoredDocument;
import ie.gmit.sw.ai.web_opinion.models.SearchModel;
import ie.gmit.sw.ai.web_opinion.selectors.GenericDocumentSelector;
import ie.gmit.sw.ai.web_opinion.selectors.DocumentLink;
import ie.gmit.sw.ai.web_opinion.utils.FrequencyMap;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.IOException;
import java.util.*;
import java.util.concurrent.ConcurrentSkipListSet;

public class BfsService {

    private static final int THRESHOLD = 10;

    private final ScoredDocument rootDoc;
    private final FuzzyService fuzzy;
    private final SearchModel _searchModel;

    private Set<String> closed;
    private Queue<ScoredDocument> queue;

//    private int _maxNodes;

    private final String TAG = "[BFS SERVICE] ";

    public FrequencyMap getFrequencyMap() {
        return frequencyMap;
    }

    private FrequencyMap frequencyMap = new FrequencyMap();

    public BfsService(ScoredDocument rootDoc, SearchModel searchModel) {
        this.fuzzy = new FuzzyService();
        this.rootDoc = rootDoc;
        this._searchModel = searchModel;

        closed = new ConcurrentSkipListSet<>();
        queue = new PriorityQueue<>(Comparator.comparing(ScoredDocument::getScore));
    }

    public void run() {

        closed.add(rootDoc.getUrl());
        queue.offer(rootDoc);

        process();
    }

    private void process() {
        while (!queue.isEmpty() && closed.size() <= _searchModel.getMaxDepth()) {

            ScoredDocument node = queue.poll();

            List<DocumentLink> docLinks = node.getEdges(); // a with href

            System.out.println(TAG + "Got: " + docLinks.size() + " docLinks");

            for (DocumentLink e : docLinks) {
                        System.out.println(e.getTitle() + " " + e.getUrl());

                String linkUrl = e.getUrl();

                if (closed.size() <= _searchModel.getMaxDepth() && linkUrl != null && !closed.contains(linkUrl)) {
                    closed.add(linkUrl);
                    Document childDoc = null;
                    try {
                        childDoc = Jsoup.connect(linkUrl).get();
                        // Since this is a child doc (i.e. could be from any site)
                        ScoredDocument scoredChild = new ScoredDocument(childDoc, linkUrl, rootDoc.getQuery(), new GenericDocumentSelector());
                        int fuzzyScore = fuzzy.getFuzzyHeuristic(scoredChild);

                        if (fuzzyScore > _searchModel.getThreshold()) {
                            // add this doc's words and frequencies to the map
                            frequencyMap.merge(scoredChild.index());
                        }

                        queue.offer(scoredChild);
                    } catch (IOException ex) {
                    }
                }
            }
        }
    }

}
