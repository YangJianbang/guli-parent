package com.atguigu.servicesta.schedule;

import com.atguigu.servicesta.service.DailyService;
import com.atguigu.servicesta.utils.DateUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.util.unit.DataUnit;

import javax.xml.crypto.Data;
import java.util.Date;

@Component
public class ScheduledTask {
    @Autowired
    DailyService dailyService;
    @Scheduled(cron = "0 0 1 * * ?")
    public void taskstadata(){
        Date date = DateUtil.addDays(new Date(), -1);
        String day = DateUtil.formatDate(date);
        dailyService.createStatisticsByDate(day);
    }
}
