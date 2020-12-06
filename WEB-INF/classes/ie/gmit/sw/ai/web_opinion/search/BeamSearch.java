package ie.gmit.sw.ai.web_opinion.search;

import ie.gmit.sw.ai.web_opinion.models.IScorableDocument;
import ie.gmit.sw.ai.web_opinion.models.ScoredDocument;
import ie.gmit.sw.ai.web_opinion.models.WebSearchType;
import ie.gmit.sw.ai.web_opinion.selectors.IEdge;
import ie.gmit.sw.ai.web_opinion.services.FuzzyDocumentService;
import ie.gmit.sw.ai.web_opinion.services.IFuzzyService;
import ie.gmit.sw.ai.web_opinion.utils.FrequencyMap;
import ie.gmit.sw.ai.web_opinion.utils.IFrequencyMap;
import ie.gmit.sw.ai.web_opinion.utils.IMergeableFrequencyMap;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.IOException;
import java.util.*;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.ConcurrentSkipListSet;

public class BeamSearch implements ISearch<String> {

    private IScorableDocument rootDoc;
    private IFuzzyService fuzzy;
    private WebSearchType _Web_search;

    private ConcurrentLinkedQueue<IScorableDocument> queue;
    private Set<String> closed;

    private IMergeableFrequencyMap<String> frequencyMap = new FrequencyMap();

    int beamWidth = 5;

    public BeamSearch() {
        closed = new ConcurrentSkipListSet<>();
        queue = new ConcurrentLinkedQueue<>();
    }


    @Override
    public void search(IScorableDocument rootDoc, WebSearchType webSearchType) {

        closed.add(rootDoc.getIdentifier());
        queue.add(rootDoc);

        this.fuzzy = new FuzzyDocumentService();
        this.rootDoc = rootDoc;
        this._Web_search = webSearchType;


        process();
    }

    private void process() {

        while (!queue.isEmpty() && closed.size() <= _Web_search.getMaxDepth()) {
            queue.poll();

            List<IEdge> docLinks = rootDoc.getEdges();

            for (IEdge searchResult : docLinks) {

                String linkUrl = searchResult.getIdentifier();
                List<IScorableDocument> children = new ArrayList<>();
                // score each child
                if (closed.size() <= _Web_search.getMaxDepth() && linkUrl != null && !closed.contains(linkUrl)) {
                    closed.add(linkUrl);
                    Document childDoc = null;
                    try {
                        childDoc = Jsoup.connect(linkUrl).get();

                        IScorableDocument scoredChild = new ScoredDocument(childDoc, linkUrl, rootDoc.getQuery());
                        for (IEdge edge : scoredChild.getEdges()) {
                            Document edgeDoc = Jsoup.connect(edge.getIdentifier()).get();
                            IScorableDocument scoredEdge = new ScoredDocument(edgeDoc, edge.getIdentifier(), rootDoc.getQuery());
                            int fuzzyScore = fuzzy.getScore(scoredEdge);
                            if (fuzzyScore >= _Web_search.getThreshold()) {
                                children.add(scoredEdge);
                            }
                            closed.add(edge.getIdentifier());
                        }

                    } catch (IOException ex) {
                    }

                    int bound = 0;
                    if (children.size() < beamWidth) {
                        bound = children.size();
                    } else {
                        bound = beamWidth;
                    }

                    Collections.sort(children, Comparator.comparing(IScorableDocument::getCombinedScore));
                    for (int i = 0; i < bound; i++) {
                        IScorableDocument child = children.get(i);
                        queue.offer(child);
                        this.frequencyMap.merge(child.index());
                    }
                }
            }
            System.out.println(queue.size());
        }
    }

    @Override
    public IFrequencyMap<String> getFrequencyMap() {
        return this.frequencyMap;
    }
}
