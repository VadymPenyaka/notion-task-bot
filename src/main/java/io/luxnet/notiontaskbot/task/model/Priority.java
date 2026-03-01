package io.luxnet.notiontaskbot.task.model;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum Priority {

    HIGH("⓵ 🔥", "🔴"),
    MEDIUM("⓶ ‼️", "🟡"),
    LOW("⓷ 👍🏻", "🟢");

    private final String notionIcon;
    private final String telegramIcon;

    public static Priority fromNotionIcon(String notionIcon) {
        for (Priority p : values()) {
            if (p.notionIcon.equals(notionIcon)) return p;
        }
        throw new IllegalArgumentException("Unknown priority: " + notionIcon);
    }
}