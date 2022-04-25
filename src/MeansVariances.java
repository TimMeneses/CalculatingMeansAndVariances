import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Map;
import java.util.Scanner;
import java.util.TreeMap;

public class MeansVariances {
    
    private static final String OUTPUTS_DIRECTORY = "D:\\Coding\\c++\\Research\\data\\outputs";
    //private static final String OUTPUTS_DIRECTORY = "E:\\RESEARCH\\Raw Data\\New Experiments\\avg swaps";
    private static final String WL_FILE = "\\workloadreport.txt";
    private static final String PROBE_FILE = "\\probereport.txt";
    private static final String SIM_FILE = "\\simparam.txt";
    private static final String OUTPUT_MEAN_VARIANCE = "\\swapsMeansAndVariance.txt";
    private static final String OUTPUT_PROBE_MEAN_VARIANCE = "\\swapsProbeMeansAndVariance.txt";
    private static final String OUTPUT_SWAP_PROBABILITY = "\\swapsProbability.txt";
    private static final String OUTPUT_WORKNUDGED_PROBABILITY = "\\workNudgedProbability.txt";
    private static final String OUTPUT_PERCENTILE = "\\percentile.txt";
    private static final Double THRESHOLD = 1.0;

    public static void main(String[] args) throws IOException {

        File file = new File(OUTPUTS_DIRECTORY);
        String[] fileList = file.list();

        for(String str: fileList) {
            try {
                //calcMeanAndVariance(OUTPUTS_DIRECTORY + "\\" + str);
                //probabilityWorkNudgedCalc((OUTPUTS_DIRECTORY + "\\" + str));
                //probabilitySwapCalc(OUTPUTS_DIRECTORY + "\\" + str);
                //calcProbes(OUTPUTS_DIRECTORY + "\\" + str);
                calcPercentile(OUTPUTS_DIRECTORY + "\\" + str, 5);
                //renameDir(OUTPUTS_DIRECTORY + "\\" + str);
            } catch (FileNotFoundException e) {
                System.out.println("Couldn't open file");
            }
        }
    }

