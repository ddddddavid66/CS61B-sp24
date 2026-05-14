package ngrams;

import java.util.*;

/**
 * An object for mapping a year number (e.g. 1996) to numerical data. Provides
 * utility methods useful for data analysis.
 *
 * @author Josh Hug
 */
public class TimeSeries extends TreeMap<Integer, Double> {

    /** If it helps speed up your code, you can assume year arguments to your NGramMap
     * are between 1400 and 2100. We've stored these values as the constants
     * MIN_YEAR and MAX_YEAR here. */
    public static final int MIN_YEAR = 1400;
    public static final int MAX_YEAR = 2100;

    /**
     * Constructs a new empty TimeSeries.
     */
    public TimeSeries() {
        super();
    }

    /**
     * Creates a copy of TS, but only between STARTYEAR and ENDYEAR,
     * inclusive of both end points.
     */
    public TimeSeries(TimeSeries ts, int startYear, int endYear) {
        super();
        for (Map.Entry<Integer, Double> entry : ts.entrySet()) {
            Integer year = entry.getKey();
            if(year >= startYear && year <= endYear){
                put(year,entry.getValue());
            }
        }
    }

    /**
     * Returns all years for this TimeSeries (in any order).
     */
    public List<Integer> years() {
        List<Integer> list = new ArrayList<>();
        for (Map.Entry<Integer, Double> entry : entrySet()) {
            list.add(entry.getKey());
        }
        return list;
    }

    /**
     * Returns all data for this TimeSeries (in any order).
     * Must be in the same order as years().
     */
    public List<Double> data() {
        List<Double> list = new ArrayList<>();
        for (Map.Entry<Integer, Double> entry : entrySet()) {
            list.add(entry.getValue());
        }
        return list;
    }

    /**
     * Returns the year-wise sum of this TimeSeries with the given TS. In other words, for
     * each year, sum the data from this TimeSeries with the data from TS. Should return a
     * new TimeSeries (does not modify this TimeSeries).
     *
     * If both TimeSeries don't contain any years, return an empty TimeSeries.
     * If one TimeSeries contains a year that the other one doesn't, the returned TimeSeries
     * should store the value from the TimeSeries that contains that year.
     */
    public TimeSeries plus(TimeSeries ts) {
        TimeSeries result = copy();
        Set<Map.Entry<Integer, Double>> entries2 = ts.entrySet();
        for (Map.Entry<Integer, Double> entry : entries2) {
            int year = entry.getKey();
            Double data = get(year);
            if(data != null){
                result.put(year,data + entry.getValue());
            }else{
                result.put(year,entry.getValue());
            }
        }
        return result;
    }

    /**
     * Returns the quotient of the value for each year this TimeSeries divided by the
     * value for the same year in TS. Should return a new TimeSeries (does not modify this
     * TimeSeries).
     *
     * If TS is missing a year that exists in this TimeSeries, throw an
     * IllegalArgumentException.
     * If TS has a year that is not in this TimeSeries, ignore it.
     */
    public TimeSeries dividedBy(TimeSeries ts) {
        TimeSeries result = copy();
        for (Map.Entry<Integer, Double> entry : entrySet()) {
            int year = entry.getKey();
            if (!ts.containsKey(year)){
                throw new IllegalArgumentException();
            }
            result.put(year,entry.getValue() / ts.get(year));
        }
        return result;
    }

    private TimeSeries copy(){
        TimeSeries result = new TimeSeries();
        Set<Map.Entry<Integer, Double>> entries = entrySet();
        for (Map.Entry<Integer, Double> entry : entries) {
            result.put(entry.getKey(),entry.getValue());
        }
        return result;
    }
}
