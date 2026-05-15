package main;

import browser.NgordnetQuery;
import browser.NgordnetQueryHandler;
import edu.princeton.cs.algs4.In;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class HyponymsHandler extends NgordnetQueryHandler {
    private WorldNet worldNet;
    public HyponymsHandler(WorldNet worldNet){
        this.worldNet = worldNet;
    }
    @Override
    public String handle(NgordnetQuery q) {
        List<String> words = q.words();
        Set<Integer> intersection = new HashSet<>(); // 取交集
        for (String word : words) {
            Set<Integer> result = worldNet.DFSAll(worldNet.wordToIds(word));
            if(intersection.isEmpty()){
                intersection = result;
            }else{
                intersection.retainAll(result);
            }

        }
        Set<String> word = worldNet.idsToWords(intersection);
        return word.toString();
    }
}
