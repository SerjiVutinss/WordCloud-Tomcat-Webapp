package ie.gmit.sw.ai.web_opinion.utils;

import java.util.Map;

/**
 * Describes behaviour expected from a FrequencyMap data structure.
 *
 * @param <E> Add instances of this type to the FrequencyMap
 */
public interface IFrequencyMap<E> extends Map<E, Integer> {
    /**
     * Put an instance into the map
     * @param key instance of type E to add to the map.
     * @return the number of occurrences of instance 'key' which have been attempted to be put in the map.
     */
    Integer put(E key);
}
