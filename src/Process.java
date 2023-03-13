/** 
 * Process class, contains required attributes for each process: pid, arrival time, service time
 * initial service time, and completion time
 */
public class Process {
    public int pid,
        arrivalTime, 
        serviceTime,
        // current service time assigned to process by scheduler at a given time 
        initServiceTime,
        // used in SRT, records remaining service time
        remainingTime,
        completionTime;    
    
    // response ration is used for HRRN
    public float responseRatio;
    
    // constructor 
    public Process(int pid, int arrivalTime, int serviceTime) {
        this.pid = pid;
        this.arrivalTime = arrivalTime;
        this.serviceTime = serviceTime;
        initServiceTime = 0;
        remainingTime = serviceTime;
    }

    public Process(Process process) {
        this.pid = process.pid;
        this.arrivalTime = process.arrivalTime;
        this.serviceTime = process.serviceTime;
        initServiceTime = 0;
        remainingTime = process.serviceTime;
    }
}
