package deque;

import java.util.*;

public class ArrayDeque61B<T> implements Deque61B<T> {
    private T[] items;
    private int size;
    private int nextLast; // 下一个addFirst要插入的地方  指向空值
    private int nextFirst; // 下一个addLast要插入的地方  指向空值
    private int arrLength;
    private static final int factor = 2;


    public ArrayDeque61B(){
        items = (T[]) new Object[8];
        size = 0;
        nextFirst = 3;
        nextLast = 4;
        arrLength = items.length;
    }
    // 逻辑第 0 个元素的物理位置： Math.floorMod(nextFirst + 1, arrayLength);
    // 逻辑第 i 个元素的物理位置： int physicalIndex = Math.floorMod(nextFirst + 1 + i, arrayLength);

    public boolean isFull(){ // 必须空一个位置 否则会出错
        return size == arrLength - 1;
    }
    private void resize(int cap){  //扩容
        T[] arr =(T[]) new Object[cap];
        for (int i = 0; i < size; i++) {
            arr[i] = items[Math.floorMod(nextFirst + 1 + i,arrLength)];
        }
        items = arr;
        arrLength = cap;

        nextFirst = cap - 1;
        nextLast = size;
    }

    @Override
    public void addFirst(T x) {
        if(isFull()){
            resize(arrLength * 2);
        }
        items[nextFirst] = x;
        nextFirst = Math.floorMod(nextFirst - 1,arrLength);
        size++;
    }

    @Override
    public void addLast(T x) {
        if(isFull()){
            resize(arrLength * 2);
        }
        items[nextLast] = x;
        nextLast = Math.floorMod(nextLast + 1,arrLength);
        size++;
    }

    @Override
    public List<T> toList() {
        List<T> returnList = new ArrayList<>();
        int i = 0;
        while(i < size){
            returnList.add(items[ Math.floorMod(nextFirst + 1 + i,arrLength)]);
            i++;
        }
        return returnList;
    }

    @Override
    public boolean isEmpty() {
        return size == 0;
    }

    @Override
    public int size() {
        return size;
    }

    @Override
    public T removeFirst() {
        if(size == 0){
            return null;
        }
        T temp = items[Math.floorMod(nextFirst + 1,arrLength)];
        items[Math.floorMod(nextFirst + 1,arrLength)] = null;
        nextFirst = Math.floorMod(nextFirst + 1 ,arrLength);
        size--;
        return temp;
    }

    @Override
    public T removeLast() {
        if(size == 0){
            return null;
        }
        T temp = items[Math.floorMod(nextLast - 1,arrLength)];
        items[Math.floorMod(nextLast - 1,arrLength)] = null;
        nextLast = Math.floorMod(nextLast - 1 ,arrLength);
        size--;
        return temp;
    }

    @Override
    public T get(int index) {  //用户眼里的第几个
        if(index < 0 || index >= size){
            return null;
        }
        return items[Math.floorMod(nextFirst + index + 1,arrLength)];
    }

    @Override
    public T getRecursive(int index) {
        if(index < 0 || index >= size){
            return null;
        }
        return getRecursive(nextFirst + 1,index);
    }
    private T getRecursive(int node,int index){
        if(index == 0){
            return items[Math.floorMod(node,arrLength)];
        }
        return getRecursive(node + 1,index - 1);
    }


    @Override
    public Iterator<T> iterator() {
        return new ArrayDequeIterator();
    }
    class ArrayDequeIterator implements Iterator<T>{
        private int index = 0; //遍历了几个 元素

        @Override
        public boolean hasNext() {
            return index < size;
        }

        @Override
        public T next() {
            if (!hasNext()){
                throw new NoSuchElementException("No more elements in deque.");
            }
            int actual = Math.floorMod(nextFirst + 1 + index, items.length);
            index++;
            return items[actual];
        }
    }

    @Override
    public boolean equals(Object obj){
        if (obj == this){
            return true;
        }
        if (obj == null || !(obj instanceof Deque61B<?>)){
            return false;
        }
        Deque61B<T> deque = (Deque61B<T>) obj;
        if (deque.size() != this.size){
            return false;
        }
        for (int i = 0; i < this.size; i++) {
            T thisItem = this.get(i);
            T otherItem = deque.get(i);
            // Handle nulls safely
            if (thisItem == null && otherItem == null) {
                continue;
            }
            if (thisItem == null || otherItem == null || !thisItem.equals(otherItem)) {
                return false;
            }
        }
        return true;
    }
    @Override
    public String toString() {
        return Arrays.toString(toList().toArray());
    }
}
