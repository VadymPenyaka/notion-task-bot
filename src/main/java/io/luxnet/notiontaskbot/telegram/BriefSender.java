package io.luxnet.notiontaskbot.telegram;

import io.luxnet.notiontaskbot.task.model.Task;
import io.luxnet.notiontaskbot.task.model.TaskStatus;
import io.luxnet.notiontaskbot.task.TaskService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

@Component
@RequiredArgsConstructor
public class BriefSender {

    private final TaskService taskService;
    private final TelegramSender telegramSender;

    public void send(long chatId) {
        List<Task> tasks = taskService.getTasks();

        telegramSender.send(SendMessage.builder()
                .chatId(chatId)
                .text("📋 Список справ на день!")
                .replyMarkup(buildKeyboard(tasks))
                .build());
    }

    static InlineKeyboardMarkup buildKeyboard(List<Task> tasks) {
        LocalDate today = LocalDate.now();

        List<Task> overdue = tasks.stream()
                .filter(t -> t.date().isBefore(today) && t.status() == TaskStatus.TODO)
                .sorted(Comparator.comparingInt(t -> t.priority().ordinal()))
                .toList();

        List<Task> todayTodo = tasks.stream()
                .filter(t -> t.date().isEqual(today) && t.status() == TaskStatus.TODO)
                .sorted(Comparator.comparingInt(t -> t.priority().ordinal()))
                .toList();

        List<Task> todayDone = tasks.stream()
                .filter(t -> t.date().isEqual(today) && t.status() == TaskStatus.DONE)
                .toList();

        List<List<InlineKeyboardButton>> rows = new ArrayList<>();

        for (Task t : todayTodo) {
            rows.add(List.of(InlineKeyboardButton.builder()
                    .text(t.priority().getTelegramIcon() + " " + t.name())
                    .callbackData(TaskStatus.TODO.callbackData(t.id()))
                    .build()));
        }

        for (Task t : todayDone) {
            rows.add(List.of(InlineKeyboardButton.builder()
                    .text("✅ " + t.name())
                    .callbackData(TaskStatus.DONE.callbackData(t.id()))
                    .build()));
        }

        if (!overdue.isEmpty()) {
            rows.add(List.of(InlineKeyboardButton.builder()
                    .text("─── Протерміновано ───")
                    .callbackData("noop")
                    .build()));
        }

        for (Task t : overdue) {
            rows.add(List.of(InlineKeyboardButton.builder()
                    .text(t.priority().getTelegramIcon() + " " + t.name())
                    .callbackData(TaskStatus.TODO.callbackData(t.id()))
                    .build()));
        }

        return InlineKeyboardMarkup.builder()
                .keyboard(rows)
                .build();
    }
}