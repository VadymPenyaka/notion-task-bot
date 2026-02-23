package io.luxnet.notiontaskbot.service;

import io.luxnet.notiontaskbot.model.Task;

import java.util.List;

public interface TaskService {

    List<Task> getTasks();
}