import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;

import java.time.Duration;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutionException;

public class CacheDemo {
    public static Map<Integer,Integer> sourceMap = new HashMap<>();

    static {
        for (int i=0;i<10;i++){
            sourceMap.put(i, i);
        }
    }

    public static void main(String[] args) throws ExecutionException {
        LoadingCache<Object,Object> loadingCache = CacheBuilder.newBuilder().maximumSize(10).build(new CacheLoader<Object, Object>() {
            @Override
            public Object load(Object o) throws Exception {
                return sourceMap.get(o);
            }
        });

        for (int va = 0;va<10;va++){
            loadingCache.get(va);
        }
        System.out.println(loadingCache.size());
        System.out.println(loadingCache.asMap());
    }


}
