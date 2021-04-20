package xyz.bumbing.scheduler.model;

import java.util.List;

public class JobStatusResponse {

    private int numOfRunningJobs;
    private int numOfGroups;
    private int numOfAllJobs;
    private List<JobStatus> jobsStatus;
    
    public int getNumOfRunningJobs() {
        return numOfRunningJobs;
    }
    public void setNumOfRunningJobs(int numOfRunningJobs) {
        this.numOfRunningJobs = numOfRunningJobs;
    }
    public int getNumOfGroups() {
        return numOfGroups;
    }
    public void setNumOfGroups(int numOfGroups) {
        this.numOfGroups = numOfGroups;
    }
    public int getNumOfAllJobs() {
        return numOfAllJobs;
    }
    public void setNumOfAllJobs(int numOfAllJobs) {
        this.numOfAllJobs = numOfAllJobs;
    }
    public List<JobStatus> getJobsStatus() {
        return jobsStatus;
    }
    public void setJobsStatus(List<JobStatus> jobsStatus) {
        this.jobsStatus = jobsStatus;
    }
    
    
}
