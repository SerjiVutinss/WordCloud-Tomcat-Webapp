package ie.gmit.sw.ai.web_opinion;

import ie.gmit.sw.ai.web_opinion.utils.Config;
import net.sourceforge.jFuzzyLogic.FIS;

public class FuzzyLogicCalculator {

    private final FIS fis;
    private final String fileName;

    public FuzzyLogicCalculator() {
        fileName = Config.FCL_FILE_LOCATION;
        fis = FIS.load(fileName);
    }

    public int getFuzzyHeuristic(int title, int headings, int body) {

        // Load from 'FCL' file

        // Error while loading?
        if( fis == null ) {
            System.err.println("Can't load file: '" + fileName + "'");
            return 0;
        }

        // Set inputs
        fis.setVariable("title", title);
        fis.setVariable("headings", headings);
        fis.setVariable("body", body);
        // Evaluate
        fis.evaluate();

        int result = (int)fis.getVariable("relevancy").getLatestDefuzzifiedValue();
        return result;
    }

}
