package ie.gmit.sw.ai.web_opinion.models;

import ie.gmit.sw.ai.web_opinion.selectors.GenericDocumentSelector;
import ie.gmit.sw.ai.web_opinion.selectors.DuckDuckGoDocSelector;
import ie.gmit.sw.ai.web_opinion.selectors.IDocumentSelector;

public class SearchModel {

    private Enums.SearchType _searchType;
    private String _baseUrl;
    private int _maxResults = 32;
    private int _maxDepth = 50;
    private int _threshold = 50;
    private IDocumentSelector _documentSelector;

    public SearchModel(Enums.SearchType searchType, String baseUrl) {
        this._searchType = searchType;
        this._baseUrl = baseUrl;
        this._documentSelector = getSearchTypeDocSelector(searchType);
    }

    public String getHeadingSelector() {
        return "";
    }

    public Enums.SearchType getSearchType() {
        return _searchType;
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

    private IDocumentSelector getSearchTypeDocSelector(Enums.SearchType searchType) {
        switch (searchType) {
            case DUCK_DUCK_GO:
                return new DuckDuckGoDocSelector();
            case WIKIPEDIA:
                return new GenericDocumentSelector();
            default:
                return null;
        }
    }
}
