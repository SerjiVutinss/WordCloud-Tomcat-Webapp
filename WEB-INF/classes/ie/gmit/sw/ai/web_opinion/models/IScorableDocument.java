package ie.gmit.sw.ai.web_opinion.models;

import ie.gmit.sw.ai.web_opinion.selectors.IEdge;

import java.util.List;

/**
 * Defines the behaviour expected on a document which can be scored.
 *
 * Extends the Indexable<String> interface.
 */
public interface IScorableDocument extends Indexable<String> {

    /**
     * @return the unique identifier for this instance
     */
    String getIdentifier();

    /**
     * @return the query which this document was scored against
     */
    String getQuery();

    /**
     * @return all instances of edges associated with this document, e.g. hyperlink
     */
    List<IEdge> getEdges();

    /**
     * @return score of the title portion of this document
     */
    int getTitleScore();

    /**
     * @return score of the combined heading portions of this document
     */
    int getHeadingScore();

    /**
     * @return score of the combined body portions of this document
     */
    int getBodyScore();

    /**
     * @return score of the combined title, heading and body portions of this document
     */
    int getCombinedScore();
}
