package ch.ethz.globis.isk.service.cache;


import org.springframework.stereotype.Component;

import java.util.LinkedHashMap;
import java.util.Map;

@Component
public class RequestResultCache {

    private Map<Object, Object> map;

    int maxSize = 1000;

    public RequestResultCache() {
        map = new LinkedHashMap<Object, Object>() {
            @Override
            protected boolean removeEldestEntry(Map.Entry eldest) {
                return size() > maxSize;
            }
        };
    }

    public RequestResultCache(final int maxSize) {
        this.maxSize = maxSize;
        map = new LinkedHashMap<Object, Object>() {
            @Override
            protected boolean removeEldestEntry(Map.Entry eldest) {
                return size() > maxSize;
            }
        };
    }

    public Object put(Object key, Object value) {
        if (key != null && value != null) {
            return map.put(key, value);
        } else {
            return null;
        }
    }

    public Object get(Object key) {
        return map.get(key);
    }

    public Object remove(Object key) { return map.remove(key); }

    public void clear() {
        map.clear();
    }
}