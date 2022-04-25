import java.util.ArrayList;
import java.util.Collections;

public class PercentileCalc {
    
    private ArrayList <Double> responseArr = new ArrayList<>();
    private ArrayList <Double> slowDownArr = new ArrayList<>();
    private ArrayList <Integer> numberOfSwapsArr = new ArrayList<>();
    private ArrayList <Double> workNudgedArr = new ArrayList<>();
    private int totalJobs = 0;
    private double percentile;

    PercentileCalc(int percent) {
        percentile = (double) percent;
        percentile /= 100;
    }


    public void addToList(Double responseTime, Double slowDown, Integer swaps, Double workNudged) {
        responseArr.add(responseTime);
        slowDownArr.add(slowDown);
        numberOfSwapsArr.add(swaps);
        workNudgedArr.add(workNudged);
        totalJobs++;
    }

    public void sortLists() {
        Collections.sort(responseArr, Collections.reverseOrder());
        Collections.sort(slowDownArr, Collections.reverseOrder());
        Collections.sort(numberOfSwapsArr, Collections.reverseOrder());
        Collections.sort(workNudgedArr, Collections.reverseOrder());
        
    }


    public String calculateWorkNudged() {
        Double totalWorkNudgeForPercentile = 0.0;
        Double totalWorkNudgeForRest = 0.0;
        Integer percentileTotalJobs =  (int) (totalJobs * percentile);
        Integer remainingJobs = totalJobs - percentileTotalJobs;

        int tj = 0;

        while(tj < percentileTotalJobs){
            totalWorkNudgeForPercentile += workNudgedArr.get(tj);
            tj++;
        }


        while(tj < totalJobs) {
            totalWorkNudgeForRest += workNudgedArr.get(tj);
            tj++;
        }

        Double meanPercentile = totalWorkNudgeForPercentile / (double) percentileTotalJobs;
        Double meanForTheRest = totalWorkNudgeForRest / (double) remainingJobs;

       /**CALCULATING VARIANCE--------------------------------------------*/
        Double totalVarForPercentile = 0.0;
        Double totalVarForRest = 0.0;
        tj = 0;

        Double current;
        while(tj < percentileTotalJobs) {
            current = workNudgedArr.get(tj) - meanPercentile;
            current = Math.pow(current, 2);
            totalVarForPercentile += current;
            tj++;
        }

        while(tj < totalJobs) {
            current = workNudgedArr.get(tj) - meanForTheRest;
            current = Math.pow(current, 2);
            totalVarForRest += current;
            tj++;
        }

        Double varPercentile = totalVarForPercentile / percentileTotalJobs;
        Double varForTheRest = totalVarForRest / (double) remainingJobs;

        return meanPercentile + "\t\t" + varPercentile + "\t\t" + percentileTotalJobs + "\t\t" + meanForTheRest + "\t\t" + varForTheRest + "\t\t" + remainingJobs + "\n";
    }

    public String calculateSwaps() {
        Integer totalSwapsForPercentile = 0;
        Integer totalSwapsForRest = 0;
        Integer percentileTotalJobs =  (int) (totalJobs * percentile);
        Integer remainingJobs = totalJobs - percentileTotalJobs;

        int tj = 0;

        while(tj < percentileTotalJobs){
            totalSwapsForPercentile += numberOfSwapsArr.get(tj);
            tj++;
        }


        while(tj < totalJobs) {
            totalSwapsForRest += numberOfSwapsArr.get(tj);
            tj++;
        }

        Double meanPercentile = (double) totalSwapsForPercentile / (double) percentileTotalJobs;
        Double meanForTheRest = (double) totalSwapsForRest / (double) remainingJobs;

       /**CALCULATING VARIANCE--------------------------------------------*/
        Double totalVarForPercentile = 0.0;
        Double totalVarForRest = 0.0;
        tj = 0;

        Double current;
        while(tj < percentileTotalJobs) {
            current = numberOfSwapsArr.get(tj) - meanPercentile;
            current = Math.pow(current, 2);
            totalVarForPercentile += current;
            tj++;
        }

        while(tj < totalJobs) {
            current = numberOfSwapsArr.get(tj) - meanForTheRest;
            current = Math.pow(current, 2);
            totalVarForRest += current;
            tj++;
        }

        Double varPercentile = totalVarForPercentile / percentileTotalJobs;
        Double varForTheRest = totalVarForRest / (double) remainingJobs;

        return meanPercentile + "\t\t" + varPercentile + "\t\t" + percentileTotalJobs + "\t\t" + meanForTheRest + "\t\t" + varForTheRest + "\t\t" + remainingJobs + "\n";
    }



