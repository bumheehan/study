package xyz.bumbing.scheduler;

import java.util.Map;
import javax.annotation.Resource;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class SimpleJob implements Job {

  @Autowired
  @Resource(name = "autowiringTestBean")
  Map<String, String> autoMap;

  @Override
  public void execute(JobExecutionContext context) throws JobExecutionException {
    System.out.println(autoMap.get("test"));
    System.out.println("JobDetail Desc : " + context.getJobDetail().getDescription());
    System.out.println("JobDetail JobKey : " + context.getJobDetail().getKey().getName());

    System.out.println("Trigger Desc : " + context.getTrigger().getDescription());
    System.out.println("Trigger JobKey : " + context.getTrigger().getKey().getName());
    System.out.println("run");
  }

}
