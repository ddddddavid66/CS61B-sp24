package main;

import browser.NgordnetQuery;
import browser.NgordnetQueryHandler;
import browser.NgordnetQueryType;
import edu.princeton.cs.algs4.In;
import ngrams.NGramMap;
import ngrams.TimeSeries;

import java.util.*;

public class HyponymsHandler extends NgordnetQueryHandler {
    private WorldNet worldNet;
    private NGramMap map;
    public HyponymsHandler(WorldNet worldNet,NGramMap map) {
        this.worldNet = worldNet;
        this.map = map;
    }

    @Override
    public String handle(NgordnetQuery q) {
        return operation(q,q.ngordnetQueryType());
    }

    private String  operation(NgordnetQuery q,NgordnetQueryType type){
        List<String> words = q.words();
        int k = q.k();
        Set<String> result = new HashSet<>();
        for (String word : words) {
            Set<Integer> result2;
            if(type == NgordnetQueryType.HYPONYMS){
                result2 = worldNet.DFSAll(worldNet.wordToIds(word));
            }else{
                result2 = worldNet.DFSAllReverse(worldNet.wordToIds(word));
            }
            Set<String> words1 = worldNet.idsToWords(result2);
            if (result.isEmpty()) {
                result = words1;
            } else {
                result.retainAll(words1);
            }
        }
        if(k == 0){
            return result.toString();
        } //处理k != 0
        int startYear = q.startYear();
        int endYear = q.endYear();
        // 我们只要前k个次数最多的 所以应该使用 优先级队列
        PriorityQueue<Map.Entry<String,Double>> queue = new PriorityQueue<>(Comparator.comparingDouble(Map.Entry::getValue));
        for (String word : result) {
            List<Double> data = map.countHistory(word, startYear, endYear).data();
            Double count = 0.0;
            for (Double d : data) {
                count += d;
            }
            if(count > 0){ //过滤 count = 0 的
                queue.offer(new AbstractMap.SimpleEntry<>(word, count));
                if(queue.size() > k){ // 如果超过k个 就移除最小的
                    queue.poll();
                }
            }
        }
        //返回的时候 还应该是字典序
        List<String>  r = new ArrayList<>();
        while(!queue.isEmpty()){
            r.add(queue.poll().getKey());
        }
        Collections.sort(r); //字典序排序
        return r.toString();
    }
}
