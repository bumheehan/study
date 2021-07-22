package xyz.bumbing.scheduler;

import java.util.Map;
import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import org.quartz.InterruptableJob;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.quartz.JobKey;
import org.quartz.UnableToInterruptJobException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.quartz.QuartzJobBean;
import org.springframework.stereotype.Component;

@Component
public class CustomJob extends QuartzJobBean implements InterruptableJob {
  private boolean isInterrupted = false;
  private JobKey jobKey = null;

  @Autowired
  @Resource(name = "autowiringTestBean")
  Map<String, String> autoMap;

  @PostConstruct
  public void init() {
    System.out.println("bean 생성");
  }

  @Override
  protected void executeInternal(JobExecutionContext context) throws JobExecutionException {

    System.out.println(autoMap.get("test"));
    jobKey = context.getJobDetail().getKey();
    System.out.println("JobDetail Desc : " + context.getJobDetail().getDescription());
    System.out.println("JobDetail JobKey : " + context.getJobDetail().getKey().getName());

    System.out.println("Trigger Desc : " + context.getTrigger().getDescription());
    System.out.println("Trigger JobKey : " + context.getTrigger().getKey().getName());


    System.out.println("JobDataMap : " + context.getJobDetail().getJobDataMap().get("data"));
    System.out.println("thread : " + Thread.currentThread());
    System.out.println("timer start");
    try {
      Thread.sleep(5000);
    } catch (InterruptedException e) {
      e.printStackTrace();
    }
    System.out.println("JobDataMap : " + context.getJobDetail().getJobDataMap().get("data"));
    System.out.println("timer stop");
    if (isInterrupted) {
      System.out.println("jobKey: " + jobKey + "is Interrupted.");
      return;
    }

  }

  @Override
  public void interrupt() throws UnableToInterruptJobException {
    System.out.println(jobKey + "  -- INTERRUPTING --");
    isInterrupted = true;

  }

}
