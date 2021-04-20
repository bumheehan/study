package xyz.bumbing.scheduler.model;

import java.util.HashMap;
import java.util.Map;

import org.quartz.JobDataMap;

public class JobReqeust {

    private String jobName;
    private String jobGroup = "default";
    private String triggerName;
    private String triggerGroup = "default";
    private Map<String, String> jobDataMap =new HashMap<>();
    public String getJobName() {
        return jobName;
    }
    public void setJobName(String jobName) {
        this.jobName = jobName;
    }
    public String getJobGroup() {
        return jobGroup;
    }
    public void setJobGroup(String jobGroup) {
        this.jobGroup = jobGroup;
    }
    public String getTriggerName() {
        return triggerName;
    }
    public void setTriggerName(String triggerName) {
        this.triggerName = triggerName;
    }
    public String getTriggerGroup() {
        return triggerGroup;
    }
    public void setTriggerGroup(String triggerGroup) {
        this.triggerGroup = triggerGroup;
    }
    public Map<String, String> getJobDataMap() {
        return jobDataMap;
    }
    public void setJobDataMap(Map<String, String> jobDataMap) {
        this.jobDataMap = jobDataMap;
    }
    
    
    
}
