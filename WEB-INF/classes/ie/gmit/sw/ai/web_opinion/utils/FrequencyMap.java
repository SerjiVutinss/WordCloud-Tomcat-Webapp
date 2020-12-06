package ie.gmit.sw.ai.web_opinion.utils;

import java.util.Collection;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.ConcurrentSkipListMap;

/**
 * Generic implementation of IMergeableFrequencyMap
 * @param <E> Type of elements whose frequency are to be tracked by the FrequencyMap
 */
public class FrequencyMap<E> implements IMergeableFrequencyMap<E> {

    private ConcurrentMap<E, Integer> map;

    public FrequencyMap() {
        map = new ConcurrentSkipListMap<>();
    }

    /**
     * Implementation of the put() method from IFrequencyMap
     *
     * @param key instance of type E to add to the map.
     * @return the number of times instance 'key' has been added to the structure
     */
    @Override
    public Integer put(E key) {
        if (map.containsKey(key)) {
            int frequency = map.get(key);
            frequency += 1;
            return map.put(key, frequency);
        } else {
            map.put(key, 1);
            return 1;
        }
    }


    /**
     * Merge all elements of inputMap to this instance of IFrequencyMap, combining frequencies where necessary.
     *
     * Any instances of E already in this.map will have their frequency incremented by the occurrence of the same
     * element.
     *
     * @param inputMap merge this map to the implementing class' IMergeableFrequencyMap map.
     */
    @Override
    public void merge(IMergeableFrequencyMap<E> inputMap) {
        // Loop through all entries in the inputMap
        for (Map.Entry<E, Integer> e : inputMap.entrySet()) {
            // If our map already contains this element
            if (map.containsKey(e.getKey())) {
                // Put the element in our map, combining the frequencies from both maps.
                map.put(e.getKey(), map.get(e.getKey()) + e.getValue());
            } else {
                // Element not in our map, just add it with the frequency from inputMap.
                map.put(e.getKey(), e.getValue());
            }
        }
    }

    //<editor-fold desc="Map Implementation">
    @Override
    public int size() {
        return map.size();
    }

    @Override
    public boolean isEmpty() {
        return map.isEmpty();
    }

    @Override
    public boolean containsKey(Object key) {
        return map.containsKey(key);
    }

    @Override
    public boolean containsValue(Object value) {
        return map.containsValue(value);
    }

    @Override
    public Integer get(Object key) {
        return map.get(key);
    }

    @Override
    @Deprecated
    public Integer put(E key, Integer value) {
        return map.put(key, value);
    }

    @Override
    public Integer remove(Object key) {
        return map.remove(key);
    }

    @Override
    public void putAll(Map<? extends E, ? extends Integer> m) {
        map.putAll(m);
    }

    @Override
    public void clear() {
        map.clear();
    }

    @Override
    public Set<E> keySet() {
        return map.keySet();
    }

    @Override
    public Collection<Integer> values() {
        return map.values();
    }

    @Override
    public Set<Entry<E, Integer>> entrySet() {
        return map.entrySet();
    }
    //</editor-fold desc="Map">
}
