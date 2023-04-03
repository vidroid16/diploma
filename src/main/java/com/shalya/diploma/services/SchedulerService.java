package com.shalya.diploma.services;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class SchedulerService {

    private final UpdateGoodsService updateGoodsService;
    private final RatingService ratingService;

    @Scheduled(cron = "0 30 4 * * ?")
    public void updateRatings(){
        log.info("Updating users ratings");
        ratingService.updateAllUsers();
    }
    public void updateGoods(){
        log.info("now is 5:00");
    }
}
