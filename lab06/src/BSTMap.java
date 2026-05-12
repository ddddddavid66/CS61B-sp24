
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.Set;

public class BSTMap<K extends Comparable<K>,V> implements Map61B<K,V>{
    class Node{
        private K key;
        private V value;
        private Node leftChild;
        private Node rightChild;
        private Node parent;
        public Node(K key,V value,Node leftChild,Node rightChild,Node parent){
            this.key = key;
            this.value = value;
            this.leftChild = leftChild;
            this.rightChild = rightChild;
            this.parent = parent;
        }
    }

    private Node root;
    private int size;

    @Override
    public void put(K key, V value) {
        root = put(root,key,value,null);
    }

    private Node put(Node node,K key,V value,Node parent){
        if (node == null){ //该插入了
            size++;
            return new Node(key,value,null,null,parent);
        }
        int cmp = node.key.compareTo(key);
        if (cmp > 0){ //node key 大
            node.leftChild = put(node.leftChild,key,value,node);
        }else if (cmp < 0){
            node.rightChild = put(node.rightChild,key,value,node);
        }else{
            node.value = value;
        }
        return node;
    }

    @Override
    public V get(K key) {
        Node node = getNode(root, key);
        return node == null ? null : node.value;
    }

    private Node getNode(Node node,K key){
        if(node == null){
            return null;
        }
        int cmp = node.key.compareTo(key);
        if (cmp > 0){ // node 大
            node = getNode(node.leftChild,key);
        }else if (cmp < 0){
            node = getNode(node.rightChild,key);
        }else{
            return node;
        }
        return node;
    }

    @Override
    public boolean containsKey(K key) {
        return getNode(root,key) != null;
    }

    @Override
    public int size() {
        return this.size;
    }

    @Override
    public void clear() {
        root = null;  //交给jvm回收垃圾
        this.size = 0;
    }

    @Override
    public Set<K> keySet() {
        Set<K> set = new LinkedHashSet<>();
        addKeys(root,set);
        return set;
    }

    private void addKeys(Node node,Set<K> set){
        if (node == null){
            return;
        }
        addKeys(node.leftChild,set);
        set.add(node.key);
        addKeys(node.rightChild,set);
    }

    @Override
    public V remove(K key) {
        Node target = getNode(root, key);
        if (target == null) {
            return null;
        }
        root = remove(root, key);
        V oldValue = target.value;
        size--;
       return oldValue;
    }

    private Node remove(Node node,K key){
        // 找predecessor 前者 左子树中最大的 也就是右子节点到尽头
        // 找 successor  右子树中最小的  也就是 左子节点到尽头
        if(node == null){
            return null;
        }
        int cmp = node.key.compareTo(key);
        if (cmp > 0){
            node.leftChild = remove(node.leftChild,key);
        }else if (cmp < 0){
            node.rightChild = remove(node.rightChild,key);
        }else{
            if(node.leftChild == null){ // 自动处理了没有孩子的事情
                return node.rightChild;
            }
            if (node.rightChild == null){
                return node.leftChild;
            }
            // 两个孩子
            Node temp = node;
            Node successor = getMin(temp.rightChild);
            // successor 需要接上 左右节点
            successor.rightChild = removeMin(temp.rightChild);
            successor.leftChild = temp.leftChild;
            node = successor;
        }
        return node;
    }

    private Node getMin(Node node){
        while(node.leftChild != null){
            node = node.leftChild;
        }
        return node;
    }

    private Node removeMin(Node node){ //需要删除 最小节点 也就是删除最左边的节点
        if(node.leftChild == null){
            return node.rightChild;
        }
        node.leftChild = removeMin(node.leftChild);
        return node;
    }

    @Override
    public Iterator<K> iterator() {
        return keySet().iterator();
    }
}