    public String calculateSlowDown() {
        Double totalSDForPercentile = 0.0;
        Double totalSDForRest = 0.0;
        Integer percentileTotalJobs =  (int) (totalJobs * percentile);
        Integer remainingJobs = totalJobs - percentileTotalJobs;

        int tj = 0;

        while(tj < percentileTotalJobs){
            totalSDForPercentile += slowDownArr.get(tj);
            tj++;
        }


        while(tj < totalJobs) {
            totalSDForRest += slowDownArr.get(tj);
            tj++;
        }

        Double meanPercentile = totalSDForPercentile / (double) percentileTotalJobs;
        Double meanForTheRest = totalSDForRest / (double) remainingJobs;

       /**CALCULATING VARIANCE--------------------------------------------*/
        Double totalVarForPercentile = 0.0;
        Double totalVarForRest = 0.0;
        tj = 0;

        Double current;
        while(tj < percentileTotalJobs) {
            current = slowDownArr.get(tj) - meanPercentile;
            current = Math.pow(current, 2);
            totalVarForPercentile += current;
            tj++;
        }

        while(tj < totalJobs) {
            current = slowDownArr.get(tj) - meanForTheRest;
            current = Math.pow(current, 2);
            totalVarForRest += current;
            tj++;
        }

        Double varPercentile = totalVarForPercentile / percentileTotalJobs;
        Double varForTheRest = totalVarForRest / (double) remainingJobs;

        return meanPercentile + "\t\t" + varPercentile + "\t\t" + percentileTotalJobs + "\t\t" + meanForTheRest + "\t\t" + varForTheRest + "\t\t" + remainingJobs + "\n";
    }

    public String calculateResponse() {
        Double totalResponseTimeForPercentile = 0.0;
        Double totalResponseTimeForRest = 0.0;
        Integer percentileTotalJobs =  (int) (totalJobs * percentile);
        Integer remainingJobs = totalJobs - percentileTotalJobs;

        int tj = 0;

        while(tj < percentileTotalJobs){
            totalResponseTimeForPercentile += responseArr.get(tj);
            tj++;
        }


        while(tj < totalJobs) {
            totalResponseTimeForRest += responseArr.get(tj);
            tj++;
        }

        Double meanPercentile = totalResponseTimeForPercentile / (double) percentileTotalJobs;
        Double meanForTheRest = totalResponseTimeForRest / (double) remainingJobs;

       /**CALCULATING VARIANCE--------------------------------------------*/
        Double totalVarForPercentile = 0.0;
        Double totalVarForRest = 0.0;
        tj = 0;

        Double current;
        while(tj < percentileTotalJobs) {
            current = responseArr.get(tj) - meanPercentile;
            current = Math.pow(current, 2);
            totalVarForPercentile += current;
            tj++;
        }

        while(tj < totalJobs) {
            current = responseArr.get(tj) - meanForTheRest;
            current = Math.pow(current, 2);
            totalVarForRest += current;
            tj++;
        }

        Double varPercentile = totalVarForPercentile / percentileTotalJobs;
        Double varForTheRest = totalVarForRest / (double) remainingJobs;

        return meanPercentile + "\t\t" + varPercentile + "\t\t" + percentileTotalJobs + "\t\t" + meanForTheRest + "\t\t" + varForTheRest + "\t\t" + remainingJobs + "\n";
    }










}
