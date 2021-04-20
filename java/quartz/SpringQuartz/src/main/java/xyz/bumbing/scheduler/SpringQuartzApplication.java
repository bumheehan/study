package xyz.bumbing.scheduler;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import xyz.bumbing.scheduler.model.JobReqeust;
import xyz.bumbing.scheduler.model.JobStatusResponse;

@SpringBootApplication
@RestController
public class SpringQuartzApplication {

    public static void main(String[] args) {
	SpringApplication.run(SpringQuartzApplication.class, args);
    }

    @Autowired
    SchedulerService schedulerService;

    @PostMapping("/scheduler/simple")
    public ResponseEntity<String> addSimpleScheduler() {
	try {
	    schedulerService.addSimpleScheduler();
	    return ResponseEntity.ok("success");
	} catch (Exception e) {
	    e.printStackTrace();
	    return ResponseEntity.badRequest().body(e.getMessage());
	}
    }

    @PostMapping("/scheduler")
    public ResponseEntity<String> add(@RequestBody JobReqeust jobRequest) {
	try {
	    return ResponseEntity.ok(schedulerService.addScheduler(jobRequest));
	} catch (Exception e) {
	    e.printStackTrace();
	    return ResponseEntity.badRequest().body(e.getMessage());
	}
    }
    @DeleteMapping("/scheduler")
    public ResponseEntity<String> delete(@RequestBody JobReqeust jobRequest) {
	try {
	    schedulerService.removeScheduler(jobRequest);
	    return ResponseEntity.ok("success");
	} catch (Exception e) {
	    e.printStackTrace();
	    return ResponseEntity.badRequest().body(e.getMessage());
	}
    }

    @PostMapping("/scheduler/stop")
    public ResponseEntity<String> stop(@RequestBody JobReqeust jobRequest) {
	try {
	    schedulerService.stopScheduler(jobRequest.getJobName(),jobRequest.getJobGroup());
	    return ResponseEntity.ok("success");
	} catch (Exception e) {
	    e.printStackTrace();
	    return ResponseEntity.badRequest().body(e.getMessage());
	}
    }
    @PostMapping("/scheduler/pause")
    public ResponseEntity<String> pause(@RequestBody JobReqeust jobRequest) {
	try {
	    schedulerService.pauseScheduler(jobRequest.getJobName(),jobRequest.getJobGroup());
	    return ResponseEntity.ok("success");
	} catch (Exception e) {
	    e.printStackTrace();
	    return ResponseEntity.badRequest().body(e.getMessage());
	}
    }
    @PostMapping("/scheduler/resume")
    public ResponseEntity<String> resume(@RequestBody JobReqeust jobRequest) {
	try {
	    schedulerService.resumeScheduler(jobRequest.getJobName(),jobRequest.getJobGroup());
	    return ResponseEntity.ok("success");
	} catch (Exception e) {
	    e.printStackTrace();
	    return ResponseEntity.badRequest().body(e.getMessage());
	}
    }
    
    @GetMapping("/scheduler/status")
    public ResponseEntity<JobStatusResponse> status() {
	try {
	    return ResponseEntity.ok(schedulerService.getStatus());
	} catch (Exception e) {
	    e.printStackTrace();
	    return ResponseEntity.badRequest().build();
	}
    }

}