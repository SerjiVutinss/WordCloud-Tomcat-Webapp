package ie.gmit.sw.ai.web_opinion;

import ie.gmit.sw.ai.cloud.WordFrequency;

import java.io.IOException;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ConcurrentMap;

public class NodeParserWorker implements Runnable {

        private final BlockingQueue<Tuple<String, String>> _inQueue;
        /**
         * This will be the main OUT-QUEUE for the requests, instantiated within the Service Handler.
         */
        private final ConcurrentMap<String, WordFrequency[]> _outQueue;

        private final NodeParserExample _nodeParser;

        private boolean _keepRunning = true;

        private final String TAG = "NODE PARSER WORKER: ";
    /**
         * Single Class Constructor
         * @param inQueue main request queue for the application.
         * @param outQueue main processed request data structure for the application.
         */
        public NodeParserWorker(BlockingQueue<Tuple<String, String>> inQueue, ConcurrentMap<String, WordFrequency[]> outQueue) {
            _inQueue = inQueue;
            _outQueue = outQueue;
//            TAG = "Threaded Request Processor: ";

            _nodeParser = new NodeParserExample();
        }

        /**
         * Implementation of the Runnable interface.
         *
         * Process a request from the _consumerQueue and place the results on the _outQueue.
         */
        @Override
        public void run() {
            System.out.println(TAG + "Starting.");
            while (_keepRunning) {
                try {
                    Tuple<String, String> requestQuery = _inQueue.take();
                    if (requestQuery != null) {

//                    Thread.sleep(30000); // for testing across multiple clients, etc

                        System.out.println(TAG + "Got " + requestQuery.getU());
                        // process the request string

                        // Parse the string, returns a frequency map for the query string.
                        _nodeParser.run("https://jsoup.org/cookbook/input/parse-document-from-string", "Java");
                        // Perform the distance calculation and store the results.
//                        List<Map.Entry<Enums.Language, Integer>> results = _distanceCalculator.calculate(sortedQueryMap);
                        WordFrequency[] results = _nodeParser.getResults();
                                // add the language and NGrams, respectively.
                        _outQueue.put(requestQuery.getT(), results);
                        System.out.println(TAG + "Completed Task: " + requestQuery.getT() + " and added to OUT Queue.");
                        // Each instance of this class should only need to process one request, so it can die now.
                        _keepRunning = false;
                    }
                } catch (InterruptedException | IOException e) {
                    e.printStackTrace();
                }
            }
            System.out.println(TAG + "Worker Finished.");
        }
}
