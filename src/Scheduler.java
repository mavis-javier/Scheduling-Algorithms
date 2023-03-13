/**
 * Implements Round Robin Scheduling Algorithm
 */
import java.util.*;

public class Scheduler {    
    int timer;
    ArrayList<Process> listOfProcesses;
    ArrayList<Process> readyQueue;
    ArrayList<Process> endProcesses;

    ArrayList<Process> temp = new ArrayList<>();
   
    int timeQuantum;
    // contextSwitch is used in preemptive scheduling
    // contextSwitch < timeQuantum, ex. 0.2 * timeQuantum
    // time taken to switch processes, for preemptive Schedulers
    int contextSwitch;  

    Process cpu;
    int counter;
    
    // constructor to define only listOfProcesses
    public Scheduler(ArrayList<Process> listOfProcesses) {
        this.listOfProcesses = listOfProcesses;        
        for(int i = 0; i < listOfProcesses.size(); i++)
            temp.add(new Process(listOfProcesses.get(i)));
    }

    // constructor to define listOfProcesses and timeQuantum (for RR scheduling)
    public Scheduler(ArrayList<Process> listOfProcesses, int timeQuantum) {
        this.listOfProcesses = listOfProcesses;
        this.timeQuantum = timeQuantum;
        for(int i = 0; i < listOfProcesses.size(); i++)
            temp.add(new Process(listOfProcesses.get(i)));
    }

    /* First-come-first-serve function (fcfs) - nonpreemptive */
    public void fcfs() {
        timer = 0;
        // no cpu is assigned to execute a process since no process is available
        cpu = null;
        // create array lists that stores readyQueue and endProcesses
        readyQueue = new ArrayList<>();
        endProcesses = new ArrayList<>();               

        // restore listOfProcesses (in case it was used in other scheduling functions)
        if(listOfProcesses.isEmpty())
            for(int i = 0; i < temp.size(); i++)
                listOfProcesses.add(temp.get(i)); 
        
        // assign new Processes to temp list
        for(int i = 0; i < listOfProcesses.size(); i++)
            temp.set(i, new Process(listOfProcesses.get(i)));

        // add processes into readyQueue, execute them and add processes into endProcesses
        while(!readyQueue.isEmpty() || !listOfProcesses.isEmpty() || cpu != null) {
            // add to readyQueue
            for(int i = 0; i < listOfProcesses.size(); i++) {
                // adds process to the readyQueue the moment it "arrives"
                if(listOfProcesses.get(i).arrivalTime == timer) {
                    readyQueue.add(listOfProcesses.remove(i));
                }
            }

            // repeat loop if readyQueue is still empty and it is not the last process
            if(readyQueue.isEmpty() && !listOfProcesses.isEmpty()) {
                timer++;
                continue;
            }

            // add first process to cpu
            if(cpu == null && !readyQueue.isEmpty()) {
                cpu = readyQueue.remove(0);
            }

            cpu.initServiceTime++;

            // process is done executing when it reaches its service time
            if(cpu.initServiceTime == cpu.serviceTime) {
                cpu.completionTime = timer + 1;
                endProcesses.add(cpu);
                // empty cpu, no process assigned to cpu again
                cpu = null;
            }

            // increment timer for real time
            timer++;
        }

        /** Calculations */
        System.out.println("FCFS:");
        double sumTAT = 0.0;
        double sumUtil = 0.0;

        for(int i = 0; i < endProcesses.size(); i++) {
            sumTAT += endProcesses.get(i).completionTime - endProcesses.get(i).arrivalTime;
            sumUtil += endProcesses.get(i).serviceTime;
        }
        double avgTAT = sumTAT / endProcesses.size();
        double avgServiceTime = sumUtil / endProcesses.size();
        double ratio = avgTAT / avgServiceTime;
        double throughput = (double) endProcesses.size() / timer;

		System.out.println("Finish Time for each process (a unit of time like seconds): " );		
        // show finish time for each process
        for(int i = 0; i < endProcesses.size(); i++)
            System.out.println("Process " + endProcesses.get(i).pid + ": " 
                + endProcesses.get(i).completionTime);
        System.out.println("Average Response/Service Time: " + avgServiceTime);
		System.out.println("Average Turnaround Time (TAT): " + avgTAT);
        System.out.println("Ratio of TAT to service time: " + ratio);
        System.out.println("Throughput: " + throughput + " process per unit of time");
        System.out.println();
    }
    
