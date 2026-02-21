package io.luxnet.notiontaskbot.telegram;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.bots.AbsSender;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.io.Serializable;

@Slf4j
@Service
@RequiredArgsConstructor
public class TelegramSender {

    private final AbsSender absSender;

    public <T extends Serializable> void send(BotApiMethod<T> method) {
        try {
            absSender.execute(method);
        } catch (TelegramApiException e) {
            log.error("Failed to execute {}: {}", method.getClass().getSimpleName(), e.getMessage());
        }
    }
}