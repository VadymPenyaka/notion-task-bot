package io.luxnet.notiontaskbot.telegram.handler;

import io.luxnet.notiontaskbot.telegram.TelegramSender;
import lombok.RequiredArgsConstructor;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.DeleteMessage;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.util.stream.IntStream;

@Component
@Order(1)
@RequiredArgsConstructor
public class StartCommandHandler implements UpdateHandler {

    private static final String COMMAND = "/start";
    private static final int MESSAGES_TO_CLEAR = 100;

    private final TelegramSender telegramSender;

    @Override
    public boolean supports(Update update) {
        return update.hasMessage()
                && update.getMessage().hasText()
                && COMMAND.equals(update.getMessage().getText());
    }

    @Override
    public void handle(Update update) {
        long chatId = update.getMessage().getChatId();
        int currentMessageId = update.getMessage().getMessageId();

        // clear all messages before stat message
        clearMessages(chatId, currentMessageId - 1);

        telegramSender.send(SendMessage.builder()
                .chatId(chatId)
                .text("Привіт! Напиши /brief щоб побачити завдання на сьогодні.")
                .build());

        telegramSender.sendSilently(DeleteMessage.builder()
                .chatId(chatId)
                .messageId(currentMessageId)
                .build());
    }

    private void clearMessages(long chatId, int upToMessageId) {
        IntStream.rangeClosed(Math.max(1, upToMessageId - MESSAGES_TO_CLEAR), upToMessageId)
                .forEach(id -> telegramSender.sendSilently(DeleteMessage.builder()
                        .chatId(chatId)
                        .messageId(id)
                        .build()));
    }
}