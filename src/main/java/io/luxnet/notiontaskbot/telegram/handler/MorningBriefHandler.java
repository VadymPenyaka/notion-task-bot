package io.luxnet.notiontaskbot.telegram.handler;

import io.luxnet.notiontaskbot.telegram.BriefSender;
import lombok.RequiredArgsConstructor;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Update;

@Component
@Order(2)
@RequiredArgsConstructor
public class MorningBriefHandler implements UpdateHandler {

    private static final String COMMAND = "/brief";

    private final BriefSender briefSender;

    @Override
    public boolean supports(Update update) {
        return update.hasMessage()
                && update.getMessage().hasText()
                && COMMAND.equals(update.getMessage().getText());
    }

    @Override
    public void handle(Update update) {
        briefSender.send(update.getMessage().getChatId());
    }
}