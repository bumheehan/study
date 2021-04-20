package xyz.bumbing.scheduler;

import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

public class SimpleJob implements Job {

    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {
	System.out.println("JobDetail Desc : " + context.getJobDetail().getDescription());
	System.out.println("JobDetail JobKey : " + context.getJobDetail().getKey().getName());

	System.out.println("Trigger Desc : " + context.getTrigger().getDescription());
	System.out.println("Trigger JobKey : " + context.getTrigger().getKey().getName());
	System.out.println("run");
    }

}
