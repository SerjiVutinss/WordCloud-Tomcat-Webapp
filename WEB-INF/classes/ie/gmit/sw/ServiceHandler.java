package ie.gmit.sw;

import ie.gmit.sw.ai.cloud.LogarithmicSpiralPlacer;
import ie.gmit.sw.ai.cloud.WeightedFont;
import ie.gmit.sw.ai.cloud.WordFrequency;
import ie.gmit.sw.ai.web_opinion.SearchQueryTaskWorker;
import ie.gmit.sw.ai.web_opinion.models.Enums;
import ie.gmit.sw.ai.web_opinion.models.SearchQueryTask;
import ie.gmit.sw.ai.web_opinion.utils.Config;

import javax.imageio.ImageIO;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.awt.image.BufferedImage;
import java.io.*;
import java.util.Arrays;
import java.util.Base64;
import java.util.Comparator;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/*
 * -------------------------------------------------------------------------------------------------------------------
 * PLEASE READ THE FOLLOWING CAREFULLY. MOST OF THE "ISSUES" STUDENTS HAVE WITH DEPLOYMENT ARISE FROM NOT READING
 * AND FOLLOWING THE INSTRUCTIONS BELOW.
 * -------------------------------------------------------------------------------------------------------------------
 *
 * To compile this servlet, open a command prompt in the web application directory and execute the following commands:
 *
 * Linux/Mac													Windows
 * ---------													---------
 * cd WEB-INF/classes/											cd WEB-INF\classes\
 * javac -cp .:$TOMCAT_HOME/lib/* ie/gmit/sw/*.java				javac -cp .:%TOMCAT_HOME%/lib/* ie/gmit/sw/*.java
 * cd ../../													cd ..\..\
 * jar -cf wcloud.war *											jar -cf wcloud.war *
 *
 * Drag and drop the file ngrams.war into the webapps directory of Tomcat to deploy the application. It will then be
 * accessible from http://localhost:8080. The ignore words file at res/ignorewords.txt will be located using the
 * IGNORE_WORDS_FILE_LOCATION mapping in web.xml. This works perfectly, so don't change it unless you know what
 * you are doing...
 *
 */


public class ServiceHandler extends HttpServlet {

    private String ignoreWords = null;
    private File f;

    private static long jobNumber = 0; //The number of the task in the async queue

    // <Tuple<jobNumber, queryText>>

    // Queries - for now, just a <job#, URL>
    private BlockingQueue<SearchQueryTask> _inQueue = new ArrayBlockingQueue<>(100);
    // Results - for now, <Job#, WordFrequency[]>
    private ConcurrentMap<String, WordFrequency[]> _outQueue = new ConcurrentHashMap<>();

    public void init() throws ServletException {
        ServletContext ctx = getServletContext(); //Get a handle on the application context

        //Reads the value from the <context-param> in web.xml
        ignoreWords = getServletContext().getRealPath(File.separator) + ctx.getInitParameter("IGNORE_WORDS_FILE_LOCATION");
        Config.IGNORE_WORDS_FILE_LOCATION = ignoreWords;
        Config.FCL_FILE_LOCATION = getServletContext().getRealPath(File.separator) + ctx.getInitParameter("FCL_FILE_LOCATION");
        f = new File(ignoreWords); //A file wrapper around the ignore words...
    }

    public void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        resp.setContentType("text/html"); //Output the MIME type
        PrintWriter out = resp.getWriter(); //Write out text. We can write out binary too and change the MIME type...

        //Initialise some request varuables with the submitted form info. These are local to this method and thread safe...

        String option = req.getParameter("cmbOptions"); //Change options to whatever you think adds value to your assignment...

        String searchSite = req.getParameter("cmbSearchSite");
        System.out.println(searchSite);


        Enums.SearchType searchType = null;

        switch (searchSite) {
            case "Wikipedia":
                searchType = Enums.SearchType.WIKIPEDIA;
                break;

            case "DuckDuckGo":
                searchType = Enums.SearchType.DUCK_DUCK_GO;
                break;

            default:
                break;
        }


        String taskNumber = req.getParameter("frmTaskNumber");
        String query = req.getParameter("query");


        String pollingTime = "5000";

        // First request - must create the task number
        if (taskNumber == null) {

            taskNumber = new String("T" + jobNumber);
            jobNumber++;
            System.out.println("CREATED TASK#: " + taskNumber);

//			// Create a separate worker thread for each client's request.
            // START THE PARSER HERE...
            new Thread(new SearchQueryTaskWorker(_inQueue, _outQueue)).start();

            printWaitPage(out, taskNumber, query, pollingTime, searchSite);

            try {
                // Add the job number and payload to the IN-QUEUE
                _inQueue.put(new SearchQueryTask(taskNumber, query, searchType));
                System.out.println(taskNumber + ": " + query + " PLACED ON IN QUEUE");
                // Also add the job number to the OUT-QUEUE with a null value.
                _outQueue.put(taskNumber, null);
                System.out.println(taskNumber + " PLACED ON IN QUEUE");
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

        } else {
            // Waiting on a job to complete.
            try {
                // Check OUT-QUEUE for finished job.
                WordFrequency[] taskResult = _outQueue.get(taskNumber);
                System.out.println("TASK " + taskNumber + " FOUND ON OUT QUEUE");
                if (taskResult != null && taskResult.length > 0) {

                    System.out.println("\tGOT RESULT FROM OUT QUEUE");
                    // Get hard-coded results for demonstration
                    WordFrequency[] words = new WeightedFont().getFontSizes(taskResult);
                    Arrays.sort(words, Comparator.comparing(WordFrequency::getFrequency, Comparator.reverseOrder()));

                    // Spira Mirabilis
                    LogarithmicSpiralPlacer placer = new LogarithmicSpiralPlacer(800, 600);
                    for (WordFrequency word : words) {
                        placer.place(word); //Place each word on the canvas starting with the largest
                    }

                    BufferedImage cloud = placer.getImage(); //Get a handle on the word cloud graphic

                    printResults(out, cloud);

                } else {
                    // Still waiting for task to complete...
                    System.out.println("\tRESULTS WAS NULL");
                    printWaitPage(out, taskNumber, query, pollingTime, searchSite);
                }
            } catch (NullPointerException e) {
//                e.printStackTrace();
                System.out.println(taskNumber + " NOT FOUND ON OUT QUEUE");
                printWaitPage(out, taskNumber, query, pollingTime, searchSite);
            }
        }
    }