    /* Round Robin function - nonpreemptive */
    public void RoundRobin() {
        timer = 0;
        contextSwitch = 0;
        // no cpu is assigned to execute a process since no process available
        cpu = null; 
        // create array lists that stores readyQueue and endProcesses
        readyQueue = new ArrayList<>();
        endProcesses = new ArrayList<>();
        // restore listOfProcesses (in case it was used in other scheduling functions)
        if(listOfProcesses.isEmpty())
            for(int i = 0; i < temp.size(); i++)
                listOfProcesses.add(temp.get(i));
        
        // assign new Processes to temp list
        for(int i = 0; i < listOfProcesses.size(); i++)
            temp.set(i, new Process(listOfProcesses.get(i)));

        while(!readyQueue.isEmpty() || !listOfProcesses.isEmpty() || cpu != null) {
            // add to readyQueue
            for(int i = 0; i < listOfProcesses.size(); i++) {
                // adds process to the readyQueue the moment it "arrives"
                if(listOfProcesses.get(i).arrivalTime == timer) {
                    readyQueue.add(listOfProcesses.remove(i));
                }
            }

            // repeat loop if readyQueue is still empty and it is not the last process
            if(readyQueue.isEmpty() && !listOfProcesses.isEmpty()) {
                timer++;
                continue;
            }

            // add first process in readyQueue if cpu is idle
            if(cpu == null && !readyQueue.isEmpty()) {
                cpu = readyQueue.remove(0);
            }

            counter++;
            cpu.initServiceTime++;

            // process is done executing when it reaches its service time
            if(cpu.initServiceTime == cpu.serviceTime) {
                cpu.completionTime = timer + 1;
                endProcesses.add(cpu);
                // empty cpu, no process assigned to cpu again 
                cpu = null;
                contextSwitch++;
                counter = 0;                
            // if counter exceeds timeQuantum and process still not done, preempt process
            } else if(counter == timeQuantum){
                readyQueue.add(cpu);
                cpu = null;
                contextSwitch++;
                counter = 0;
            }
            // increment timer for real-time
            timer++;
        }

        /** Calculations */
        System.out.println("Round Robin:");
        double sumTAT = 0.0;
        double sumUtil = 0.0;

        for(int i = 0; i < endProcesses.size(); i++) {
            sumTAT += endProcesses.get(i).completionTime - endProcesses.get(i).arrivalTime;
            sumUtil += endProcesses.get(i).serviceTime;
        }
        double avgTAT = sumTAT / endProcesses.size();
        double avgServiceTime = sumUtil / endProcesses.size();
        double ratio = avgTAT / avgServiceTime;
        double throughput = (double) endProcesses.size() / timer;

		System.out.println("Finish Time for each process (a unit of time like seconds): " );		
        // show finish time for each process
        for(int i = 0; i < endProcesses.size(); i++)
            System.out.println("Process " + endProcesses.get(i).pid + ": " 
                + endProcesses.get(i).completionTime);
        System.out.println("Average Response/Service Time: " + avgServiceTime);
		System.out.println("Average Turnaround Time (TAT): " + avgTAT);
        System.out.println("Ratio of TAT to service time: " + ratio);
        System.out.println("Throughput: " + throughput + " process per unit of time");
        System.out.println();
    }    

