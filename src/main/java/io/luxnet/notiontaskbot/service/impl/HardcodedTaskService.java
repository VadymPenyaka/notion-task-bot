package io.luxnet.notiontaskbot.service.impl;

import io.luxnet.notiontaskbot.service.TaskService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class HardcodedTaskService implements TaskService {

    @Override
    public List<String> getTasks() {
        return List.of(
                "1. Записатись до стоматолога",
                "2. Купити продукти",
                "3. Зробити code review",
                "4. Прочитати главу книги"
        );
    }
}