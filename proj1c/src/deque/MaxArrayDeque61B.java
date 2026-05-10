package deque;

import java.util.Comparator;

public class MaxArrayDeque61B<T> extends ArrayDeque61B<T>{
    private Comparator<T> comparator;

    public MaxArrayDeque61B(Comparator<T> cmp){
        super();
        this.comparator =  cmp;
    }

    public T max(){
        return max(comparator);
    }

    public T max(Comparator<T> cmp){
        if (isEmpty()) {
            return null;
        }
        T maxItem = get(0);
        for (int i = 1; i < size(); i++) {
            T cur = get(i);
            if (cmp.compare(cur, maxItem) > 0) {
                maxItem = cur;
            }
        }
        return maxItem;
    }

}
