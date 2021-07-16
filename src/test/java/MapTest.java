import java.util.*;

public class MapTest {
    public static Map<Integer,Integer> sortLow(Map<Integer,Integer> map){
        List<Map.Entry<Integer,Integer>> list = new ArrayList<>(map.entrySet());
        Collections.sort(list, new Comparator<Map.Entry<Integer, Integer>>() {
            @Override
            public int compare(Map.Entry<Integer, Integer> o1, Map.Entry<Integer, Integer> o2) {
                return o2.getValue()- o1.getValue();
            }
        });
        Map<Integer,Integer> returnMap = new LinkedHashMap<>();
        for (Map.Entry<Integer,Integer> entry:list){
            returnMap.put(entry.getKey(), entry.getValue());
        }
        return returnMap;
    }

    public static Map<Integer,Integer> sortUp(Map<Integer,Integer> map){
        List<Map.Entry<Integer,Integer>> list = new ArrayList<>(map.entrySet());
        Collections.sort(list, new Comparator<Map.Entry<Integer, Integer>>() {
            @Override
            public int compare(Map.Entry<Integer, Integer> o1, Map.Entry<Integer, Integer> o2) {
                return o1.getValue()- o2.getValue();
            }
        });
        Map<Integer,Integer> returnMap = new LinkedHashMap<>();
        for (Map.Entry<Integer,Integer> entry:list){
            returnMap.put(entry.getKey(), entry.getValue());
        }
        return returnMap;
    }

    public static void main(String[] args) {
        Map<Integer,Integer> map = new HashMap<>();
        map.put(1, 2);
        map.put(2, 5);
        map.put(3, 4);
        map.put(4, 1);
        System.out.println(map);
        System.out.println("由大到小："+sortLow(map));
        System.out.println("由小到大："+sortUp(map));
        map.put(7, 8);
        System.out.println(map);
        System.out.println(sortLow(map));
    }
}
