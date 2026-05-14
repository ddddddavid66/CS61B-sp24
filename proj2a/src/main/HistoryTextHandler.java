package main;

import browser.NgordnetQuery;
import browser.NgordnetQueryHandler;
import ngrams.NGramMap;
import ngrams.TimeSeries;

import java.util.List;

public class HistoryTextHandler extends NgordnetQueryHandler {
    private NGramMap map;
    public HistoryTextHandler(NGramMap map) {
        this.map = map;
    }

    @Override
    public String handle(NgordnetQuery q) {
        List<String> words = q.words();
        int startYear = q.startYear();
        int endYear = q.endYear();
        StringBuilder sb = new StringBuilder();
        for (String word : words) {
            sb.append(word);
            sb.append(": {");
            boolean flag = true;
            TimeSeries timeSeries = map.weightHistory(word, startYear, endYear);
            for(int i = startYear;i <= endYear;i++){
                Double data = timeSeries.get(i);
                if(data == null){
                    continue;
                }
                if(!flag){  // 解决多余,问题
                    sb.append(", ");
                }
                sb.append(i);
                sb.append("=");
                sb.append(data);
                flag = false;
            }
            sb.append("}");
        }
        return sb.toString();
    }
}
