package holymagic.typeservice.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

@EnableAsync
@Configuration
public class AsyncConfig {

    @Bean("cacheUpdateExecutor")
    public ThreadPoolTaskExecutor cacheExecutor(
            @Value("${async_core_pool_size}") int corePoolSize,
            @Value("${async_max_pool_size}") int maxPoolSize,
            @Value("${async_queue_capacity}") int queueCapacity,
            @Value("${keep_alive_seconds}") int keepAliveSeconds,
            @Value("${await_termination}") int waitTerminationSeconds
    ) {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(corePoolSize);
        executor.setMaxPoolSize(maxPoolSize);
        executor.setQueueCapacity(queueCapacity);
        executor.setKeepAliveSeconds(keepAliveSeconds);
        executor.setThreadNamePrefix("async-pool-cache");
        executor.setWaitForTasksToCompleteOnShutdown(true);
        executor.setAwaitTerminationSeconds(waitTerminationSeconds);
        executor.initialize();
        return executor;
    }

}
