package com.gms.config;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.concurrent.ConcurrentTaskScheduler;

/**
 *
 * @author vvthanh
 */
@Configuration
@EnableScheduling
@ConditionalOnProperty(value = "app.job.enabled", matchIfMissing = true, havingValue = "true")
public class SchedulerConfig {

    @Bean
    public TaskScheduler taskScheduler() {
        return new ConcurrentTaskScheduler(); //single threaded by default
    }

}
