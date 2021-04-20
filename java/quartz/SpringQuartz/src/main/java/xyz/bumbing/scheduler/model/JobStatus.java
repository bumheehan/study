package xyz.bumbing.scheduler.model;

import java.util.List;

public class JobStatus {

    private String name;
    private String group;
    private List<TriggerStatus> triggerList;
    private boolean isRunning;
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public String getGroup() {
        return group;
    }
    public void setGroup(String group) {
        this.group = group;
    }
    public List<TriggerStatus> getTriggerList() {
        return triggerList;
    }
    public void setTriggerList(List<TriggerStatus> triggerList) {
        this.triggerList = triggerList;
    }
    public boolean isRunning() {
        return isRunning;
    }
    public void setRunning(boolean isRunning) {
        this.isRunning = isRunning;
    }
    
    
}
