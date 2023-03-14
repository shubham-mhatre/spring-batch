package com.spring.batch.controller;

import java.time.Duration;
import java.time.Instant;

import org.apache.logging.log4j.CloseableThreadContext.Instance;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParameter;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.JobParametersInvalidException;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.repository.JobExecutionAlreadyRunningException;
import org.springframework.batch.core.repository.JobInstanceAlreadyCompleteException;
import org.springframework.batch.core.repository.JobRestartException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class WebController {
	
	@Autowired private JobLauncher jobLauncher;
	
	@Autowired private Job job;
	
	@GetMapping("/csvToDb")
	public String csvToDbProcess() {
		
		Instant startTime = Instant.now();
		
		JobParameters jobParam = new JobParametersBuilder().addLong("startAt", System.currentTimeMillis()).toJobParameters();
		try {
			jobLauncher.run(job, jobParam);
		} catch (JobExecutionAlreadyRunningException | JobRestartException | JobInstanceAlreadyCompleteException
				| JobParametersInvalidException e) {
			e.printStackTrace();
		} 
		
		Instant finishTime = Instant.now();
		Long minutes = Duration.between(startTime, finishTime).toMinutes();
		Long seconds = Duration.between(startTime, finishTime).getSeconds();
		Long milisec = Duration.between(startTime, finishTime).toMillis();
		System.out.println("##########   TOTAL TIME TAKEN TO EXECUTE RULES :: MINUTES = +"+ minutes +" :: SECONDS = "+seconds+" :: MILISEC = "+milisec +"  ##########");
		return "file procesed in milisec : "+milisec;
	}

}
