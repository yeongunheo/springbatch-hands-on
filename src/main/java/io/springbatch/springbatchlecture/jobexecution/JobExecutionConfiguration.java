package io.springbatch.springbatchlecture.jobexecution;

import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.Job;
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

@RequiredArgsConstructor
@Configuration
public class JobExecutionConfiguration {

    @Bean
    public Job jobExecutionTestJob(JobRepository jobRepository, PlatformTransactionManager platformTransactionManager) {
        return new JobBuilder("jobExecutionTestJob", jobRepository)
                .start(jobExecutionTestStep1(jobRepository, platformTransactionManager))
                .next(jobExecutionTestStep2(jobRepository, platformTransactionManager))
                .build();
    }

    @Bean
    public Step jobExecutionTestStep1(JobRepository jobRepository, PlatformTransactionManager platformTransactionManager) {
        return new StepBuilder("jobExecutionTestStep1", jobRepository)
                .tasklet(new Tasklet() {
                    @Override
                    public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) throws Exception {
                        System.out.println("step1 was executed");
                        return RepeatStatus.FINISHED;
                    }
                }, platformTransactionManager)
                .build();
    }

    @Bean
    public Step jobExecutionTestStep2(JobRepository jobRepository, PlatformTransactionManager platformTransactionManager) {
        return new StepBuilder("jobExecutionTestStep2", jobRepository)
                .tasklet(new Tasklet() {
                    @Override
                    public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) throws Exception {
                        System.out.println("step2 was executed");

                        /**
                         * job exectuion status가 COMPLETED가 아닐 땐 계속해서 재실행이 가능하다.
                         * 대신 job instance는 한 번만 만들어진다.
                         */
                        // throw new RuntimeException("step2 has failed");
                        return RepeatStatus.FINISHED;
                    }
                }, platformTransactionManager)
                .build();
    }
}
