import java.util.ArrayList;
import java.util.List;

public class LinkedListDeque61B <T> implements Deque61B<T> {

    public class Node{  //Node 节点
        Node next;
        Node prev;
        T data;
        public Node(T data,Node next,Node prev){
            this.data = data;
            this.next = next;
            this.prev = prev;
        }
    }
    private Node sentinel;
    private int size;

    public LinkedListDeque61B() { //空参构造
        sentinel = new Node(null,null,null);
        sentinel.prev = sentinel;
        sentinel.next = sentinel;
        this.size = 0;
    }



    @Override
    public void addFirst(T x) {  //但是第一次添加是否会出故障？ 哨兵 -> node
        Node temp = sentinel.next;
        Node node = new Node(x,temp,sentinel);
        sentinel.next = node;
        temp.prev = node;
        size++;
    }

    @Override
    public void addLast(T x) { // 哨兵 -> node1 -> node2 -> node3 -> 哨兵（循环回来了）
        Node temp = sentinel.prev;
        Node node = new Node(x,sentinel,temp);
        temp.next = node;
        sentinel.prev = node;
        size++;
    }

    @Override
    public List<T> toList() {
        List<T> returnList = new ArrayList<>(size);
        Node temp = sentinel.next;
        while(temp != sentinel){
            returnList.add(temp.data);
            temp = temp.next;
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
    public T removeFirst() { // 极端情况
        if (size == 0){
            return null;
        }
        Node temp = sentinel.next;
        sentinel.next = temp.next;
        temp.next.prev = sentinel;
        size--;
        return temp.data;
    }

    @Override
    public T removeLast() {
        if (size == 0){
            return null;
        }
        Node temp = sentinel.prev;
        sentinel.prev = temp.prev;
        temp.prev.next = sentinel;
        size--;
        return temp.data;
    }

    @Override
    public T get(int index) {
        if (index + 1 >  size || index < 0){
            return null;
        }
        Node temp = sentinel.next;
        int i = 0;
        while(i < index){
            temp = temp.next;
            i++;
        }
        return temp.data;
    }

    @Override
    public T getRecursive(int index) {
        if (index + 1 >  size || index < 0){
            return null;
        }
        return getRecursive(sentinel.next, index);
    }
    // 写一个helper 帮助我们移动节点
    private T getRecursive(Node node, int index) {
        if (index == 0) {
            return node.data;
        }
        return getRecursive(node.next, index - 1);
    }

}