    /* Shortest Remaining Time function - preemptive */ 
    public void SRT() {
        timer = 0;
        contextSwitch = 0;
        // no cpu is assigned to execute a process since no process available
        cpu = null; 
        // create array lists that stores readyQueue and endProcesses
        readyQueue = new ArrayList<>();
        endProcesses = new ArrayList<>();
        // restore listOfProcesses (in case it was used in other scheduling functions)
        if(listOfProcesses.isEmpty())
            for(int i = 0; i < temp.size(); i++)
                listOfProcesses.add(temp.get(i));

        // assign new Processes to temp list
        for(int i = 0; i < listOfProcesses.size(); i++)
            temp.set(i, new Process(listOfProcesses.get(i)));
        
        while(!readyQueue.isEmpty() || !listOfProcesses.isEmpty() || cpu != null) {
            for(int i = 0; i < listOfProcesses.size(); i++) {
                // adds process to the readyQueue the moment it "arrives"
                if(listOfProcesses.get(i).arrivalTime == timer) {
                    readyQueue.add(listOfProcesses.remove(i));
                }
            }

            // repeat loop if readyQueue is still empty and it is not the last process
            if(readyQueue.isEmpty() && !listOfProcesses.isEmpty()) {
                timer++;
                continue;
            }
    
            // add first process to cpu if not assigned
            if(cpu == null && !readyQueue.isEmpty()) {
                cpu = readyQueue.remove(0);
            }                

            // counter increments for preemption
            counter++;
            cpu.initServiceTime++;
            cpu.remainingTime--;

            // process reaches timeQuantum but not done executing
            if (counter == timeQuantum && cpu.initServiceTime != cpu.serviceTime) {
                // preempt process by adding process back to readyQueue
                if(findSRTIndex(readyQueue, cpu) != -1) {
                    readyQueue.add(cpu); 
                    cpu = readyQueue.remove(findSRTIndex(readyQueue, cpu));
                }                     
                counter = 0;
            }
            // process finished executing 
            else if (cpu.initServiceTime == cpu.serviceTime){
                cpu.completionTime = timer;
                cpu.remainingTime = 0;                
                endProcesses.add(cpu);     
                // if last process
                if(cpu != null && readyQueue.isEmpty())
                    cpu = null;
                // if there are still processes in readyQueue, assign cpu with SRT process
                if(!readyQueue.isEmpty()) {
                    if(findSRTIndex(readyQueue, cpu) != -1)
                        cpu = readyQueue.remove(findSRTIndex(readyQueue, cpu));
                }                       
                counter = 0;
            }

            // process finished executing and does not reach timeQuantum yet
            else if (cpu.initServiceTime == cpu.serviceTime && cpu.remainingTime == 0) {
                // System.out.println("Process " + cpu.pid + " finished executing before timeQuantum");
                cpu.completionTime = timer;                
                endProcesses.add(cpu);
                cpu = null;
            }
    
            timer++;
        }          
        
        /** Calculations */
        System.out.println("SRT:");
        double sumTAT = 0.0;
        double sumUtil = 0.0;

        for(int i = 0; i < endProcesses.size(); i++) {
            sumTAT += endProcesses.get(i).completionTime - endProcesses.get(i).arrivalTime;
            sumUtil += endProcesses.get(i).serviceTime;
        }
        double avgTAT = sumTAT / endProcesses.size();
        double avgServiceTime = sumUtil / endProcesses.size();
        double ratio = avgTAT / avgServiceTime;
        double throughput = (double) endProcesses.size() / timer;

		System.out.println("Finish Time for each process (a unit of time like seconds): " );		
        // show finish time for each process
        for(int i = 0; i < endProcesses.size(); i++)
            System.out.println("Process " +  endProcesses.get(i).pid
                + ": " + endProcesses.get(i).completionTime);
        System.out.println("Average Response/Service Time: " + avgServiceTime);
		System.out.println("Average Turnaround Time (TAT): " + avgTAT);
        System.out.println("Ratio of TAT to service time: " + ratio);
        System.out.println("Throughput: " + throughput + " process per unit of time");
        System.out.println();
    }

