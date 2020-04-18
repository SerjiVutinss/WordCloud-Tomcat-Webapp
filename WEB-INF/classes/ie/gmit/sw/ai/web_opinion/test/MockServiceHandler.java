package ie.gmit.sw.ai.web_opinion.test;

import ie.gmit.sw.ai.cloud.WordFrequency;
import ie.gmit.sw.ai.web_opinion.SearchQueryTaskWorker;
import ie.gmit.sw.ai.web_opinion.models.Query;
import ie.gmit.sw.ai.web_opinion.models.SearchQueryTask;

import java.io.PrintWriter;
import java.io.Writer;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.ConcurrentSkipListMap;

public class MockServiceHandler implements Runnable {


    private BlockingQueue<SearchQueryTask> _inQueue;
    private ConcurrentMap<String, WordFrequency[]> _outQueue;

    private String TAG = "[MOCK SERVICE HANDLER] ";

    @Override
    public void run() {

        _inQueue = new ArrayBlockingQueue<>(10);
        _outQueue = new ConcurrentSkipListMap<>();

        Writer out = new PrintWriter(System.out); //Write out text. We can write out binary too and change the MIME type...

        //Initialise some request variables with the submitted form info. These are local to this method and thread safe...
        // SearchQuery variables
        String query = "species";
        String searchType = "wikipedia";
        int maxResults = 32;
        int maxDepth = 50;

        Query searchQuery = new Query.QueryBuilder(query, searchType)
                .setMaxDepth(maxDepth)
                .setMaxResults(maxResults)
                .build();

        System.out.println(TAG + "Created new SearchQuery: " + searchQuery.toString());


        int pollingTime = 5000;
        String taskNumber = null;
        int jobNumber = 0;

        boolean isFinished = false;
        WordFrequency[] taskResult = new WordFrequency[0];


        // First request - must create the task number
        if (taskNumber == null) {

            taskNumber = new String("T" + jobNumber);
            jobNumber++;
            System.out.println(TAG + "Created new task number: " + taskNumber);

            // Create a separate worker thread for each client's request.
            new Thread(new SearchQueryTaskWorker(_inQueue, _outQueue)).start();

            try {

                // Create the SearchQueryTask from the Query object
                SearchQueryTask searchTask = new SearchQueryTask(taskNumber, searchQuery);
                System.out.println(TAG + "Created new QueryTask: " + searchTask.getTaskNumber() + "; " + searchTask.getSearchQuery());


                // Add the task to in and out queues
                _inQueue.put(searchTask);
                System.out.println(TAG + taskNumber + " placed in IN queue.");

                _outQueue.put(taskNumber, new WordFrequency[0]);
                System.out.println(TAG + taskNumber + " placed on OuT queue.");
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }


        while (!isFinished) {

            try {
                // Check OUT-QUEUE for finished job.
                taskResult = _outQueue.get(taskNumber);
                if (taskResult != null && taskResult.length > 0) {
                    System.out.println(TAG + "GOT RESULT FROM OUT QUEUE");
                    isFinished = true;
                    System.out.println(TAG + "FINISHED WITH QUEUE");
                } else {
                }
            } catch (NullPointerException e) {
            }
            try {
                Thread.sleep(pollingTime);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        System.out.println("\n" + TAG + "RESULTS:");

        for (WordFrequency wf : taskResult) {
            System.out.println(TAG + String.format("%s : %d", wf.getWord(), wf.getFrequency()));
        }
    }
}
