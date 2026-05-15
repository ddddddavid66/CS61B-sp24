package main;

import edu.princeton.cs.algs4.In;

import java.util.*;

public class WorldNet {
    private Graph graph;
    private Graph reverseGraph;
    private  HashMap<String, Set<Integer>> wordToIds;
    private  HashMap<Integer,Set<String>> idToWords;

    public WorldNet(String synsetFile, String hyponymFile ){ //解析两个文件
        wordToIds = new HashMap<>();
        idToWords = new HashMap<>();
        In i = new In(synsetFile); // 解析 synsetFile
        while (i.hasNextLine()) {
            String line = i.readLine();
            String[] split = line.split(",");
            Integer id = Integer.valueOf(split[0]);
            String[] words = split[1].split(" ");
            // idToWords
            Set<String> set = idToWords.get(id);
            if (set == null) {
                set = new TreeSet<>(); // 字典序排序
                idToWords.put(id, set);
            }
            for (String word : words) {
                set.add(word);
                //wordToIds
                Set<Integer> integers = wordToIds.get(word);
                if(integers == null){
                    integers = new HashSet<>();
                    wordToIds.put(word,integers);
                }
                integers.add(id);
            }
        }
        i = new In(hyponymFile); //创建映射关系
        graph = new Graph(idToWords.size()); // 一共 id个size
        reverseGraph = new Graph(idToWords.size());
        while (i.hasNextLine()) {
            String line = i.readLine();
            String[] split = line.split(",");
            Integer id = Integer.valueOf(split[0]);
            String[] ids = Arrays.copyOfRange(split, 1, split.length); // 切片
            for (String s : ids) {
                Integer t = Integer.valueOf(s);
                graph.addEdge(id, t);
                reverseGraph.addEdge(t, id); // 建立反向图
            }
        }
    }

    public Set<Integer> wordsToIds(List<String> words){
        Set<Integer> result = new HashSet<>();
        for (String word : words) {
            Set<Integer> integers = wordToIds.get(word);
            result.addAll(integers);
        }
        return result;
    }
    public Set<Integer> wordToIds(String word){
        Set<Integer> integer = wordToIds.get(word);
        return integer == null ? new HashSet<>():integer;
    }

    public Set<String> idsToWords(Set<Integer> ids){
        Set<String> result = new TreeSet<>();
        for (Integer id : ids) {
            Set<String> strings = idToWords.get(id);
            result.addAll(strings);
        }
        return result;
    }
    public Set<Integer> DFSAll(Collection<Integer> starts){
        return graph.DFSAll(starts);
    }
    public Set<Integer> DFSAllReverse(Collection<Integer> starts){
        return reverseGraph.DFSAll(starts);
    }
}
