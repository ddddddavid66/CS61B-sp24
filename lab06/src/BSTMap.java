import javax.crypto.KEM;
import java.util.*;

public class BSTMap<K extends Comparable<K>,V> implements Map61B<K,V> {
    class Node{
        private K key;
        private V value;
        private Node leftChild;
        private Node rightChild;
        private Node parent;
        public Node(K key,V value,Node leftChild,Node rightChild,Node parent){
            this.key = key;
            this.value = value;
            this.rightChild = rightChild;
            this.leftChild = leftChild;
            this.parent = parent;
        }
    }
    private Node root;
    private int size;



    @Override
    public void put(K key, V value) { //key value 关联
        root = put(root,key,value,null);
    }

    private Node put(Node node,K key,V value,Node parent){
        if (node == null){ // 找到了自己的位置
            size++;
            return new Node(key,value,null,null,parent);
        }
        int cmp = key.compareTo(node.key);
        if(cmp > 0) {
            node.rightChild = put(node.rightChild,key,value,node);
        }else if (cmp < 0){
            node.leftChild  = put(node.leftChild,key,value,node);
        }else{ //cmp =0 更新value
            node.value = value;
        }
        return node;
    }

    @Override
    public V get(K key) {
        return get(root,key);
    }

    private V get(Node node,K key){
        if (node == null){
            return null;
        }
        int cmp = key.compareTo(node.key);
        if (cmp > 0){
            return get(node.rightChild,key);
        }else if(cmp < 0){
            return get(node.leftChild,key);
        }else{
            return node.value;
        }
    }

    private Node getNode(Node node,K key){
        if (node == null){
            return null;
        }
        int cmp = key.compareTo(node.key);
        if (cmp > 0){
            return getNode(node.rightChild,key);
        }else if(cmp < 0){
            return getNode(node.leftChild,key);
        }else{
            return node;
        }
    }

    @Override
    public boolean containsKey(K key) {//直接写get是错的 因为get返回的是值 而判断是否存在关键在于键
        return containsKey(root,key);
    }

    private boolean containsKey(Node node ,K key){
        if (node == null){
            return false;
        }
        int cmp = key.compareTo(node.key);
        if (cmp > 0){
            return containsKey(node.rightChild,key);
        }else if(cmp < 0){
            return containsKey(node.leftChild,key);
        }else{
            return true;
        }
    }

    @Override
    public int size() {
        return this.size;
    }

    @Override
    public void clear() {
        /*clear(root);
        root = null;*/
        //实际上java有垃圾回收
        root = null;
        size = 0;
    }

    private void clear(Node node){
        if (node == null){
            return;
        }
        if (node.leftChild != null){
            clear(node.leftChild);
        }
        if(node.rightChild != null){
            clear(node.rightChild);
        }
        size--;
        node.leftChild = null;
        node.rightChild = null;
    }

    @Override
    public Set<K> keySet() { // 这个是插入顺序  必须在插入的时候 实现
        Set<K> set = new LinkedHashSet<>();
        inOrder(root,set);
        return set;
    }

    private void inOrder(Node node,Set<K> set){
        if (node == null){
            return;
        }
        inOrder(node.leftChild,set);
        set.add(node.key);
        inOrder(node.rightChild,set);
    }


    @Override
    public V remove(K key) { // 删除key
        // 找到predecessor 根节点的左子节点的最大节点 并且必须没有孩子 或者只能有一个 左子节点
        // 或者 successor 根节点的右子节点的最小值 必须没有子节点 或者只有一个 右子节点
        Node target = getNode(root,key);
        if (target == null){
            return null;
        }
        V value = target.value;
        root = remove(root,key,null);
        if (root != null) {
            root.parent = null;
        }
        size--;
        return value;
    }
    private Node remove(Node node,K key,Node parent){
        if (node == null){
            return null;
        }
        int cmp = node.key.compareTo(key);
        if(cmp > 0){ //node 大
            node.leftChild = remove(node.leftChild,key,node);
        }else if (cmp < 0){
            node.rightChild = remove(node.rightChild,key,node);
        }else{ //cmp = 0
            //如果 只有 一个做左子节点
            if(node.rightChild == null){
                Node left = node.leftChild;
                if (left != null){
                    left.parent = parent;
                }
                return left;
            }
            if (node.leftChild == null){
                Node right = node.rightChild;
                if (right != null){
                    right.parent = parent; //顶替node的位置
                }
                return right;
            }
            //如果两个孩子 那么就找一个 predecessor 左子节点的最大节点
            Node predecessor = findPredecessor(node);
            node.key = predecessor.key;
            node.value = predecessor.value;
            node.leftChild = removeMax(node.leftChild,node);
        }
        node.parent = parent;
        return node;
    }

    private Node findPredecessor(Node node){
        node = node.leftChild;
        while(node.rightChild != null){
            node = node.rightChild;
        }
        return node;
    }
    private Node removeMax(Node node,Node parent){
        if (node.rightChild == null){
            Node left = node.leftChild;
            if(left != null){
                left.parent = parent;
            }
            return left;
        }
        node.rightChild = removeMax(node.rightChild, node);
        node.parent = parent;
        return node;
    }


    @Override
    public Iterator<K> iterator() { //中序迭代
        List<K> keys = new ArrayList<>();
        iterator(root,keys);
        return keys.iterator();
    }

    private void iterator(Node node,List<K> keys){
        if (node == null){
            return;
        }
        iterator(node.leftChild,keys);
        keys.add(node.key);
        iterator(node.rightChild,keys);
    }


}
