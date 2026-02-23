package io.luxnet.notiontaskbot.service.impl;

import io.luxnet.notiontaskbot.model.Priority;
import io.luxnet.notiontaskbot.model.Task;
import io.luxnet.notiontaskbot.service.TaskService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class HardcodedTaskService implements TaskService {

    @Override
    public List<Task> getTasks() {
        return List.of(
                new Task("1", "Записатись до стоматолога", Priority.HIGH),
                new Task("2", "Купити продукти", Priority.MEDIUM),
                new Task("3", "Зробити code review", Priority.MEDIUM),
                new Task("4", "Прочитати главу книги", Priority.MEDIUM)
        );
    }
}