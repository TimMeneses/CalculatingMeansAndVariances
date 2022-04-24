import java.util.ArrayList;

public class Swaps {
    

    private Integer totalJobs;
    private ArrayList<Integer> swaps;
    private Double mean;
    private Double variance;
    private Boolean isProbe;
    private Integer max;

    Swaps(Boolean probe) {
        swaps = new ArrayList<>();
        totalJobs = 0;
        mean = 0.0;
        variance = 0.0;
        isProbe = probe;
    }

    public void addToList(Integer i) {
        if(i < 0) {
            swaps.add(0);
        } else {
            swaps.add(i);
        }
       totalJobs++;
    }

    public void calculate() {
        calculateMean();
        calculateVariance();
        findMax();
    }

    private void calculateMean() {
        Integer totalSwaps = 0;
        for (Integer i: swaps) {
            totalSwaps += i;
        }
        mean = ((double) totalSwaps) / ((double) totalJobs - 1);
    
        System.out.println("TOTAL LARGE JOBS: " + totalJobs);
        System.out.println("TOTAL SWAPS FROM THE LARGE JOBS: " + totalSwaps);
    
    
    
    }
    
    private void calculateVariance() {
        Double totalVar = 0.0;
        Double current;

        for(Integer i: swaps) {
            current = ((double) i) - mean;  //deviation from the mean.
            current = Math.pow(current, 2); //square each deviation
            totalVar += current;
        }

        variance = totalVar / totalJobs;
    }

    private void findMax() {     
        Integer maxSwaps = 0;
        for (Integer i: swaps) {
            if(i > maxSwaps) {
                maxSwaps = i;
            }
        }
        max = maxSwaps;
    };



    public Double getMean() {return mean;}
    public Double getVariance() {return variance;};
    public Integer getMax() {return max;}
    public Integer getTotalJobs() {return totalJobs;}

}