    public void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        doGet(req, resp);
    }

    private String encodeToString(BufferedImage image) {
        String s = null;
        ByteArrayOutputStream bos = new ByteArrayOutputStream();

        try {
            ImageIO.write(image, "png", bos);
            byte[] bytes = bos.toByteArray();

            Base64.Encoder encoder = Base64.getEncoder();
            s = encoder.encodeToString(bytes);
            bos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return s;
    }

    private BufferedImage decodeToImage(String imageString) {
        BufferedImage image = null;
        byte[] bytes;
        try {
            Base64.Decoder decoder = Base64.getDecoder();
            bytes = decoder.decode(imageString);
            ByteArrayInputStream bis = new ByteArrayInputStream(bytes);
            image = ImageIO.read(bis);
            bis.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return image;
    }

    private void printWaitPage(PrintWriter out, String taskNumber, String queryText, String pollingTime, String searchSite) {
        out.print("<H1>Processing request for Job#: " + taskNumber + "</H1>");
        out.print("<div id=\"r\"></div>");
        out.print("<font color=\"#993333\"><b>");
//		out.print("<P>Language Dataset is located at " + languageDataSet);
//		out.print("<P>Language Dataset is <b><u>" + _dataSetFile.length() + "</u></b> bytes in size");
        out.print("<br>Polling Time: " + pollingTime + "(ms)");
//		out.print("<br>Number of Results: " + numberOfResults);
        out.print("<br>Query Text: " + queryText);
        out.print("<br>Searching site: " + searchSite);
        out.print("</font><p/>");
        out.print("<form method=\"POST\" name=\"frmRequestDetails\">");
        out.print("<input name=\"cmbSearchSite\" type=\"hidden\" value=\"" + searchSite + "\">");
        out.print("<input name=\"cmbPollingTime\" type=\"hidden\" value=\"" + pollingTime + "\">");
//		out.print("<input name=\"cmbNumberOfResults\" type=\"hidden\" value=\"" + numberOfResults + "\">");
        out.print("<input name=\"query\" type=\"hidden\" value=\"" + queryText + "\">");
        out.print("<input name=\"frmTaskNumber\" type=\"hidden\" value=\"" + taskNumber + "\">");
        out.print("</form>");
        out.print("</body>");
        out.print("</html>");

        out.print("<script>");
//              out.print("<LI>Return the jobNumber to the client web browser with a wait interval using <meta http-equiv=\"refresh\" content=\"10\">. The content=\"10\" will wait for 10s.");
        out.print("var wait=setTimeout(\"document.frmRequestDetails.submit();\", " + pollingTime + ");");
        out.print("</script>");
    }

    private void printResults(PrintWriter out, BufferedImage cloud) {
        out.print("<html><head><title>Artificial Intelligence Assignment</title>");
        out.print("<link rel=\"stylesheet\" href=\"includes/style.css\">");

        out.print("</head>");
        out.print("<body>");
        out.print("<div style=\"font-size:48pt; font-family:arial; color:#990000; font-weight:bold\">Web Opinion Visualiser</div>");

        out.print("<p><h2>Please read the following carefully</h2>");
        out.print("<p>The &quot;ignore words&quot; file is located at <font color=red><b>" + f.getAbsolutePath() + "</b></font> and is <b><u>" + f.length() + "</u></b> bytes in size.");
        out.print("You must place any additional files in the <b>res</b> directory and access them in the same way as the set of ignore words.");
        out.print("<p>Place any additional JAR archives in the WEB-INF/lib directory. This will result in Tomcat adding the library of classes ");
        out.print("to the CLASSPATH for the web application context. Please note that the JAR archives <b>jFuzzyLogic.jar</b>, <b>encog-core-3.4.jar</b> and ");
        out.print("<b>jsoup-1.12.1.jar</b> have already been added to the project.");

        out.print("<p><fieldset><legend><h3>Result</h3></legend>");

        out.print("<img src=\"data:image/png;base64," + encodeToString(cloud) + "\" alt=\"Word Cloud\">");


        out.print("</fieldset>");
        out.print("<P>Maybe output some search stats here, e.g. max search depth, effective branching factor.....<p>");
        out.print("<a href=\"./\">Return to Start Page</a>");
        out.print("</body>");
        out.print("</html>");

    }
}