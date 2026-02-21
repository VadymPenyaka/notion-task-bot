package io.luxnet.notiontaskbot.telegram.handler;

import io.luxnet.notiontaskbot.service.TaskService;
import io.luxnet.notiontaskbot.telegram.TelegramSender;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;

@Component
@RequiredArgsConstructor
public class TasksCommandHandler implements UpdateHandler {

    private static final String COMMAND = "/tasks";

    private final TaskService taskService;
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
        String body = String.join("\n", taskService.getTasks());

        telegramSender.send(SendMessage.builder()
                .chatId(chatId)
                .text("Список завдань:\n\n" + body)
                .build());
    }
}