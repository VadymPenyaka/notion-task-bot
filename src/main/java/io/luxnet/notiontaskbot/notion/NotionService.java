package io.luxnet.notiontaskbot.notion;

import tools.jackson.databind.JsonNode;
import io.luxnet.notiontaskbot.config.property.NotionProperties;
import io.luxnet.notiontaskbot.task.model.Task;
import io.luxnet.notiontaskbot.task.model.TaskStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

@Service
@RequiredArgsConstructor
public class NotionService {

    private final NotionApiClient apiClient;
    private final NotionProperties notionProperties;
    private final NotionTaskMapper mapper;

    public List<Task> getTasks() {
        String today = LocalDate.now().toString();
        JsonNode response = apiClient.queryDatabase(notionProperties.getDatabaseId(), buildFilter(today));

        List<Task> tasks = new ArrayList<>();
        for (JsonNode page : response.get("results")) {
            mapper.map(page).ifPresent(tasks::add);
        }

        return tasks.stream()
                .sorted(Comparator.comparingInt(t -> t.priority().ordinal()))
                .toList();
    }

    public void updateStatus(String pageId, TaskStatus status) {
        String body = """
                {
                  "properties": {
                    "Task Status": {
                      "status": {"name": "%s"}
                    }
                  }
                }
                """.formatted(status.getNotionName());
        apiClient.updatePageStatus(pageId, body);
    }

    private String buildFilter(String today) {
        return """
                {
                  "filter": {
                    "or": [
                      {
                        "and": [
                          {"property": "Task Status", "status": {"equals": "TODO"}},
                          {"property": "Date", "date": {"before": "%s"}}
                        ]
                      },
                      {
                        "property": "Date",
                        "date": {"equals": "%s"}
                      }
                    ]
                  }
                }
                """.formatted(today, today);
    }
}