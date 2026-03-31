package com.apisentinel.config;

import com.apisentinel.job.ApiCheckJob;
import org.quartz.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Quartz 定时任务配置
 *
 * @author ApiSentinel Team
 */
@Configuration
public class QuartzConfig {

    /**
     * API检查任务 JobDetail
     */
    @Bean
    public JobDetail apiCheckJobDetail() {
        return JobBuilder.newJob(ApiCheckJob.class)
                .withIdentity("apiCheckJob")
                .storeDurably()
                .build();
    }

    /**
     * API检查任务 Trigger - 每分钟执行一次
     */
    @Bean
    public Trigger apiCheckJobTrigger() {
        SimpleScheduleBuilder scheduleBuilder = SimpleScheduleBuilder.simpleSchedule()
                .withIntervalInMinutes(1)
                .repeatForever();

        return TriggerBuilder.newTrigger()
                .forJob(apiCheckJobDetail())
                .withIdentity("apiCheckTrigger")
                .withSchedule(scheduleBuilder)
                .build();
    }
}
