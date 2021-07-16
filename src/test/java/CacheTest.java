import com.google.common.cache.*;
import com.google.common.collect.Lists;
import com.google.common.util.concurrent.ListenableFuture;
import com.google.common.util.concurrent.ListenableFutureTask;
import com.sun.org.apache.xml.internal.utils.ListingErrorHandler;
import org.junit.Test;

import java.time.Duration;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.*;
import java.util.function.Function;
import java.util.stream.Collectors;


public class CacheTest {
    public static Map<Integer,Integer> sourceMap = new HashMap<>();
    public static List<Integer> ids = new ArrayList<>();
    public static ExecutorService executorService= Executors.newFixedThreadPool(5);

    static {
        for (int i=0;i<10;i++){
            sourceMap.put(i, i);
        }
        ids.add(2);
        ids.add(5);
        ids.add(3);
        ids.add(1);
        ids.add(6);

    }

    @Test
    public void call() throws ExecutionException {
        Cache<Integer,Integer> cache = CacheBuilder.newBuilder().maximumSize(5).recordStats().build();
        Object values = cache.get(1, new Callable<Integer>() {
            @Override
            public Integer call() throws Exception {
                return sourceMap.get(1);
            }
        });

        System.out.println(values);
        cache.put(1, 2);
        System.out.println(cache.asMap().values());
        System.out.println(cache.stats().averageLoadPenalty());
        System.out.println(cache.getIfPresent(1));
    }


    @Test
    public void demo() throws ExecutionException, InterruptedException {
        LoadingCache<Integer,Integer> loadingCache = CacheBuilder
                .newBuilder()
                .maximumSize(5)
                .expireAfterAccess(10  ,TimeUnit.SECONDS )
                .recordStats()
                .removalListener(new RemovalListener<Object, Object>() {
                    @Override
                    public void onRemoval(RemovalNotification<Object, Object> removalNotification) {
                        System.out.println(removalNotification.getKey()+" is removed "+ removalNotification.getCause());
                    }
                })
                .build(new CacheLoader<Integer, Integer>() {
            @Override
            public Integer load(Integer integer) throws Exception {
                return sourceMap.get(integer);
            }

            @Override
            public ListenableFuture<Integer> reload(Integer key, Integer oldValue) throws Exception {
                System.out.println("reload====================");
                ListenableFutureTask<Integer> listenableFutureTask = ListenableFutureTask
                        .create(new Callable<Integer>() {
                            @Override
                            public Integer call() throws Exception {
                                return sourceMap.get(key);
                            }
                        });
                executorService.execute(listenableFutureTask);
                return listenableFutureTask;
            }

            @Override
            public Map<Integer, Integer> loadAll(Iterable<? extends Integer> keys) throws Exception {
                List<Integer> IdList = Lists.newArrayList(keys.iterator());
                return IdList.stream().collect(Collectors.toMap(Function.identity(), Function.identity()));
            }

        });


        System.out.println();
        for (int i=0;i<10;i++){
            loadingCache.get(i);
        }
        System.out.println("过期前=="+loadingCache.asMap());
        Thread.sleep(1000*11);
        System.out.println("过期后=="+loadingCache.asMap());
        loadingCache.invalidateAll();
        loadingCache.getAll(ids);
        System.out.println(loadingCache.stats().averageLoadPenalty());
    }

    @Test
    public void demo1() throws ExecutionException, InterruptedException {
        LoadingCache<Integer,Integer> loadingCache= CacheBuilder
                .newBuilder()
                .expireAfterAccess(Duration.ofSeconds(5))
                .build(new CacheLoader<Integer, Integer>() {
                    @Override
                    public Integer load(Integer integer) throws Exception {
                        return sourceMap.get(integer);
                    }
                });
        loadingCache.invalidateAll();
        for (int i=0;i<10;i++){
            loadingCache.get(i);
        }
        System.out.println("过期前=="+loadingCache.asMap());
        Thread.sleep(1000*3);
        System.out.println(loadingCache.asMap());
        Thread.sleep(1000*4);
        loadingCache.put(11, 10);
        System.out.println(loadingCache.asMap()+"==="+loadingCache.getIfPresent(1));
        System.out.println("过期后=="+loadingCache.size());
    }
}
