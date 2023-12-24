package io.springbatch.springbatchlecture;

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
public class HelloJobConfiguration {

    @Bean
    public Job footballJob(JobRepository jobRepository, PlatformTransactionManager platformTransactionManager) {
        return new JobBuilder("helloJob", jobRepository)
                .start(helloStep1(jobRepository, platformTransactionManager))
                .next(helloStep2(jobRepository, platformTransactionManager))
                .build();
    }

    @Bean
    public Step helloStep1(JobRepository jobRepository, PlatformTransactionManager platformTransactionManager) {
        return new StepBuilder("helloStep1", jobRepository)
                .tasklet(new Tasklet() {
                    @Override
                    public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) throws Exception {

                        System.out.println(" ======================= ");
                        System.out.println(" >> Hello Spring Batch!! ");
                        System.out.println(" ======================= ");

                        return RepeatStatus.FINISHED;
                    }
                }, platformTransactionManager)
                .build();
    }

    @Bean
    public Step helloStep2(JobRepository jobRepository, PlatformTransactionManager platformTransactionManager) {
        return new StepBuilder("helloStep2", jobRepository)
                .tasklet(new Tasklet() {
                    @Override
                    public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) throws Exception {

                        System.out.println(" ======================= ");
                        System.out.println(" >> step2 was executed ");
                        System.out.println(" ======================= ");

                        return RepeatStatus.FINISHED;
                    }
                }, platformTransactionManager)
                .build();
    }
}
