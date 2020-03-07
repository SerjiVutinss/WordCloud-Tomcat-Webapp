package ie.gmit.sw.ai.web_opinion;

import net.sourceforge.jFuzzyLogic.FIS;

public class FuzzyLogicCalculator {

    public static int getFuzzyHeuristic(int title, int headings, int body) {
        //
        // Load from 'FCL' file
        String fileName = "./fcl/my-fcl.fcl";
        FIS fis = FIS.load(fileName,true);

        // Error while loading?
        if( fis == null ) {
            System.err.println("Can't load file: '" + fileName + "'");
            return 0;
        }

        // Set inputs
        fis.setVariable("title", title);
        fis.setVariable("headings", headings);
        fis.setVariable("body", body);

        // if TITLE is significant and headings is relevant and
        // body is frequent then score is high

        // Evaluate
        fis.evaluate();

        // if fuzzy score is high, call index on the title, headings and body

        // Print ruleSet
        System.out.println(fis);

        return 1;
    }

}
