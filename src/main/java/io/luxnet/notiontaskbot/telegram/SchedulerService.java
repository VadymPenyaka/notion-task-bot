package io.luxnet.notiontaskbot.telegram;

import io.luxnet.notiontaskbot.config.property.TelegramProperties;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class SchedulerService {

    private final BriefSender briefSender;
    private final TelegramProperties telegramProperties;

    @Scheduled(cron = "0 0 8 * * *")
    public void morningBrief() {
        log.info("Sending morning brief");
        briefSender.send(telegramProperties.getChatId());
    }
}