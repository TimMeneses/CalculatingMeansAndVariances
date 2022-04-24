import java.util.ArrayList;

public class Job {
    private Double probeSize;
    Swaps swap;
    WorkNudged nudged;
    WorkSkipped skipped;

    Job(Boolean isProbe) {
        swap = new Swaps(isProbe);
        nudged = new WorkNudged(isProbe);
        skipped = new WorkSkipped(isProbe);
    }

    public void addToList(Double threshold, Double size, Integer s, Double sk, Double n) {
        if(size < threshold) {
            skipped.addToList(sk);
        }
        else {
            swap.addToList(s);
            nudged.addToList(n);
        }
    }

    public void calculate() {
        swap.calculate();
        skipped.calculate();
        nudged.calculate();
    }

    public String SwapOutput() {
        return "Swaps            " + swap.getMean() + "                 " + swap.getVariance() + "               " + swap.getMax() + "                 " + swap.getTotalJobs() + "\n";
    }

    public String SkippedOutput() {
        return "WorkSkipped            " + skipped.getMean() + "                 " + skipped.getVariance() + "               " + skipped.getMax() + "                 " + skipped.getTotalJobs() + "\n";
    }

    public String NudgedOutput() {
        return "WorkNudged    " + nudged.getMean() + "                " + nudged.getVariance() + "              " + nudged.getMax() + "               " + nudged.getTotalJobs();
    }

    public String ProbeOutput() {
        return "        " + swap.getMean() + "       " + swap.getVariance() + "        " + swap.getMax() + "    " + skipped.getMean() + "       " + skipped.getVariance() + "      " + skipped.getMax() + "        " + nudged.getMean() + "      " + nudged.getVariance() + "      " + nudged.getMax() + "      " + nudged.getTotalJobs() + "\n";
    }

    public void setProbeSize(Double pb) {
        probeSize = pb;
    }

    public Double getProbeSize() {
        return probeSize;
    }

}
