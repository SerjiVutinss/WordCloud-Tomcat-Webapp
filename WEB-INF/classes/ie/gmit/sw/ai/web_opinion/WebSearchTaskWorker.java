package ie.gmit.sw.ai.web_opinion;

import ie.gmit.sw.ai.cloud.WordFrequency;
import ie.gmit.sw.ai.web_opinion.models.SearchQuery;
import ie.gmit.sw.ai.web_opinion.models.SearchQueryTask;
import ie.gmit.sw.ai.web_opinion.services.QueryRunner;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ConcurrentMap;

/**
 * Implementation of Runnable which processes SearchQueryTask objects on the inQueue and
 * places results on the outQueue.
 */
public class WebSearchTaskWorker implements Runnable {

    /**
     * This will be the main OUT-QUEUE for the requests, instantiated within the Service Handler.
     */
    private final BlockingQueue<SearchQueryTask> _inQueue;
    private final ConcurrentMap<String, WordFrequency[]> _outQueue;

    private boolean _keepRunning = true;

    private final String TAG = "[WEB SEARCH TASK WORKER] ";

    /**
     * Single Class Constructor
     *
     * @param inQueue  task queue containing tasks submitted via the ServiceHandler or MockServiceHandler (for testing)
     * @param outQueue processed tasks are placed on this queue to be displayed by a Handler
     */
    public WebSearchTaskWorker(BlockingQueue<SearchQueryTask> inQueue, ConcurrentMap<String, WordFrequency[]> outQueue) {
        this._inQueue = inQueue;
        this._outQueue = outQueue;
    }

    /**
     * Implementation of the Runnable interface.
     * <p>
     * Process a request from the _inQueue and place the results on the _outQueue.
     */
    @Override
    public void run() {
        System.out.println(TAG + "Starting new worker.");
        while (_keepRunning) {
            try {
                // Try to get a task from the queue
                SearchQueryTask requestQuery = _inQueue.take();
                if (requestQuery != null) {

                    String taskNumber = requestQuery.getTaskNumber();
                    SearchQuery searchQuery = requestQuery.getSearchQuery();

                    // Instantiate and run QueryRunner to process the search query...
                    QueryRunner qr = new QueryRunner(searchQuery);
                    qr.run();

                    // This thread will wait until a result is received
                    WordFrequency[] results = qr.getResults();
                    // add the language and NGrams, respectively.

                    _outQueue.put(taskNumber, results);
                    System.out.println(String.format("%s Completed Task %s with %d results and added to OUT Queue.",
                            TAG, taskNumber, results.length));
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
