import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;

public class Driver {
    public static void main(String[] args) throws IOException {        
        // use fcfs, rr (requires time quantum), SRT, HRRN (must calculate ratio)
        ArrayList<Process> randInput = new ArrayList<>();
        ArrayList<Process> sampleInput = new ArrayList<>();

        // let arrival time be given for simplicity: 0, 2, 4, 6, 8
        int[] arrivalTimes = {0, 2, 4, 6, 8};
        // let PID be dummy values declared : 1, 2, 3, 4 ,5
        int[] pid = {1, 2, 3, 4 ,5};
        // serviceTimes from example
        int[] serviceTimes = {6, 12, 8, 10, 4};

        randInput = RandomSeed(arrivalTimes, pid);
        sampleInput = SampleSeed(arrivalTimes, pid, serviceTimes);    
                
        // call schedulers
        // assume timeQuantum is 5s
        int timeQuantum = 5;
        Scheduler sched;
        sched = new Scheduler(randInput, timeQuantum);
        /* Call First-Come-First-Serve */
        sched.fcfs();
        /* Call Round Robin */
        sched.RoundRobin();
        /* Call Shortest Remaining Time */
        sched.SRT();
        /* Call Highest Response Ratio Next */
        sched.HRRN();

        sched = new Scheduler(sampleInput, timeQuantum);        
        /* Call First-Come-First-Serve */        
        sched.fcfs();
        /* Call Round Robin */
        sched.RoundRobin();
        /* Call Shortest Remaining Time */
        sched.SRT();
        /* Call Highest Response Ratio Next */
        sched.HRRN();
    }

    public static ArrayList<Process> RandomSeed(int[] arrivalTimes, int[] pid) {
        ArrayList<Process> input = new ArrayList<>();
        // generate random service time for 5 processes
        Random rand = new Random();

        // assume max service time and arrival time for a process to 20
        int upperbound = 20;

        // add each process to an array list 
        System.out.println("PID \t arrivalTime \t serviceTime");
        for(int i = 0; i < 5; i++ ) {
            int serviceTime = rand.nextInt(upperbound);
            int arrivalTime = rand.nextInt(upperbound);

            Process process = new Process(pid[i], arrivalTime, serviceTime);  
            System.out.println(process.pid + "\t\t" + process.arrivalTime + "\t\t" 
                + process.serviceTime);
            input.add(process);
        }
        return input;
    }

    public static ArrayList<Process> SampleSeed(int[] arrivalTimes, int[] pid, int[] serviceTimes) {
        ArrayList<Process> input = new ArrayList<>();
        // add each process to an array list 
        System.out.println("PID \t arrivalTime \t serviceTime");
        for(int i = 0; i < 5; i++) {
            Process process = new Process(pid[i], arrivalTimes[i], serviceTimes[i]);
            System.out.println(process.pid + "\t\t" + process.arrivalTime + "\t\t" 
                + process.serviceTime);
            input.add(process);
        }
        return input;
    }
}
