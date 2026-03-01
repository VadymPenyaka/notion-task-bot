package io.luxnet.notiontaskbot.notion;

import tools.jackson.databind.JsonNode;
import io.luxnet.notiontaskbot.task.model.Category;
import io.luxnet.notiontaskbot.task.model.Priority;
import io.luxnet.notiontaskbot.task.model.Task;
import io.luxnet.notiontaskbot.task.model.TaskStatus;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.Optional;

@Slf4j
@Component
public class NotionTaskMapper {

    public Optional<Task> map(JsonNode page) {
        try {
            String id = page.get("id").asText();
            JsonNode props = page.get("properties");

            String name = props.get("Name").get("title").get(0).get("plain_text").asText();
            Priority priority = Priority.fromNotionIcon(props.get("Priority").get("select").get("name").asText());
            Category category = Category.fromNotionLabel(props.get("Category").get("select").get("name").asText());
            LocalDate date = LocalDate.parse(props.get("Date").get("date").get("start").asText());
            TaskStatus status = TaskStatus.fromNotionName(props.get("Task Status").get("status").get("name").asText());

            return Optional.of(new Task(id, name, priority, category, date, status));
        } catch (Exception e) {
            log.warn("Skipping page {}: {}", page.get("id"), e.getMessage());
            return Optional.empty();
        }
    }
}