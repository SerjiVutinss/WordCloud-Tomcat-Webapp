package ie.gmit.sw.ai.web_opinion;

import java.util.Collection;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentSkipListMap;

public class FrequencyMap implements Map<String, Integer> {

    private Map<String, Integer> map;

    public FrequencyMap() {
        map = new ConcurrentSkipListMap<>();
    }

    public Integer put(String key) {
        if(map.containsKey(key)){
            int frequency = map.get(key);
            frequency+=1;
            return map.put(key, frequency);
        } else {
            return map.put(key, 1);
        }
    }

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
    public Integer put(String key, Integer value) {
        return map.put(key, value);
    }

    @Override
    public Integer remove(Object key) {
        return map.remove(key);
    }

    @Override
    public void putAll(Map<? extends String, ? extends Integer> m) {
        map.putAll(m);
    }

    @Override
    public void clear() {
        map.clear();
    }

    @Override
    public Set<String> keySet() {
        return map.keySet();
    }

    @Override
    public Collection<Integer> values() {
        return map.values();
    }

    @Override
    public Set<Entry<String, Integer>> entrySet() {
        return map.entrySet();
    }
}
