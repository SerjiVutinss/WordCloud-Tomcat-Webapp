package ie.gmit.sw.ai.web_opinion.models;

import ie.gmit.sw.ai.web_opinion.services.SearchTypeService;

public class Query {

    private String _query;
    private SearchModel _searchModel;

    public Query(String _query, SearchModel searchModel) {
        this._query = _query;
        this._searchModel = searchModel;
    }

    public SearchModel getSearchModel() {
        return _searchModel;
    }

    public String getQuery() {
        return _query;
    }

    public String getUrl() {
        return _searchModel.getBaseUrl() + getQuery();
    }

    public Enums.SearchType getSearchType() {
        return _searchModel.getSearchType();
    }

    public int getMaxResults() {
        return _searchModel.getMaxResults();
    }

    public int getMaxDepth() {
        return _searchModel.getMaxResults();
    }

    @Override
    public String toString() {
        return String.format("{Query: %s; SearchType: %s; Url: %s; MaxResults: %d; MaxDepth: %d}",
                this._query,
                this._searchModel.getSearchType(),
                this.getUrl(),
                this._searchModel.getMaxResults(),
                this._searchModel.getMaxDepth());
    }

    public static class QueryBuilder {

        private final String _query;
        private final SearchModel _searchModel;
        private int _maxResults;
        private int _maxDepth;
        private int _threshold;

        public QueryBuilder(String query, String searchType) {
            this(query, SearchTypeService.getSearchModel(searchType));
        }

        public QueryBuilder(String query, Enums.SearchType searchType) {
            this(query, SearchTypeService.getSearchModel(searchType));
        }

        public QueryBuilder(String query, SearchModel searchModel) {
            this._query = query;
            this._searchModel = searchModel;
        }

        public QueryBuilder setMaxResults(int maxResults) {
            this._maxResults = maxResults;
            return this;
        }

        public QueryBuilder setMaxDepth(int maxDepth) {
            this._maxDepth = maxDepth;
            return this;
        }

        public QueryBuilder setThreshold(int threshold) {
            this._threshold = threshold;
            return this;
        }

        public Query build() {
            return new Query(this._query, this._searchModel);
        }
    }
}
