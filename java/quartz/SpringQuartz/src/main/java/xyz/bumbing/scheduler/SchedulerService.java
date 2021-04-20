package xyz.bumbing.scheduler;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.Random;
import java.util.TimeZone;
import java.util.stream.Collectors;

import org.quartz.CronScheduleBuilder;
import org.quartz.JobBuilder;
import org.quartz.JobDataMap;
import org.quartz.JobDetail;
import org.quartz.JobKey;
import org.quartz.ScheduleBuilder;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.SimpleScheduleBuilder;
import org.quartz.Trigger;
import org.quartz.TriggerBuilder;
import org.quartz.UnableToInterruptJobException;
import org.quartz.impl.matchers.GroupMatcher;
import org.springframework.boot.task.TaskSchedulerBuilder;
import org.springframework.stereotype.Service;

import xyz.bumbing.scheduler.model.JobReqeust;
import xyz.bumbing.scheduler.model.JobStatus;
import xyz.bumbing.scheduler.model.JobStatusResponse;
import xyz.bumbing.scheduler.model.TriggerStatus;

@Service
public class SchedulerService {

    
    private Scheduler scheduler;
    
    public SchedulerService(Scheduler scheduler) {
	this.scheduler=scheduler;
    }
    
    public void addSimpleScheduler() throws SchedulerException {
	
	try {
	    JobDetail jd = JobBuilder.newJob()
		    .ofType(SimpleJob.class)
		    .setJobData(new JobDataMap()) //Job에서 사용할 데이터 추가
		    //.storeDurably() //Job 종료 후 남아있게함? 
		    .withIdentity("simpleDetail", "simpleGroup") // JobKey 설정
		    .withDescription("Simple Scheduler입니다.")
		    .build();
	    
	    Trigger tr = TriggerBuilder.newTrigger()
		    .withIdentity("simpleTrigger", "simpleGroup")
		    .withDescription("Simple Trigger")
		    .withSchedule(CronScheduleBuilder.cronSchedule("0/10 * * * * ?").inTimeZone(TimeZone.getDefault()))
		    .build();
	    
	    scheduler.scheduleJob(jd,tr);
	} catch (SchedulerException e) {
	    throw e;
	}
	
    }
    public String addScheduler(JobReqeust jobRequest) throws SchedulerException {
	if(jobRequest.getJobName()==null){
	    jobRequest.setJobName(getRandomKey(8));
	}
	if(jobRequest.getTriggerName()==null) {
	    jobRequest.setTriggerName(getRandomKey(8));
	}
	
	try {
	    JobDetail jd = JobBuilder.newJob()
		    .ofType(CustomJob.class)
		    .setJobData(new JobDataMap(jobRequest.getJobDataMap())) //Job에서 사용할 데이터 추가
		    //.storeDurably() //Job 종료 후 남아있게함? 
		    .withIdentity(jobRequest.getJobName(), jobRequest.getJobGroup()) // JobKey 설정
		    .withDescription("Custom Scheduler입니다.")
		    .build();
	    
	    Trigger tr = TriggerBuilder.newTrigger()
		    .withIdentity(jobRequest.getTriggerName(), jobRequest.getTriggerGroup())
		    .withDescription("Custom Trigger")
		    .startAt(Date.from(Instant.now().plusSeconds(10)))
		    .endAt(Date.from(Instant.now().plusSeconds(60)))
		    .withSchedule(SimpleScheduleBuilder.repeatSecondlyForever(10))
		    .build();
	    
	    scheduler.scheduleJob(jd,tr);
	    
	    return jobRequest.getJobName();
	} catch (SchedulerException e) {
	    throw e;
	}
    }
    
    public void removeScheduler(JobReqeust jobRequest) throws SchedulerException {
	try {
	    if(jobRequest.getJobGroup()==null) {
		scheduler.getJobKeys(GroupMatcher.jobGroupContains(jobRequest.getJobGroup())).stream().filter(s->jobRequest.getJobName().equals(s.getName())).findFirst().ifPresent(s->{
		    try {
			scheduler.deleteJob(s);
		    } catch (SchedulerException e) {
			e.printStackTrace();
		    }
		});
	    }else {
		scheduler.getJobKeys(GroupMatcher.anyJobGroup()).stream().filter(s->jobRequest.getJobName().equals(s.getName())).findFirst().ifPresent(s->{
		    try {
			scheduler.deleteJob(s);
		    } catch (SchedulerException e) {
			e.printStackTrace();
		    }
		});
	    }
	} catch (SchedulerException e) {
	    throw e;
	}
    }
    
