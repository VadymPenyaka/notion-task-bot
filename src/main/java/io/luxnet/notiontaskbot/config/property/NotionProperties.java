package io.luxnet.notiontaskbot.config.property;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Data
@Component
@ConfigurationProperties(prefix = "notion")
public class NotionProperties {
    private String apiToken;
    private String databaseId;
}
