package io.luxnet.notiontaskbot.task;

import io.luxnet.notiontaskbot.notion.NotionService;
import io.luxnet.notiontaskbot.task.model.Task;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TaskService {

    private final NotionService notionService;

    public List<Task> getTasks() {
        return notionService.getTasks();
    }
}