    public JobStatusResponse getStatus() throws SchedulerException {
	
	
	int numOfRunningJobs = 0 ;
	int numOfGroups = 0 ;
	int numOfAllJobs = 0 ;
	try {
	    	List<JobKey> runningJobs = scheduler.getCurrentlyExecutingJobs().stream().map(s->s.getJobDetail().getKey()).collect(Collectors.toList());
	    	numOfRunningJobs=runningJobs.size();
	    	
	    	List<JobStatus> jobsStatus = new ArrayList<>(); 
        	for(String group : scheduler.getJobGroupNames()) {
        	    numOfGroups++;
        		for(JobKey jobKey: scheduler.getJobKeys(GroupMatcher.jobGroupEquals(group))) {
        		    JobStatus status = new JobStatus();
        		    numOfAllJobs++;
        		    List<TriggerStatus> triggerList = new ArrayList<>();
        		    for(Trigger trigger :  scheduler.getTriggersOfJob(jobKey)) {
        			TriggerStatus triggerStatus = new TriggerStatus();
        			triggerStatus.setName(trigger.getKey().getName());
        			triggerStatus.setGroup(trigger.getKey().getGroup());
        			triggerStatus.setNextFireTime(trigger.getNextFireTime());
        			triggerStatus.setPreviousFireTime(trigger.getPreviousFireTime());
        			triggerStatus.setStartTime(trigger.getStartTime());
        			triggerList.add(triggerStatus);
        		    }
        		    status.setName(jobKey.getName());
        		    status.setGroup(group);
        		    status.setTriggerList(triggerList);
        		    status.setRunning(runningJobs.contains(jobKey));
        		    jobsStatus.add(status);
        		}
        	}
        	
        	JobStatusResponse jobStatusResponse = new JobStatusResponse();
        	jobStatusResponse.setNumOfAllJobs(numOfAllJobs);
        	jobStatusResponse.setNumOfGroups(numOfGroups);
        	jobStatusResponse.setNumOfRunningJobs(numOfRunningJobs);
        	jobStatusResponse.setJobsStatus(jobsStatus);
        	return jobStatusResponse;
	} catch (SchedulerException e) {
	    throw e;
	}
	
    }
    
    public void stopScheduler(String jobid,String group) throws SchedulerException {
	try {
	    Optional<JobKey> jobKey = scheduler.getJobKeys(GroupMatcher.jobGroupContains(group)).stream().filter(s->jobid.equals(s.getName())).findFirst();
	    jobKey.ifPresent(s->{
		try {
		    scheduler.interrupt(s);
		} catch (UnableToInterruptJobException e) {
		    e.printStackTrace();
		}
	    });
	} catch (SchedulerException e) {
	    throw e;
	}
    }
    public void pauseScheduler(String jobid,String group) throws SchedulerException {
	try {
	    Optional<JobKey> jobKey = scheduler.getJobKeys(GroupMatcher.jobGroupContains(group)).stream().filter(s->jobid.equals(s.getName())).findFirst();
	    jobKey.ifPresent(s->{
		try {
		    scheduler.pauseJob(s);
		} catch (SchedulerException e) {
		    e.printStackTrace();
		}
	    });
	} catch (SchedulerException e) {
	    throw e;
	}
    }
    
    public void resumeScheduler(String jobid,String group) throws SchedulerException {
	try {
	    Optional<JobKey> jobKey = scheduler.getJobKeys(GroupMatcher.jobGroupContains(group)).stream().filter(s->jobid.equals(s.getName())).findFirst();
	    jobKey.ifPresent(s->{
		try {
		    scheduler.resumeJob(s);
		} catch (SchedulerException e) {
		    e.printStackTrace();
		}
	    });
	} catch (SchedulerException e) {
	    throw e;
	}
    }

    private String getRandomKey(int num) {
	 
	    int leftLimit = 97; // letter 'a'
	    int rightLimit = 122; // letter 'z'
	    int targetStringLength = num;
	    Random random = new Random();
	    StringBuilder buffer = new StringBuilder(targetStringLength);
	    for (int i = 0; i < targetStringLength; i++) {
	        int randomLimitedInt = leftLimit + (int) 
	          (random.nextFloat() * (rightLimit - leftLimit + 1));
	        buffer.append((char) randomLimitedInt);
	    }
	    String generatedString = buffer.toString();

	    return generatedString;
	}

   
}
