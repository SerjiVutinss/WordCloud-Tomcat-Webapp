package ie.gmit.sw.ai.web_opinion;

import ie.gmit.sw.ai.cloud.WordFrequency;
import ie.gmit.sw.ai.web_opinion.models.Query;
import ie.gmit.sw.ai.web_opinion.services.QueryRunner;
import ie.gmit.sw.ai.web_opinion.models.SearchQueryTask;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ConcurrentMap;

public class SearchQueryTaskWorker implements Runnable {

    private final BlockingQueue<SearchQueryTask> _inQueue;
    /**
     * This will be the main OUT-QUEUE for the requests, instantiated within the Service Handler.
     */
    private final ConcurrentMap<String, WordFrequency[]> _outQueue;

    private boolean _keepRunning = true;

    private final String TAG = "[SEARCH QUERY TASK WORKER] ";

    /**
     * Single Class Constructor
     *
     * @param inQueue  main request queue for the application.
     * @param outQueue main processed request data structure for the application.
     */
    public SearchQueryTaskWorker(BlockingQueue<SearchQueryTask> inQueue, ConcurrentMap<String, WordFrequency[]> outQueue) {
        _inQueue = inQueue;
        _outQueue = outQueue;
    }

    /**
     * Implementation of the Runnable interface.
     * <p>
     * Process a request from the _consumerQueue and place the results on the _outQueue.
     */
    @Override
    public void run() {
        System.out.println(TAG + "Starting.");

        while (_keepRunning) {
            try {
                SearchQueryTask requestQuery = _inQueue.take();
                if (requestQuery != null) {

                    String taskNumber = requestQuery.getTaskNumber();
                    Query searchQuery = requestQuery.getSearchQuery();
                    System.out.println(TAG + "Got - " + taskNumber + ", " + searchQuery.getQuery() + "(" + requestQuery.getSearchQuery());
                    // process the request string

                    // Parse the string, returns a frequency map for the query string.
                    QueryRunner qr = new QueryRunner(searchQuery);
                    qr.run();

                    WordFrequency[] results = qr.getResults();
                    List<WordFrequency> resultList = Arrays.asList(results);

                    // add the language and NGrams, respectively.
                    System.out.println(TAG + "Created results: " + results.length);
                    _outQueue.put(taskNumber, results);
                    System.out.println(TAG + "Completed Task " + taskNumber + " and added to OUT Queue.");
                    // Each instance of this class should only need to process one request, so it can die now.
                    _keepRunning = false;
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        System.out.println(TAG + "Worker Finished.");
    }
}
