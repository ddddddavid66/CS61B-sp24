public class UnionFind {
    // TODO: 实例变量
    private int[] parent;

    /* Creates a UnionFind data structure holding N items. Initially, all
       items are in disjoint sets.
       创建一个包含 N 个元素的 UnionFind 数据结构。初始时，所有元素都在互不相交的集合中。 */
    public UnionFind(int N) {
        parent = new int[N];
        for (int i = 0; i < N; i++) {
            parent[i] = -1;
        }
    }

    /* Returns the size of the set V belongs to.
       返回 V 所属集合的大小。 */
    public int sizeOf(int v) {
        while(parent[v] > 0){
            return v;
        }

        return -parent[v];
    }

    /* Returns the parent of V. If V is the root of a tree, returns the
       negative size of the tree for which V is the root.
       返回 V 的父节点。如果 V 是一棵树的根节点，则返回以 V 为根的树的大小的负数。 */
    public int parent(int v) {
        return parent[v];
    }

    /* Returns true if nodes/vertices V1 and V2 are connected.
       如果节点/顶点 V1 和 V2 已连接，则返回 true。 */
    public boolean connected(int v1, int v2) {
        return find(v1) == find(v2);
    }

    /* Returns the root of the set V belongs to. Path-compression is employed
       allowing for fast search-time. If invalid items are passed into this
       function, throw an IllegalArgumentException.
       返回 V 所属集合的根节点。
       如果向该函数传入无效元素，则抛出 IllegalArgumentException。 */
    //throw new IllegalArgumentException("Some comment to describe the reason for throwing.");
    public int find(int v) {
        // TODO 这里使用路径压缩，以便快速查找。
        if(v > parent.length || v < 0){
            throw new IllegalArgumentException("Some comment to describe the reason for throwing.");
        }
        while(parent[v] < 0){
            //v = parent[v];
            //路径压缩
            return v;
        }
        parent[v] = find(parent[v]);
        return parent[v];
    }

    /* Connects two items V1 and V2 together by connecting their respective
       sets. V1 and V2 can be any element, and a union-by-size heuristic is
       used. If the sizes of the sets are equal, tie break by connecting V1's
       root to V2's root. Union-ing an item with itself or items that are
       already connected should not change the structure.
       通过连接 V1 和 V2 各自所属的集合，将这两个元素连接起来。
       V1 和 V2 可以是任意元素，并使用按大小合并的启发式策略。
       如果两个集合的大小相等，则通过将 V1 的根节点连接到 V2 的根节点来打破平局。
       将一个元素与自身合并，或合并已经连接的元素，都不应改变结构。 */
    public void union(int v1, int v2) {
        // TODO: 在这里编写你的代码
        int root1  = find(v1);
        int root2  = find(v2);
        if(root2 == root1){
            return;
        }
        int size1 = -parent[root1];
        int size2 = -parent[root2];
        if(size1 > size2){
            parent[root2] = root1;
            parent[root1] -= size2;
        }else{
            parent[root1] = root2;
            parent[root2] -= size1;
        }
    }

}
