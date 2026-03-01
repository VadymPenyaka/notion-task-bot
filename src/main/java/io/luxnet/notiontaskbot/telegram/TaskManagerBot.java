package io.luxnet.notiontaskbot.telegram;

import io.luxnet.notiontaskbot.config.property.TelegramProperties;
import io.luxnet.notiontaskbot.telegram.handler.UpdateHandler;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.util.List;

@Slf4j
@Component
public class TaskManagerBot extends TelegramLongPollingBot {

    private final TelegramProperties telegramProperties;
    private final List<UpdateHandler> handlers;

    public TaskManagerBot(TelegramProperties telegramProperties, List<UpdateHandler> handlers) {
        super(telegramProperties.getToken());
        this.telegramProperties = telegramProperties;
        this.handlers = handlers;
    }

    @Override
    public String getBotUsername() {
        return telegramProperties.getUsername();
    }

    @Override
    public void onUpdateReceived(Update update) {
        log.info("Update received: {}", update.getUpdateId());
        handlers.stream()
                .filter(handler -> handler.supports(update))
                .findFirst()
                .ifPresentOrElse(
                        handler -> handler.handle(update),
                        () -> log.warn("No handler found for update id={}", update.getUpdateId())
                );
    }
}