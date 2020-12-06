package ie.gmit.sw.ai.web_opinion.services;

import net.sourceforge.jFuzzyLogic.FIS;

public abstract class BaseFuzzyService<E> implements IFuzzyService<E> {

    protected final FIS fis;
    protected final String fileName;

    public BaseFuzzyService(String fclFilename) {
        fileName = fclFilename;
        fis = FIS.load(fileName);
    }
}
