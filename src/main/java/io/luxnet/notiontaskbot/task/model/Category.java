package io.luxnet.notiontaskbot.task.model;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum Category {

    HOUSEHOLD("🏠 Побут"),
    SHOPPING("🛒 Покупки"),
    EVFF("📗 ЄВФФ"),
    SELF_CARE("💇🏻‍♂️ Догляд за собою"),
    SELF_DEVELOPMENT("🧘🏻 Саморозвиток"),
    WORK("👨🏻‍💻 Робота");

    private final String notionLabel;

    public static Category fromNotionLabel(String label) {
        for (Category c : values()) {
            if (c.notionLabel.equals(label)) return c;
        }
        throw new IllegalArgumentException("Unknown category: " + label);
    }
}