package io.springbatch.springbatchlecture.jobincrementor;

import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.PlatformTransactionManager;

@RequiredArgsConstructor
@Configuration
public class JobIncrementorConfiguration {

    @Bean
    public Job jobIncrementorTestJob(JobRepository jobRepository, PlatformTransactionManager platformTransactionManager) {
        return new JobBuilder("jobIncrementorTestJob", jobRepository)
                .start(jobIncrementorTestStep1(jobRepository, platformTransactionManager))
                .next(jobIncrementorTestStep2(jobRepository, platformTransactionManager))
                .incrementer(new RunIdIncrementer()) // key = run.id
                .build();
    }

    @Bean
    public Step jobIncrementorTestStep1(JobRepository jobRepository, PlatformTransactionManager platformTransactionManager) {
        return new StepBuilder("jobIncrementorTestStep1", jobRepository)
                .tasklet((contribution, chunkContext) -> {
                    System.out.println("step1 was executed");
                    return RepeatStatus.FINISHED;
                }, platformTransactionManager)
                .build();
    }

    @Bean
    public Step jobIncrementorTestStep2(JobRepository jobRepository, PlatformTransactionManager platformTransactionManager) {
        return new StepBuilder("jobIncrementorTestStep2", jobRepository)
                .tasklet((contribution, chunkContext) -> {
                    System.out.println("step2 was executed");
                    return RepeatStatus.FINISHED;
                }, platformTransactionManager)
                .build();
    }
}
