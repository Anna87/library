package com.customer.java.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.scheduling.annotation.Scheduled;

public class Sender {
    @Autowired
    private JmsTemplate jmsTemplate;

    // @Scheduled(cron = "0 0 12 * * ?") //At 12:00 pm (noon) every day
    @Scheduled(cron = "20 * * * * ?") //Every minutes
    public void scheduleTask() {
        jmsTemplate.convertAndSend("testQueue", "xxx");
    }
}
