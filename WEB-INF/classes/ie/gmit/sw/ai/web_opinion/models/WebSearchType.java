package ie.gmit.sw.ai.web_opinion.models;

import ie.gmit.sw.ai.web_opinion.selectors.DefaultDocumentSelector;
import ie.gmit.sw.ai.web_opinion.selectors.DuckDuckGoDocSelector;
import ie.gmit.sw.ai.web_opinion.selectors.IDocumentSelector;

/**
 * Bean class defining a WebSearch which is returned by the SearchTypeService
 */
public class WebSearchType {

    private Enums.WebSearchType _Web_searchType;
    private String _baseUrl;
    private int _maxResults;

    public Enums.HeuristicSearchType getHeuristicSearchType() {
        return heuristicSearchType;
    }

    public void setHeuristicSearchType(Enums.HeuristicSearchType heuristicSearchType) {
        this.heuristicSearchType = heuristicSearchType;
    }

    private Enums.HeuristicSearchType heuristicSearchType;

    public void set_maxResults(int _maxResults) {
        System.out.println("WST: " + _maxResults);
        this._maxResults = _maxResults;
    }

    public void set_maxDepth(int _maxDepth) {
        this._maxDepth = _maxDepth;
    }

    public void set_threshold(int _threshold) {
        this._threshold = _threshold;
    }

    public void set_beamWidth(int _beamWidth) {
        this._beamWidth = _beamWidth;
    }

    private int _maxDepth = 50;
    private int _threshold = 4;
    private int _beamWidth = 4;
    private IDocumentSelector _documentSelector;

    public WebSearchType(Enums.WebSearchType webSearchType, String baseUrl, int maxResults, int maxDepth, int threshold, int beamWidth) {
        this._Web_searchType = webSearchType;
        this._baseUrl = baseUrl;
        this._documentSelector = getSearchTypeDocSelector(webSearchType);
        this._maxResults = maxResults;
        this._maxDepth = maxDepth;
        this._threshold = threshold;
        this._beamWidth = beamWidth;
    }

    public WebSearchType(Enums.WebSearchType webSearchType, String baseUrl) {
        this._Web_searchType = webSearchType;
        this._baseUrl = baseUrl;
        this._documentSelector = getSearchTypeDocSelector(webSearchType);
    }

    public Enums.WebSearchType getSearchType() {
        return _Web_searchType;
    }

    public String getBaseUrl() {
        return _baseUrl;
    }

    public int getMaxResults() {
        return _maxResults;
    }

    public int getMaxDepth() {
        return _maxDepth;
    }

    public int getThreshold() {
        return _threshold;
    }

    public IDocumentSelector getDocumentSelector() {
        return _documentSelector;
    }

    private IDocumentSelector getSearchTypeDocSelector(Enums.WebSearchType webSearchType) {
        switch (webSearchType) {
            case DUCK_DUCK_GO:
                return new DuckDuckGoDocSelector();
            case WIKIPEDIA:
                return new DefaultDocumentSelector();
            default:
                return null;
        }
    }
}
