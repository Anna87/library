package com.library.java.services;

import com.library.java.models.Borrow;
import lombok.RequiredArgsConstructor;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
@RequiredArgsConstructor
public class Sender {
    private final JmsTemplate jmsTemplate;
    private final BorrowService borrowService;

    @Scheduled(cron = "0 0 12 * * ?") //At 12:00 pm (noon) every day
    //@Scheduled(cron = "5 * * * * ?") //Every minutes
    public void scheduleTask() {
        final Date date = new Date();
        final List<Borrow> borrows = borrowService.getExpiredBorrow(date);
        for (Borrow borrow : borrows) {
            jmsTemplate.convertAndSend("library-queue", borrow);
        }
    }
}
