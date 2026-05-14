package ngrams;

import edu.princeton.cs.algs4.In;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import static ngrams.TimeSeries.MAX_YEAR;
import static ngrams.TimeSeries.MIN_YEAR;

/**
 * An object that provides utility methods for making queries on the
 * Google NGrams dataset (or a subset thereof).
 *
 * An NGramMap stores pertinent data from a "words file" and a "counts
 * file". It is not a map in the strict sense, but it does provide additional
 * functionality.
 *
 * @author Josh Hug
 */
public class NGramMap {

    private HashMap<String,TimeSeries> map;
    private TimeSeries totalCount;

    /**
     * Constructs an NGramMap from WORDSFILENAME and COUNTSFILENAME.
     */
    public NGramMap(String wordsFilename, String countsFilename) {
        map = new HashMap<>();
        totalCount = new TimeSeries();
        // 读取文件 words 就是 csv 文件 count 就是txt 文件
        In i = new In(wordsFilename);
        while(i.hasNextLine()){
            String nextLine = i.readLine();
            String[] split = nextLine.split("\t");
            String word = split[0];
            Integer year = Integer.valueOf(split[1]);
            Double count = Double.valueOf(split[2]);
            TimeSeries timeSeries = map.get(word);
            if(timeSeries == null){
                timeSeries = new TimeSeries();
               map.put(word,timeSeries);
            }
            timeSeries.put(year,count);
        }
        i = new In(countsFilename);
        while (i.hasNextLine()){
            String nextLine = i.readLine();
            String[] split = nextLine.split(",");
            Integer year = Integer.valueOf(split[0]);
            Double count = Double.valueOf(split[1]);
            Double data = totalCount.get(year);
            if(data == null){
                totalCount.put(year,count);
            }else {
                totalCount.put(year,count + data);
            }
        }
    }

    /**
     * Provides the history of WORD between STARTYEAR and ENDYEAR, inclusive of both ends. The
     * returned TimeSeries should be a copy, not a link to this NGramMap's TimeSeries. In other
     * words, changes made to the object returned by this function should not also affect the
     * NGramMap. This is also known as a "defensive copy". If the word is not in the data files,
     * returns an empty TimeSeries.
     */
    public TimeSeries countHistory(String word, int startYear, int endYear) {
        TimeSeries timeSeries = map.get(word);
        if(timeSeries == null){
            return new TimeSeries();
        }
        return new TimeSeries(timeSeries,startYear,endYear);
    }

    /**
     * Provides the history of WORD. The returned TimeSeries should be a copy, not a link to this
     * NGramMap's TimeSeries. In other words, changes made to the object returned by this function
     * should not also affect the NGramMap. This is also known as a "defensive copy". If the word
     * is not in the data files, returns an empty TimeSeries.
     */
    public TimeSeries countHistory(String word) {
        TimeSeries timeSeries = map.get(word);
        if(timeSeries == null){
            return new TimeSeries();
        }
        return new TimeSeries(timeSeries,MIN_YEAR,MAX_YEAR);
    }

    /**
     * Returns a defensive copy of the total number of words recorded per year in all volumes.
     */
    public TimeSeries totalCountHistory() {
        return  new TimeSeries(totalCount,MIN_YEAR,MAX_YEAR);
    }

    /**
     * Provides a TimeSeries containing the relative frequency per year of WORD between STARTYEAR
     * and ENDYEAR, inclusive of both ends. If the word is not in the data files, returns an empty
     * TimeSeries.
     */
    public TimeSeries weightHistory(String word, int startYear, int endYear) {
        return new TimeSeries(getWeighted(word),startYear,endYear);
    }

    /**
     * Provides a TimeSeries containing the relative frequency per year of WORD compared to all
     * words recorded in that year. If the word is not in the data files, returns an empty
     * TimeSeries.
     */
    public TimeSeries weightHistory(String word) {
        return getWeighted(word);
    }


    /**
     * Provides the summed relative frequency per year of all words in WORDS between STARTYEAR and
     * ENDYEAR, inclusive of both ends. If a word does not exist in this time frame, ignore it
     * rather than throwing an exception.
     */
    public TimeSeries summedWeightHistory(Collection<String> words, int startYear, int endYear) {
        return new TimeSeries(getWeighted(words),startYear,endYear);
    }

    /**
     * Returns the summed relative frequency per year of all words in WORDS. If a word does not
     * exist in this time frame, ignore it rather than throwing an exception.
     */
    public TimeSeries summedWeightHistory(Collection<String> words) {
        return new TimeSeries(getWeighted(words),MIN_YEAR,MAX_YEAR);
    }

    private TimeSeries getWeighted(String word){
        TimeSeries timeSeries = map.get(word);
        if(timeSeries == null){
            return new TimeSeries();
        }
        TimeSeries series = timeSeries.dividedBy(totalCount);
        return series;
    }

    private TimeSeries getWeighted(Collection<String> words){
        TimeSeries series = new TimeSeries();
        for (String word : words) {
            TimeSeries timeSeries = map.get(word);
            if(timeSeries == null){
                continue;
            }
            TimeSeries temp = timeSeries.dividedBy(totalCount);
            series = series.plus(temp);
        }
        return series;
    }


}