    private static void calcPercentile(String filePath, int percentile) throws IOException {
        System.out.println("\n\n-----------------------Calculating Percentile " + filePath + "-----------------------------------");
        //open the workload.txt in this folder:
        File file = new File(filePath + WL_FILE);
        Scanner readFile = new Scanner(file);
        
        /*--------------------------------FILE READING -------------------------------*/
        //ignore header.
        readFile.nextLine();
        PercentileCalc percCalc = new PercentileCalc(percentile);

        while(readFile.hasNext()) {
            for (int i = 0; i < 6; i++) {
                readFile.next();// ignore all colums before response time
            }
            Double response = readFile.nextDouble();
            Double slowDown = readFile.nextDouble();
            readFile.next();
            Integer swaps = readFile.nextInt();
            if(swaps < 0) {
                swaps = 0;
            }
            readFile.next();
            Double workNudged = readFile.nextDouble();
            percCalc.addToList(response, slowDown, swaps, workNudged); 
        }
        percCalc.sortLists();

          /**--------------------------------OUTPUT WRITING---------------------------------------------------- */

          File outputFile = new File(filePath + OUTPUT_PERCENTILE);
          BufferedWriter output = new BufferedWriter((new FileWriter(outputFile)));
        
          output.write("RV\t\t\tMean-of-"+percentile+"-percentile" + "\t\tVariance\t\tTotalJobs\t\t" + "Mean-of-"+(100-percentile)+"percentile\t\tVariance\t\tTotalJobs\n");
          output.write("Response-Time\t\t" + percCalc.calculateResponse());
          output.write("SlowDown\t\t" + percCalc.calculateSlowDown());
          output.write("NumberOfNudge\t\t" + percCalc.calculateSwaps());
          output.write("TotalWorkNudged\t\t" + percCalc.calculateWorkNudged());
        
          output.close();
          readFile.close();
    }





/**-----------------------------------------------------------------------------------------------------------------------------------------------------*/
     /**-----------------------------------------------------------------------------------------------------------------------------------------------------*/
     /**-----------------------------------------------------------------------------------------------------------------------------------------------------*/
     private static void probabilityWorkNudgedCalc(String filePath) throws IOException {
        System.out.println("\n\n-----------------------Calculating Probability of Work Nudged " + filePath + "-----------------------------------");
        File file = new File(filePath + WL_FILE);
        Scanner readFile = new Scanner(file);

        /*--------------------------------FILE READING -------------------------------*/
        //ignore header.
        readFile.nextLine();

        Map<Double, Integer> workNudgedCount = new TreeMap<>();
        Double size = 0.0; 
        int totalJobs = 0;
        int totalLargeJobs = 0;
        Double MAX_WORK_NUDGED = 0.0;

        while(readFile.hasNext()) {
            readFile.next();  // ignore job id
            size = readFile.nextDouble();
            if (size < THRESHOLD) {
                totalJobs++;
                for(int i = 0; i < 10; i++) {
                    readFile.next();
                }
            } else {
                //look at the work Nudged.
                for(int i = 0; i < 9; i++) {
                    readFile.next();
                 }
                 Double workNudged = readFile.nextDouble();

                 if (workNudged > MAX_WORK_NUDGED) {
                     MAX_WORK_NUDGED = workNudged;
                 }

                 if (workNudged == 0.0) {
                    workNudgedCount.merge(workNudged,1,Integer::sum);
                 } else {
                    workNudgedCount.merge(Math.ceil(workNudged),1, Integer::sum);
                 }

                totalJobs++;
                totalLargeJobs++;
            }
        }

         /**--------------------------------OUTPUT WRITING---------------------------------------------------- */

         File outputFile = new File(filePath + OUTPUT_WORKNUDGED_PROBABILITY);
         BufferedWriter output = new BufferedWriter((new FileWriter(outputFile)));
 
 
         /**-FILE WRITING------------------------------------------------------- */
         output.write("Total-Jobs: " + totalJobs + "\n");
         output.write("Total-Large-Jobs: " + totalLargeJobs + "\n\n");
         output.write("Max-Work-Nudged: " + MAX_WORK_NUDGED + "\n\n");
 
        output.write("Work-Nudged-Range      Total      Probability\n");

        Double current = 0.0;
        Double previous = 0.0;
        while (current <= Math.ceil(MAX_WORK_NUDGED)) {
            Integer totalCount;
            if (current == 0.0) {
                totalCount = workNudgedCount.get(current);
                output.write("0");
            } else if (current == Math.ceil(MAX_WORK_NUDGED)){
                totalCount = workNudgedCount.get(current);
                output.write(previous + "<X>" + MAX_WORK_NUDGED);
            } else {
                totalCount = workNudgedCount.get(current);
                output.write(previous + "<X>" + current);
                previous = current;
            }

            Double prob = 0.0;
            if (totalCount != null) { 
                prob = (double) totalCount / (double) totalLargeJobs;
            } else {
                prob = 0.0;
            }
            output.write("\t\t\t" + totalCount + "\t\t\t" + prob + "\n");

            current+=1.0;
        }

        output.close();
        readFile.close();
     }





