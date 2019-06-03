package com.customer.java.services;

import com.customer.java.models.Borrow;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.scheduling.annotation.Scheduled;

import java.util.Date;
import java.util.List;

public class Sender {
    @Autowired
    private JmsTemplate jmsTemplate;

    @Autowired
    BorrowService borrowService;

    @Scheduled(cron = "0 0 12 * * ?") //At 12:00 pm (noon) every day
    //@Scheduled(cron = "5 * * * * ?") //Every minutes
    public void scheduleTask() {

        Date date = new Date();
        List<Borrow> borrows = borrowService.getExpiredBorrow(date);
        for (Borrow borrow: borrows
             ) {
            Date d = borrow.getExpiredDate();
            jmsTemplate.convertAndSend("LibraryQueue", borrow);
        }
    }
}
