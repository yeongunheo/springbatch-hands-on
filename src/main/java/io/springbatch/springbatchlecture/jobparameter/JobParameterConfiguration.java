package io.springbatch.springbatchlecture.jobparameter;

import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.PlatformTransactionManager;

import java.util.Map;

@RequiredArgsConstructor
@Configuration
public class JobParameterConfiguration {

    @Bean
    public Job jobParameterTestJob(JobRepository jobRepository, PlatformTransactionManager platformTransactionManager) {
        return new JobBuilder("jobParameterTestJob", jobRepository)
                .start(jobParameterTestStep1(jobRepository, platformTransactionManager))
                .next(jobParameterTestStep2(jobRepository, platformTransactionManager))
                .build();
    }

    @Bean
    public Step jobParameterTestStep1(JobRepository jobRepository, PlatformTransactionManager platformTransactionManager) {
        return new StepBuilder("jobParameterTestStep1", jobRepository)
                .tasklet(new Tasklet() {
                    @Override
                    public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) throws Exception {

                        JobParameters jobParameters = contribution.getStepExecution().getJobExecution().getJobParameters();
                        jobParameters.getString("name");
                        jobParameters.getLong("seq");
                        jobParameters.getDate("date");
                        jobParameters.getDouble("age");

                        Map<String, Object> jobParameters1 = chunkContext.getStepContext().getJobParameters();

                        System.out.println("step1 was executed");
                        return RepeatStatus.FINISHED;
                    }
                }, platformTransactionManager)
                .build();
    }

    @Bean
    public Step jobParameterTestStep2(JobRepository jobRepository, PlatformTransactionManager platformTransactionManager) {
        return new StepBuilder("jobParameterTestStep2", jobRepository)
                .tasklet(new Tasklet() {
                    @Override
                    public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) throws Exception {
                        System.out.println("step2 was executed");
                        return RepeatStatus.FINISHED;
                    }
                }, platformTransactionManager)
                .build();
    }
}
