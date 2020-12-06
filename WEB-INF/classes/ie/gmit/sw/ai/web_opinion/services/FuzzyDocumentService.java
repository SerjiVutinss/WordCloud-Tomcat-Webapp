package ie.gmit.sw.ai.web_opinion.services;

import ie.gmit.sw.ai.web_opinion.models.IScorableDocument;
import ie.gmit.sw.ai.web_opinion.utils.Config;

public class FuzzyDocumentService extends BaseFuzzyService<IScorableDocument> {

    public FuzzyDocumentService() {
        super(Config.FCL_FILE_LOCATION);
    }

    @Override
    public int getScore(IScorableDocument scoredDocument) {

        // Load from 'FCL' file

        // Error while loading?
        if (fis == null) {
            System.err.println("Can't load file: '" + fileName + "'");
            return 0;
        }

        // Set inputs
        fis.setVariable("title", scoredDocument.getTitleScore());
        fis.setVariable("headings", scoredDocument.getHeadingScore());
        fis.setVariable("body", scoredDocument.getBodyScore());
        // Evaluate
        fis.evaluate();

        int result = (int) fis.getVariable("relevancy").getLatestDefuzzifiedValue();

        return result;
    }
}
