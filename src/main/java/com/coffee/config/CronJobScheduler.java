//package com.coffee.config;
//
//import com.coffee.entity.OrderMenuCategoryEntity;
//import com.coffee.repository.OrderMenuCategoryRepository;
//import lombok.RequiredArgsConstructor;
//import org.slf4j.LoggerFactory;
//import org.springframework.scheduling.annotation.Scheduled;
//import org.springframework.stereotype.Component;
//
//import java.util.List;
//import org.slf4j.Logger;
//
//@Component
//@RequiredArgsConstructor
//public class CronJobScheduler {
//
//    private final OrderMenuCategoryRepository repository;
//
//    private static final Logger log = LoggerFactory.getLogger(CronJobScheduler.class);
//
//    @Scheduled(fixedRate = 40000)  // runs every 40 seconds
//    public void runScheduledTask() {
//        log.info("[CronJobScheduler][runScheduledTask] Scheduled task started");
//        List<OrderMenuCategoryEntity> entity =  repository.findAll();
//        log.info("[CronJobScheduler][runScheduledTask] entity: " + entity);
//        log.info("[CronJobScheduler][runScheduledTask] Scheduled task completed");
//    }
//}
