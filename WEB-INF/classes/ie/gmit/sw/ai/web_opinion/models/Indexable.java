package ie.gmit.sw.ai.web_opinion.models;

import ie.gmit.sw.ai.web_opinion.utils.IMergeableFrequencyMap;

/**
 * Generic interface which describes behaviour which allows an implementing instance to be indexed.
 *
 * The implementing class is expected to return a IMergeableFrequencyMap instance.
 *
 * @param <E> The type of elements expected in the IMergeableFrequencyMap instance.
 */
public interface Indexable<E> {
    IMergeableFrequencyMap<E> index();
}
