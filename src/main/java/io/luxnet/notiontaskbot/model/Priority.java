package io.luxnet.notiontaskbot.model;

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
}