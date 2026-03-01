package io.luxnet.notiontaskbot.telegram.handler;

import io.luxnet.notiontaskbot.task.model.Task;
import io.luxnet.notiontaskbot.task.model.TaskStatus;
import io.luxnet.notiontaskbot.task.TaskService;
import io.luxnet.notiontaskbot.telegram.TelegramSender;
import lombok.RequiredArgsConstructor;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.AnswerCallbackQuery;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageReplyMarkup;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

@Component
@Order(3)
@RequiredArgsConstructor
public class CallbackQueryHandler implements UpdateHandler {

    private final TaskService taskService;
    private final TelegramSender telegramSender;

    @Override
    public boolean supports(Update update) {
        return update.hasCallbackQuery();
    }

    @Override
    public void handle(Update update) {
        CallbackQuery callbackQuery = update.getCallbackQuery();

        TaskStatus.fromCallbackData(callbackQuery.getData())
                .ifPresent(status -> toggle(callbackQuery, status));
    }

    private void toggle(CallbackQuery callbackQuery, TaskStatus currentStatus) {
        if (!(callbackQuery.getMessage() instanceof Message message)) return;

        String taskId = currentStatus.extractTaskId(callbackQuery.getData());
        TaskStatus nextStatus = currentStatus.toggle();

        Task task = taskService.getTasks().stream()
                .filter(t -> t.id().equals(taskId))
                .findFirst()
                .orElse(null);

        if (task == null) return;

        List<InlineKeyboardButton> allButtons = message.getReplyMarkup().getKeyboard().stream()
                .map(List::getFirst)
                .map(button -> isButtonForTask(button, taskId) ? buildButton(task, nextStatus) : button)
                .toList();

        List<List<InlineKeyboardButton>> todoRows = allButtons.stream()
                .filter(b -> TaskStatus.fromCallbackData(b.getCallbackData())
                        .map(s -> s == TaskStatus.TODO).orElse(false))
                .sorted(Comparator.comparingInt(this::taskPriority))
                .map(List::of)
                .toList();

        List<List<InlineKeyboardButton>> doneRows = allButtons.stream()
                .filter(b -> TaskStatus.fromCallbackData(b.getCallbackData())
                        .map(s -> s == TaskStatus.DONE).orElse(false))
                .map(List::of)
                .toList();

        List<List<InlineKeyboardButton>> sortedRows = new ArrayList<>();
        sortedRows.addAll(todoRows);
        sortedRows.addAll(doneRows);

        telegramSender.send(EditMessageReplyMarkup.builder()
                .chatId(message.getChatId())
                .messageId(message.getMessageId())
                .replyMarkup(InlineKeyboardMarkup.builder().keyboard(sortedRows).build())
                .build());

        answerCallback(callbackQuery.getId());
    }

    private int taskPriority(InlineKeyboardButton button) {
        return TaskStatus.fromCallbackData(button.getCallbackData())
                .map(s -> s.extractTaskId(button.getCallbackData()))
                .flatMap(id -> taskService.getTasks().stream()
                        .filter(t -> t.id().equals(id))
                        .findFirst())
                .map(t -> t.priority().ordinal())
                .orElse(Integer.MAX_VALUE);
    }

    private boolean isButtonForTask(InlineKeyboardButton button, String taskId) {
        return TaskStatus.fromCallbackData(button.getCallbackData())
                .map(s -> s.extractTaskId(button.getCallbackData()).equals(taskId))
                .orElse(false);
    }

    private InlineKeyboardButton buildButton(Task task, TaskStatus status) {
        String text = status == TaskStatus.DONE
                ? "✅ " + task.name()
                : task.priority().getTelegramIcon() + " " + task.name();

        return InlineKeyboardButton.builder()
                .text(text)
                .callbackData(status.callbackData(task.id()))
                .build();
    }

    private void answerCallback(String callbackQueryId) {
        telegramSender.send(AnswerCallbackQuery.builder()
                .callbackQueryId(callbackQueryId)
                .build());
    }
}