    /**-----------------------------------------------------------------------------------------------------------------------------------------------------*/
    /**-----------------------------------------------------------------------------------------------------------------------------------------------------*/
    /**-----------------------------------------------------------------------------------------------------------------------------------------------------*/
     private static void calcProbes(String filePath) throws IOException {
         System.out.println("\n\n-----------------------Calculating Probes " + filePath + "-----------------------------------");
         //open the workload.txt in this folder:
         File file = new File(filePath + PROBE_FILE);
         Scanner readFile = new Scanner(file);


         //ignore header.
         readFile.nextLine();

         Map<Double, Job> jobProbes = new TreeMap<>();
         Double size = 0.0;

         while(readFile.hasNext()) {
               readFile.next();  // ignore job id

             size = readFile.nextDouble();
             if (jobProbes.containsKey(size)) {
                 Job currentJob = jobProbes.get(size);

                 //ignore all columns up to the number of swaps
                 for(int i = 0; i < 7; i++) {
                     readFile.next();
                 }

                 Integer swap = readFile.nextInt();
                 Double workSkipped = readFile.nextDouble();
                 Double workNudged = readFile.nextDouble();
                 currentJob.addToList(THRESHOLD, size, swap, workSkipped, workNudged);

             } else {
                 Job newJob = new Job(true);
                 newJob.setProbeSize(size);

                 for(int i = 0; i < 7; i++) {
                         readFile.next();
                     }

                Integer swap = readFile.nextInt();
                Double workSkipped = readFile.nextDouble();
                Double workNudged = readFile.nextDouble();
                newJob.addToList(THRESHOLD, size, swap, workSkipped, workNudged);

                jobProbes.put(size, newJob);
             }  
         }


         File outputFile = new File(filePath + OUTPUT_PROBE_MEAN_VARIANCE);
         BufferedWriter output = new BufferedWriter((new FileWriter(outputFile)));

        output.write("Size       MeanSwaps        VarianceSwaps         MaxSwaps         Mean-Work-Skipped          Var-Work-Skipped          Max-Work-Skipped         Mean-Work-Nudged         Var-Work-Nudged          Max-Work-Nudged       No-Of-Samples\n");
        for (Map.Entry<Double, Job> entry : jobProbes.entrySet())  {
             Double key = entry.getKey();
             Job current = entry.getValue();

             System.out.println("Key: " + key);
             current.calculate();
             output.write(key + current.ProbeOutput());


         }

         output.close();
         readFile.close();

     }


     

    /**-----------------------------------------------------------------------------------------------------------------------------------------------------*/
    /**-----------------------------------------------------------------------------------------------------------------------------------------------------*/
    /**-----------------------------------------------------------------------------------------------------------------------------------------------------*/
     private static void probabilitySwapCalc(String filePath) throws IOException {
        System.out.println("\n\n-----------------------Calculating Probability of Swaps " + filePath + "-----------------------------------");
        File file = new File(filePath + WL_FILE);
        Scanner readFile = new Scanner(file);


        /*--------------------------------FILE READING -------------------------------*/
        //ignore header.
        readFile.nextLine();

        //Key:Value
        //Number Of Swaps : value 
        Map<Integer, Integer> swapCount = new TreeMap<>();
        Double size = 0.0;
        int totalJobs = 0;
        int totalLargeJobs = 0;

        while(readFile.hasNext()) {
            readFile.next();  // ignore job id
            size = readFile.nextDouble();
            //if size less than threshold we dont care about it.        
            if (size < THRESHOLD) {
                totalJobs++;
                for(int i = 0; i < 10; i++) {
                    readFile.next();
                }
            } else {
                
                //look at the number of swaps.
                for(int i = 0; i < 7; i++) {
                   readFile.next();
                }
                
                Integer swap = readFile.nextInt();

                swapCount.merge(swap,1,Integer::sum);
                totalJobs++;
                totalLargeJobs++;
                readFile.next();
                readFile.next();
            }
        }


        /**--------------------------------OUTPUT WRITING---------------------------------------------------- */

        File outputFile = new File(filePath + OUTPUT_SWAP_PROBABILITY);
        BufferedWriter output = new BufferedWriter((new FileWriter(outputFile)));

        //find the max swap
        Integer maxSwap = 0;
        for (Map.Entry<Integer, Integer> entry: swapCount.entrySet()) {
            Integer key = entry.getKey();
            if (key > maxSwap) {
                maxSwap = key;
            }
        }

        output.write("Total-Jobs: " + totalJobs + "\n");
        output.write("Total-Large-Jobs: " + totalLargeJobs + "\n\n");

       output.write("Number-of-Swaps       Total      Probability\n");

       Integer current = 0;
       while (current <= maxSwap)  {
            Integer total = swapCount.get(current);
            Double prob;
            if (total != null) { 
                prob = (double) total / (double) totalLargeJobs;
            } else {
                prob = 0.0;
            }
            output.write(current + "\t\t\t\t\t" + total + "\t\t\t" + prob + "\n");

            current++;
        }

        output.close();
        readFile.close();
    }



