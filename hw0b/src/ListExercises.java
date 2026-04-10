
import java.util.ArrayList;
import java.util.List;

public class ListExercises {

    /** Returns the total sum in a list of integers */
	public static int sum(List<Integer> L) {
        int sum = 0;
        for (Integer i : L) {
            sum += i;
        }
        return L == null ? 0 : sum ;
    }

    /** Returns a list containing the even numbers of the given list */
    public static List<Integer> evens(List<Integer> L) {
        List<Integer> evensList = new ArrayList<>();
        for (Integer i : L) {
            if(i % 2 == 0){
                evensList.add(i);
            }
        }
        return evensList;
    }

    /** Returns a list containing the common item of the two given lists */
    public static List<Integer> common(List<Integer> L1, List<Integer> L2) {
/*        Map<Integer,Integer> map = new HashMap<>();
        for (Integer i : L1) {
                map.put(i,1);
        }
        for (Integer i : L2) {
            if(map.containsKey(i)){
                map.put(i,map.get(i) + 1);
            }
        }
        Set<Integer> set = map.keySet();
        List<Integer> commonList = new ArrayList<>();
        for (Integer i : set) {
            if(map.get(i) > 1){
                commonList.add(i);
            }
        }*/
        List<Integer> commonList = new ArrayList<>();
        for (Integer i : L1) {
            if(L2.contains(i) && !commonList.contains(i)) commonList.add(i);
        }
        return commonList;
    }


    /** Returns the number of occurrences of the given character in a list of strings. */
    public static int countOccurrencesOfC(List<String> words, char c) {
        int count = 0;
        for (String word : words) {
            char[] charArray = word.toCharArray();
            for (char c1 : charArray) {
                if(c1 == c) count++;
            }
        }
        return count;
    }
}
