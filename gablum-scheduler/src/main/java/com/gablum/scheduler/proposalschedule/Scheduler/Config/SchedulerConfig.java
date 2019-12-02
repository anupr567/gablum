package com.gablum.scheduler.proposalschedule.Scheduler.Config;

import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.scheduling.annotation.SchedulingConfigurer;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.scheduling.config.ScheduledTaskRegistrar;

import java.util.concurrent.ScheduledFuture;

@Configuration
public class SchedulerConfig implements SchedulingConfigurer {
    TaskScheduler taskScheduler;

    private ScheduledFuture<?> job;

    @Override
    public void configureTasks(ScheduledTaskRegistrar scheduledTaskRegistrar) {

        ThreadPoolTaskScheduler threadPoolTaskScheduler =new ThreadPoolTaskScheduler();

        threadPoolTaskScheduler.setPoolSize(10);// Set the pool of threads

        threadPoolTaskScheduler.setThreadNamePrefix("scheduler-thread");

        threadPoolTaskScheduler.initialize();

        this.taskScheduler=threadPoolTaskScheduler;
        scheduledTaskRegistrar.setTaskScheduler(threadPoolTaskScheduler);
    }
}
