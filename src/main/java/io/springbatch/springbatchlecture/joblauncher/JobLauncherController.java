package io.springbatch.springbatchlecture.joblauncher;

import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.JobParametersInvalidException;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.repository.JobExecutionAlreadyRunningException;
import org.springframework.batch.core.repository.JobInstanceAlreadyCompleteException;
import org.springframework.batch.core.repository.JobRestartException;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;

@RequiredArgsConstructor
@RestController
public class JobLauncherController {

    private final Job jobLauncherTestJob;
    private final JobLauncher jobLauncher;

    @PostMapping("/batch")
    public String launch(@RequestBody Member member) throws JobInstanceAlreadyCompleteException, JobExecutionAlreadyRunningException, JobParametersInvalidException, JobRestartException {

        var jobParameters = new JobParametersBuilder()
                .addString("id", member.id())
                .addDate("date", new Date())
                .toJobParameters();

        jobLauncher.run(jobLauncherTestJob, jobParameters);

        return "batch completed";
    }
}
