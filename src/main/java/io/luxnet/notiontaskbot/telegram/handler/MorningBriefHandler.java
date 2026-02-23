package io.luxnet.notiontaskbot.telegram.handler;

import io.luxnet.notiontaskbot.model.Priority;
import io.luxnet.notiontaskbot.model.Task;
import io.luxnet.notiontaskbot.model.TaskStatus;
import io.luxnet.notiontaskbot.service.TaskService;
import io.luxnet.notiontaskbot.telegram.TelegramSender;
import lombok.RequiredArgsConstructor;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;

import java.util.List;

@Component
@Order(2)
@RequiredArgsConstructor
public class MorningBriefHandler implements UpdateHandler {

    private static final String COMMAND = "/brief";

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
        List<Task> tasks = taskService.getTasks();

        telegramSender.send(SendMessage.builder()
                .chatId(update.getMessage().getChatId())
                .text("📋 Список справ на день!")
                .replyMarkup(buildKeyboard(tasks))
                .build());
    }

    static InlineKeyboardMarkup buildKeyboard(List<Task> tasks) {
        List<List<InlineKeyboardButton>> rows = tasks.stream()
                .map(task -> List.of(
                        InlineKeyboardButton.builder()
                                .text(priorityIcon(task.priority()) + " " + task.name())
                                .callbackData(TaskStatus.TODO.callbackData(task.id()))
                                .build()
                ))
                .toList();

        return InlineKeyboardMarkup.builder()
                .keyboard(rows)
                .build();
    }

    static String priorityIcon(Priority priority) {
        return priority.getTelegramIcon();
    }
}