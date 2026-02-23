package io.luxnet.notiontaskbot.model;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.Arrays;
import java.util.Optional;

@Getter
@RequiredArgsConstructor
public enum TaskStatus {

    TODO("done:"),
    DONE("undo:");

    private final String callbackPrefix;

    public TaskStatus toggle() {
        return this == TODO ? DONE : TODO;
    }

    public String callbackData(String taskId) {
        return callbackPrefix + taskId;
    }

    public String extractTaskId(String callbackData) {
        return callbackData.substring(callbackPrefix.length());
    }

    public static Optional<TaskStatus> fromCallbackData(String data) {
        return Arrays.stream(values())
                .filter(s -> data.startsWith(s.callbackPrefix))
                .findFirst();
    }
}