package ie.gmit.sw.ai.web_opinion;

import ie.gmit.sw.ai.cloud.WordFrequency;

import java.io.IOException;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

public class Runner {


    public static void main(String[] args) throws InterruptedException {

//        FuzzyLogicCalculator.getFuzzyHeuristic(10, 30, 50);

//        runParser();

        final BlockingQueue<Tuple<String, String>> _inQueue = new ArrayBlockingQueue<>(100);
        final ConcurrentMap<String, WordFrequency[]> _outQueue = new ConcurrentHashMap<>();

        runThreadedExample(_inQueue, _outQueue);

        Thread.sleep(1000);

        _inQueue.put(new Tuple<String, String>("1", "Hello WOrld!"));

        Thread.sleep(5000);

        try {
            WordFrequency[] results = _outQueue.get("1");
            for (WordFrequency w : results) {
                System.out.println(w.getWord() + ": " + w.getFrequency());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void runThreadedExample(BlockingQueue<Tuple<String, String>> _inQueue, ConcurrentMap<String, WordFrequency[]> _outQueue) {

        new Thread(new NodeParserWorker(_inQueue, _outQueue)).start();

    }

    public static void runParser() {
        NodeParserExample npe = null;
        try {
            npe = new NodeParserExample();
            npe.run("https://jsoup.org/cookbook/input/parse-document-from-string", "Java");
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (npe != null) {
            for (WordFrequency w : npe.getResults()) {
                System.out.println(w.getWord() + ": " + w.getFrequency());
            }
        }
    }
}
