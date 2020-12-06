package ie.gmit.sw.ai.web_opinion.search;

import ie.gmit.sw.ai.web_opinion.models.IScorableDocument;
import ie.gmit.sw.ai.web_opinion.models.WebSearchType;
import ie.gmit.sw.ai.web_opinion.utils.IFrequencyMap;

/**
 * Describes the behaviour each Search Algorithm should  implements.
 * @param <E> The Type to be returned by the search algorithm, usually String will suffice.
 */
public interface ISearch<E> {
    /**
     * @return the search results as a IFrequencyMap of type E
     */
    IFrequencyMap<E> getFrequencyMap();

    /**
     * Begin a search on the IScorable object using the parameters accesible in the WebSearchType object.
     * @param rootDoc
     * @param webSearchType
     */
    void search(IScorableDocument rootDoc, WebSearchType webSearchType);
}
