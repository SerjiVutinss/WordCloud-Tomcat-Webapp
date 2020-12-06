package ie.gmit.sw.ai.web_opinion.models;

import ie.gmit.sw.ai.web_opinion.services.SearchTypeService;

public class SearchQuery {

    /**
     * Static inner builder class for SearchQuery
     */
    public static class SearchQueryBuilder {

        private final String _query;
        private final WebSearchType _Web_search;
        private int _maxResults;
        private int _maxDepth;
        private int _beamWidth;
        private int _threshold;


        private Enums.HeuristicSearchType heuristicSearchType;

        /**
         * Build a SearchQuery object using the search term (query) and searchType
         *
         * @param query      term to be searched
         * @param searchType type of search (site) to be performed using this query
         */
        public SearchQueryBuilder(String query, String searchType) {
            this(query, SearchTypeService.getSearchModel(searchType));
        }

        /**
         * Build a SearchQuery object using the search term (query) and searchType
         *
         * @param query         term to be searched
         * @param webSearchType enum type of search (site) to be performed using this query
         */
        public SearchQueryBuilder(String query, Enums.WebSearchType webSearchType) {
            this(query, SearchTypeService.getSearchModel(webSearchType));
        }

        private SearchQueryBuilder(String query, WebSearchType webSearchType) {
            this._query = query;
            this._Web_search = webSearchType;
        }

        /**
         * Set max results
         *
         * @param maxResults max number of results to return from search
         * @return modified builder object
         */
        public SearchQueryBuilder setMaxResults(int maxResults) {
            this._maxResults = maxResults;
            return this;
        }

        /**
         * Set max depth
         *
         * @param maxDepth max depth to traverse within the search
         * @return modified builder object
         */
        public SearchQueryBuilder setMaxDepth(int maxDepth) {
            this._maxDepth = maxDepth;
            return this;
        }

        /**
         * Set threshold
         *
         * @param threshold minimum score for IScorable to be accepted in search.
         * @return modified builder object
         */
        public SearchQueryBuilder setThreshold(int threshold) {
            this._threshold = threshold;
            return this;
        }

        /**
         * Set beam width
         *
         * @param beamWidth width of beam if Beam Search is used
         * @return modified builder object
         */
        public SearchQueryBuilder setBeamWidth(int beamWidth) {
            this._beamWidth = beamWidth;
            return this;
        }

        public SearchQueryBuilder setHeuristicSearchType(Enums.HeuristicSearchType heuristicSearchType) {
            this.heuristicSearchType = heuristicSearchType;
            return this;
        }
        /**
         * Build and return the builder object.
         *
         * @return the built object.
         */
        public SearchQuery build() {
            this._Web_search.set_maxDepth(_maxDepth);
            this._Web_search.set_maxResults(_maxResults);
            this._Web_search.set_threshold(_threshold);
            this._Web_search.set_beamWidth(_beamWidth);
            if (heuristicSearchType == null) {
                this._Web_search.setHeuristicSearchType(Enums.HeuristicSearchType.BEST_FIRST);
            } else {
                this._Web_search.setHeuristicSearchType(heuristicSearchType);
            }

            return new SearchQuery(this._query, this._Web_search);
        }

    }


    /**
     * Search Query class
     */

    private String _query;
    private WebSearchType _Web_search;

    /**
     * Private constructor only ever used by the builder
     */
    private SearchQuery(String _query, WebSearchType webSearchType) {
        this._query = _query;
        this._Web_search = webSearchType;
    }

    /**
     * @return the WebSearch object related to this instance.
     */
    public WebSearchType getSearchModel() {
        return _Web_search;
    }

    /**
     * The search term associated with this SearchQuery
     *
     * @return
     */
    public String getQuery() {
        return _query;
    }

    /**
     * The starting URL to be used for this SearchQuery
     *
     * @return
     */
    public String getUrl() {
        return _Web_search.getBaseUrl() + getQuery();
    }

    /**
     * The maximum number of results to be returned by this SearchQuery
     *
     * @return
     */
    public int getMaxResults() {
        return _Web_search.getMaxResults();
    }

    @Override
    public String toString() {
        return String.format("{Query: %s; SearchType: %s; Url: %s; MaxResults: %d; MaxDepth: %d}",
                this._query,
                this._Web_search.getSearchType(),
                this.getUrl(),
                this._Web_search.getMaxResults(),
                this._Web_search.getMaxDepth());
    }
}
