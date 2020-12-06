package ie.gmit.sw.ai.web_opinion.utils;

/**
 * Describes behaviour which may allow an implementing instance to merge itself with another frequency map.
 *
 * @param <E> Type of elements which are held in the IFrequencyMap
 */
public interface IMergeableFrequencyMap<E> extends IFrequencyMap<E> {
    /**
     * Merge all elements of inputMap and their frequencies to this map.
     * @param inputMap merge this map to the implementing class' IMergeableFrequencyMap map.
     */
    void merge(IMergeableFrequencyMap<E> inputMap);
}
