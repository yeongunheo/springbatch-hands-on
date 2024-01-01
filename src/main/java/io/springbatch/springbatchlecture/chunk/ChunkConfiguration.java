package io.springbatch.springbatchlecture.chunk;

import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.item.Chunk;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.support.ListItemReader;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.PlatformTransactionManager;

import java.util.List;

@RequiredArgsConstructor
@Configuration
public class ChunkConfiguration {

    @Bean
    public Job chunkTestJob(JobRepository jobRepository, PlatformTransactionManager platformTransactionManager) {
        return new JobBuilder("chunkTestJob", jobRepository)
                .start(chunkTestStep1(jobRepository, platformTransactionManager))
                .next(chunkTestStep2(jobRepository, platformTransactionManager))
                .build();
    }

    @Bean
    public Step chunkTestStep1(JobRepository jobRepository, PlatformTransactionManager platformTransactionManager) {
        return new StepBuilder("chunkTestStep1", jobRepository)
                .<String, String>chunk(5, platformTransactionManager)
                .reader(new ListItemReader<>(List.of("item1", "item2", "item3", "item4", "item5")))
                .processor(new ItemProcessor<String, String>() {
                    @Override
                    public String process(String item) throws Exception {
                        Thread.sleep(300);
                        System.out.println("item = " + item);
                        return "my" + item;
                    }
                })
                .writer(new ItemWriter<String>() {
                    @Override
                    public void write(Chunk<? extends String> chunk) throws Exception {
                        Thread.sleep(300);
                        List<? extends String> items = chunk.getItems();
                        System.out.println("items = " + items);
                    }
                })
                .build();
    }

    @Bean
    public Step chunkTestStep2(JobRepository jobRepository, PlatformTransactionManager platformTransactionManager) {
        return new StepBuilder("chunkTestStep2", jobRepository)
                .tasklet((contribution, chunkContext) -> {
                    System.out.println("step2 was executed");
                    return RepeatStatus.FINISHED;
                }, platformTransactionManager)
                .build();
    }
}
