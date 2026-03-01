package io.luxnet.notiontaskbot.task.model;

import java.time.LocalDate;

public record Task(String id, String name, Priority priority, Category category, LocalDate date, TaskStatus status) {
}