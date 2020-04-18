package ie.gmit.sw.ai.web_opinion.services;

import ie.gmit.sw.ai.web_opinion.utils.Config;
import ie.gmit.sw.ai.web_opinion.models.ScoredDocument;
import net.sourceforge.jFuzzyLogic.FIS;

public class FuzzyService {

    private final FIS fis;
    private final String fileName;

    public FuzzyService() {
        fileName = Config.FCL_FILE_LOCATION;
        fis = FIS.load(fileName);
    }

    public int getFuzzyHeuristic(ScoredDocument scoredDocument) {

        // Load from 'FCL' file

        // Error while loading?
        if( fis == null ) {
            System.err.println("Can't load file: '" + fileName + "'");
            return 0;
        }

        // Set inputs
        fis.setVariable("title", scoredDocument.getTitleScore());
        fis.setVariable("headings", scoredDocument.getHeadingScore());
        fis.setVariable("body", scoredDocument.getBodyScore());
        // Evaluate
        fis.evaluate();

        return (int)fis.getVariable("relevancy").getLatestDefuzzifiedValue();
    }
}