    /* Highest Response Ratio Next function - nonpreemptive */
    public void HRRN() {
        timer = 0;
        // initially assign no process to cpu (no process has arrived yet)
        cpu = null;

        // create array lists that stores readyQueue and endProcesses
        readyQueue = new ArrayList<>();
        endProcesses = new ArrayList<>();
        // restore listOfProcesses (in case it was used in other scheduling functions)
        if(listOfProcesses.isEmpty())
            for(int i = 0; i < temp.size(); i++)
                listOfProcesses.add(temp.get(i));

        // assign new Processes to temp list
        for(int i = 0; i < listOfProcesses.size(); i++)
            temp.set(i, new Process(listOfProcesses.get(i)));

        // perform scheduling while there is still process to be executed
        while(!readyQueue.isEmpty() || !listOfProcesses.isEmpty() || cpu != null) {
            for(int i = 0; i < listOfProcesses.size(); i++) {
                // adds process to the readyQueue the moment it "arrives"
                if(listOfProcesses.get(i).arrivalTime == timer) {                      
                    readyQueue.add(listOfProcesses.remove(i));                          
                }
            }
            // repeat loop if readyQueue is still empty and it is not the last process
            if(readyQueue.isEmpty() && !listOfProcesses.isEmpty()) {
                if(cpu != null)
                    cpu.initServiceTime++;
                timer++;
                continue;
            }
    
            // add first process to cpu if not assigned
            if(cpu == null && !readyQueue.isEmpty()) {
                int index = findHRRN(readyQueue);
                cpu = readyQueue.remove(index);
            }   

            cpu.initServiceTime++;

            // process finished executing
            if(cpu.initServiceTime == cpu.serviceTime) {
                cpu.completionTime = timer;                
                endProcesses.add(cpu);     
                cpu = null;       
            }

            timer++;
            // update HRRN for each process in readyQueue   
            updateHRRN(readyQueue, timer);   
        }

        /** Calculations */
        System.out.println("HRRN:");
        double sumTAT = 0.0;
        double sumUtil = 0.0;

        for(int i = 0; i < endProcesses.size(); i++) {
            sumTAT += endProcesses.get(i).completionTime - endProcesses.get(i).arrivalTime;
            sumUtil += endProcesses.get(i).serviceTime;
        }
        double avgTAT = sumTAT / endProcesses.size();
        double avgServiceTime = sumUtil / endProcesses.size();
        double ratio = avgTAT / avgServiceTime;
        double throughput = (double) endProcesses.size() / timer;

        System.out.println("Finish Time for each process (a unit of time like seconds): " );		
        // show finish time for each process
        for(int i = 0; i < endProcesses.size(); i++)
            System.out.println("Process " +  endProcesses.get(i).pid
                + ": " + endProcesses.get(i).completionTime);
        System.out.println("Average Response/Service Time: " + avgServiceTime);
        System.out.println("Average Turnaround Time (TAT): " + avgTAT);
        System.out.println("Ratio of TAT to service time: " + ratio);
        System.out.println("Throughput: " + throughput + " process per unit of time");
        System.out.println();
    }
    
    /* Helper Functions */
    /**
     * SRT: finds next process with shortest remaining time
     * @param processList readyQueue
     * @param current current process executed 
     * @return index of SRT process
     */
    private static int findSRTIndex(ArrayList<Process> processList, Process current) {
        Process minSRT = new Process(current);
        int minI = -1;
        // if no remaining time, set current process's remaining time to a max value to swap out
        if(current.remainingTime == 0)
            minSRT.remainingTime = Integer.MAX_VALUE;
        // swaps with current process with the lowest remaining time
        for(int i = 0; i < processList.size(); i++) {
            if(processList.get(i).remainingTime < minSRT.remainingTime) {
                minSRT = processList.get(i);
                minI = i;
            }            
        }    
        return minI;
    }

    /**
     * HRRN: records each response ratio in a readyQueue
     * @param list readyQueue
     * @param timer program's real-time timer
     */
    private static void updateHRRN(ArrayList<Process> list, int timer) {
        int waitingTime;
        float responseRatio;        
        for(int i = 0; i < list.size(); i++) {            
            waitingTime = timer - list.get(i).arrivalTime;
            responseRatio = (float) waitingTime/ list.get(i).serviceTime;
            list.get(i).responseRatio = responseRatio;
        }        
    }

    /**
     * HRRN: finds next process to execute based on highest response ratio 
     * @param list readyQueue
     */
    private static int findHRRN(ArrayList<Process> list) {
        int minI = 0;
        Process process = list.get(0);
        for(int i = 1; i < list.size(); i++) {
            if(Float.compare(list.get(i).responseRatio, process.responseRatio) < 0) {
                process = list.get(i);
                minI = i;
            }
        }
        return minI;
    }
}
