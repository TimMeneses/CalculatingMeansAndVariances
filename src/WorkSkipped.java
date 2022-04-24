import java.util.ArrayList;

public class WorkSkipped {
    private Integer totalJobs;
    private ArrayList<Double> totalSkipped;
    private Double mean;
    private Double variance;
    private Boolean isProbe;
    private Double max;

    WorkSkipped(Boolean probe) {
        totalSkipped = new ArrayList<>();
        totalJobs = 0;
        mean = 0.0;
        variance = 0.0;
        isProbe = probe;
    }

    public void addToList(Double i) {
        totalSkipped.add(i);
        totalJobs++;
    }

    public void calculate() {
        calculateMean();
        calculateVariance();
        findMax();
    }

    private void calculateMean() {
       Double totalS = 0.0;
        for (Double i: totalSkipped) {
            totalS += i;
        }
        if (isProbe) {
            mean = totalS / ((double) totalJobs - 1);
        } else {
            mean = totalS / ((double) totalJobs - 1);
        }
    }
    
    private void calculateVariance() {
        Double totalVar = 0.0;
        Double current;

        for(Double i: totalSkipped) {
            current = i - mean;  //deviation from the mean.
            current = Math.pow(current, 2); //square each deviation
            totalVar += current;
        }

        variance = totalVar / (double) totalJobs;
    }

    private void findMax() {     
        Double maxWork = 0.0;
        for (Double i: totalSkipped) {
            if(i > maxWork) {
                maxWork = i;
            }
        }
        max = maxWork;
    };



    public Double getMean() {return mean;}
    public Double getVariance() {return variance;};
    public Double getMax() {return max;}
    public Integer getTotalJobs() {return totalJobs;}

}
