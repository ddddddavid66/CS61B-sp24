package main;

import java.util.*;

public class Graph {
    private final int V;
    private List<Integer>[] adj;
    public Graph(int V){ //初始化图
        this.V = V;
        adj = new List[V];
        for (int i = 0; i < V; i++) {
            adj[i] = new ArrayList<>();
        }
    }

    public void addEdge(int v,int w) { //(v,w) 单向的
        isLegal(v);
        isLegal(w);
        adj[v].add(w);
    }
    public List<Integer> adj(int v){
        isLegal(v);
        return adj[v];
    }

    // 实现栈版本的DFS
    public Set<Integer> DFSSingle(int start){
        Set<Integer> visited = new HashSet<>();
        Deque<Integer> stack = new ArrayDeque<>(); // 栈
        stack.push(start);
        while (!stack.isEmpty()){
            Integer pop = stack.pop();
            if(!visited.add(pop)){ // 已经存在就跳过 没有就添加
                continue;
            }
            //没有被添加过  就添加 并且遍历邻居
            for (Integer i : adj(pop)) {
                if(!visited.contains(i)){//那就压栈
                    stack.push(i);
                }
            }
        }
        return visited;
    }

    public Set<Integer> DFSAll(Collection<Integer> starts){
        Set<Integer> visited = new HashSet<>();  //Set 自动去重
        Deque<Integer> stack = new ArrayDeque<>(); // 栈
        for (Integer start : starts) {
            stack.push(start);
        }
        while (!stack.isEmpty()){
            Integer pop = stack.pop();
            if(!visited.add(pop)){ // 已经存在就跳过 没有就添加
                continue;
            }
            //没有被添加过  就添加 并且遍历邻居
            for (Integer i : adj(pop)) {
                if(!visited.contains(i)){//那就压栈
                    stack.push(i);
                }
            }
        }
        return visited;
    }

    private void isLegal(int v){
        if(v < 0 || v >= V){
            throw new IllegalArgumentException("vertex out of bounds: " + v);
        }
    }

}
