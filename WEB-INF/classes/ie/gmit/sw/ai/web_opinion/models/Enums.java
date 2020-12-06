package ie.gmit.sw.ai.web_opinion.models;

/**
 * Central location for any Enums used in the application.
 */
public class Enums {

    /**
     * Types of search supported by the application.
     */
    public enum WebSearchType {
        DUCK_DUCK_GO,
        WIKIPEDIA
    }

    public enum HeuristicSearchType {
        BEST_FIRST,
        BEAM
    }
}
