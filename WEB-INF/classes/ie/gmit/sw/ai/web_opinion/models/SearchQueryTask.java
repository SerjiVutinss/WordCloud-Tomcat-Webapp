package ie.gmit.sw.ai.web_opinion.models;

public class SearchQueryTask {

    private String _taskNumber;
    private Query _searchQuery;

    public SearchQueryTask(String taskNumber, Query searchQuery) {
        this._taskNumber = taskNumber;
        this._searchQuery = searchQuery;
    }

    public String getTaskNumber() {
        return _taskNumber;
    }

    public Query getSearchQuery() {
        return this._searchQuery;
    }
}