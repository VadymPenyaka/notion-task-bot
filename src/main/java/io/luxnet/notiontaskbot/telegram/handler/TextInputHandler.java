package io.luxnet.notiontaskbot.telegram.handler;

import lombok.extern.slf4j.Slf4j;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Update;

@Slf4j
@Component
@Order(100)
public class TextInputHandler implements UpdateHandler {

    @Override
    public boolean supports(Update update) {
        return update.hasMessage()
                && update.getMessage().hasText()
                && !update.getMessage().getText().startsWith("/");
    }

    @Override
    public void handle(Update update) {
        long chatId = update.getMessage().getChatId();
        String text = update.getMessage().getText();
        log.info("New task input from chatId={}: {}", chatId, text);
    }
}