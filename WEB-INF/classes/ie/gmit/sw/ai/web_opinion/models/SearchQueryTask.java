package ie.gmit.sw.ai.web_opinion.models;

/***
 * Bean class representing a SearchQuery which has been submitted as a task to a worker thread.
 */
public class SearchQueryTask {

    private String _taskNumber;
    private SearchQuery _searchQuery;

    /**
     * Single class constructor
     * @param taskNumber Generated task number associated with this task.
     * @param searchQuery SearchQuery object which will be used to perform the search by the worker.
     */
    public SearchQueryTask(String taskNumber, SearchQuery searchQuery) {
        this._taskNumber = taskNumber;
        this._searchQuery = searchQuery;
    }

    /**
     * @return the number assigned to this task.
     */
    public String getTaskNumber() {
        return _taskNumber;
    }

    /**
     * @return the SearchQuery object associated with this task.
     */
    public SearchQuery getSearchQuery() {
        return this._searchQuery;
    }
}