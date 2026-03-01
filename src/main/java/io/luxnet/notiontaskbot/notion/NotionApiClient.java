package io.luxnet.notiontaskbot.notion;

import tools.jackson.databind.JsonNode;
import io.luxnet.notiontaskbot.config.property.NotionProperties;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

@Component
public class NotionApiClient {

    private static final String NOTION_VERSION = "2022-06-28";

    private final RestClient restClient;

    public NotionApiClient(NotionProperties properties) {
        this.restClient = RestClient.builder()
                .baseUrl("https://api.notion.com/v1")
                .defaultHeader("Authorization", "Bearer " + properties.getApiToken())
                .defaultHeader("Notion-Version", NOTION_VERSION)
                .build();
    }

    public JsonNode queryDatabase(String databaseId, String body) {
        return restClient.post()
                .uri("/databases/{id}/query", databaseId)
                .contentType(MediaType.APPLICATION_JSON)
                .body(body)
                .retrieve()
                .body(JsonNode.class);
    }

    public void updatePageStatus(String pageId, String body) {
        restClient.patch()
                .uri("/pages/{id}", pageId)
                .contentType(MediaType.APPLICATION_JSON)
                .body(body)
                .retrieve()
                .toBodilessEntity();
    }
}