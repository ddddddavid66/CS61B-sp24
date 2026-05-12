package hashmap;

import java.util.*;

/**
 *  A hash table-backed Map implementation.
 *
 *  Assumes null keys will never be inserted, and does not resize down upon remove().
 *  @author YOUR NAME HERE
 */
public class MyHashMap<K, V> implements Map61B<K, V> {

    /**
     * Protected helper class to store key/value pairs
     * The protected qualifier allows subclass access
     */
    protected class Node {
        K key;
        V value;

        Node(K k, V v) {
            key = k;
            value = v;
        }
    }

    /* Instance Variables */
    private Collection<Node>[] buckets;
    private int initialCapacity = 16;
    private double loadFactor = 0.75;
    private int size;
    // You should probably define some more!

    /** Constructors */
    public MyHashMap() {
        this.buckets = (Collection<Node>[]) new Collection[initialCapacity];
        for (int i = 0; i < initialCapacity; i++) {
            buckets[i] = createBucket();
        }
    }

    public MyHashMap(int initialCapacity) {
        this.initialCapacity = initialCapacity;
        this.buckets = (Collection<Node>[]) new Collection[initialCapacity];
        for (int i = 0; i < initialCapacity; i++) {
            buckets[i] = createBucket();
        }
    }

    /**
     * MyHashMap constructor that creates a backing array of initialCapacity.
     * The load factor (# items / # buckets) should always be <= loadFactor
     *
     * @param initialCapacity initial size of backing array
     * @param loadFactor maximum load factor
     */
    public MyHashMap(int initialCapacity, double loadFactor) {
        this.initialCapacity = initialCapacity;
        this.loadFactor = loadFactor;
        this.buckets = (Collection<Node>[]) new Collection[initialCapacity];
        for (int i = 0; i < initialCapacity; i++) {
            buckets[i] = createBucket();
        }
    }

    /**
     * Returns a data structure to be a hash table bucket
     *
     * The only requirements of a hash table bucket are that we can:
     *  1. Insert items (`add` method)
     *  2. Remove items (`remove` method)
     *  3. Iterate through items (`iterator` method)
     *  Note that that this is referring to the hash table bucket itself,
     *  not the hash map itself.
     *
     * Each of these methods is supported by java.util.Collection,
     * Most data structures in Java inherit from Collection, so we
     * can use almost any data structure as our buckets.
     *
     * Override this method to use different data structures as
     * the underlying bucket type
     *
     * BE SURE TO CALL THIS FACTORY METHOD INSTEAD OF CREATING YOUR
     * OWN BUCKET DATA STRUCTURES WITH THE NEW OPERATOR!
     */
    protected Collection<Node> createBucket() {
        //  Fill in this method.
        return new LinkedList<>(); // 里面存储节点
    }

    //  Implement the methods of the Map61B Interface below
    // Your code won't compile until you do so!

    private int computeIndex(K key){
        return  key.hashCode() & (this.initialCapacity - 1);
    }

    @Override
    public void put(K key, V value) {
        Collection<Node> collection =  this.buckets[computeIndex(key)];
        Iterator<Node> iterator = collection.iterator();
        boolean flag = false;
        while(iterator.hasNext()){
            Node node = iterator.next();
            if(node.key.equals(key)){
                flag = true;
                node.value = value;
                break;
            }
        }
        if(!flag){ //添加
            collection.add(new Node(key,value));
            size++;
        }
        if((double)size / initialCapacity > loadFactor){ //需要扩容
            resize(initialCapacity * 2);
        }
    }

    private void resize(int cap){
        int length = initialCapacity;
        this.initialCapacity = cap;
        Collection<Node>[] temp = (Collection<Node>[]) new Collection[initialCapacity];
        for (int i = 0; i < cap; i++) {
            temp[i] = createBucket();
        }
        for (int i = 0; i < length; i++) {
            Iterator<Node> iterator = buckets[i].iterator();
            while(iterator.hasNext()){
                Node node = iterator.next();
                temp[computeIndex(node.key)].add(node);
            }
        }
        this.buckets = temp; //之前的会被当作垃圾回收
    }

    @Override
    public V get(K key) {
        Node node = getNode(key);
        return node == null? null : node.value;
    }
    private Node getNode(K key){
        Iterator<Node> iterator = this.buckets[computeIndex(key)].iterator();
        while(iterator.hasNext()){
            Node node = iterator.next();
            if(node.key.equals(key)){
                return node;
            }
        }
        return null;
    }

    @Override
    public boolean containsKey(K key) {
        return getNode(key) != null;
    }

    @Override
    public int size() {
        return this.size;
    }

    @Override
    public void clear() {
        for (int i = 0; i < initialCapacity; i++) {
            buckets[i] = createBucket();
        }
        size = 0;
    }

    @Override
    public Set<K> keySet() {
        Set<K> set = new LinkedHashSet<>();
        for (Collection<Node> bucket : buckets) {
            for (Node node : bucket) {
                set.add(node.key);
            }
        }
        return set;
    }

    @Override
    public V remove(K key) {
        Collection<Node> collection =  this.buckets[computeIndex(key)];
        Iterator<Node> iterator = collection.iterator();
        while (iterator.hasNext()){
            Node node = iterator.next();
            if(node.key.equals(key)){
                collection.remove(node);
                size--;
                return node.value;
            }
        }
        return null;
    }

    @Override
    public Iterator<K> iterator() {
        return keySet().iterator();
    }
}