    /**-----------------------------------------------------------------------------------------------------------------------------------------------------*/
    /**-----------------------------------------------------------------------------------------------------------------------------------------------------*/
    /**-----------------------------------------------------------------------------------------------------------------------------------------------------*/
    private static void calcMeanAndVariance(String filePath) throws IOException {

        System.out.println("\n\n-----------------------Calculating Means and Variances for: " + filePath + "-----------------------------------");
        //open the workload.txt in this folder:
        File file = new File(filePath + WL_FILE);
        Scanner readFile = new Scanner(file);

        /*--------------------------------FILE READING -------------------------------*/
        //ignore header.
        System.out.println(readFile.nextLine());
        Job job = new Job(false);


        while(readFile.hasNext()) {
        // THE SWAPS IS IN COLUMN 9 ignore all other COLUMN
            int COLUMN = 9;
            int i = 0;
            Double size = 0.0;
            for (; i < COLUMN; i++) {
                if (i == 1) {
                    size = readFile.nextDouble();
                }
                else {
                    readFile.next();
                } 
           }
            
            Integer swap = readFile.nextInt();
            Double workSkipped = readFile.nextDouble();
            Double workNudged = readFile.nextDouble();
            job.addToList(THRESHOLD,size, swap, workSkipped, workNudged);

        }
        /*---------------------------------------------------------------------------*/

        job.calculate();

         /*--------------------------------FILE WRITING -------------------------------*/
        
         File outputFile = new File(filePath + OUTPUT_MEAN_VARIANCE);
         BufferedWriter output = new BufferedWriter((new FileWriter(outputFile)));

         output.write("RV                Mean                            Variance                Max-Val                 No-Of-Samples\n");
         output.write(job.SwapOutput());
         output.write(job.SkippedOutput());
         output.write(job.NudgedOutput());
        //  /*---------------------------------------------------------------------------*/

        System.out.println("DONE");

        output.close();
        readFile.close();
    }


    /**-----------------------------------------------------------------------------------------------------------------------------------------------------*/
    /**-----------------------------------------------------------------------------------------------------------------------------------------------------*/
    /**-----------------------------------------------------------------------------------------------------------------------------------------------------*/
    private static void renameDir(String filePath) throws FileNotFoundException {
        File sourceFile = new File(filePath);

                File file = new File(filePath + SIM_FILE);
                Scanner readFile = new Scanner(file);
                //      % Simulation configuration:                   0
                // PATH ..\\..\\data\\                                1
                // SCHEDULER	NUDGE 1 1 1 22                        2
                // SPEEDSCALER	SingleSpeed	1		1                 3
                // POWERFUNCTION	ALPHA		2.0                   4
                // WORKLOAD	EXPONENTIAL	1000000		0.8		1         5
                // LOGGER		BasicLogger	CONCISE		PROMTWRITE    6
                // PROBES		2		0.02		0.98		1.06  7
                for(int i = 0; i < 5 ; i++) {   //row
                    readFile.nextLine();
                }

                for (int i = 0; i < 4; i++) {
                    readFile.next();
                }
                

     String newName = sourceFile.getParent() + "\\E[S]=" + readFile.next();
     System.out.println(newName);
     readFile.close();

        File destFile = new File(newName);
        sourceFile.renameTo(destFile);
        
    }

 









}
