package io.luxnet.notiontaskbot.config;

import io.luxnet.notiontaskbot.config.property.TelegramProperties;
import io.luxnet.notiontaskbot.telegram.TaskManagerBot;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.telegram.telegrambots.bots.DefaultAbsSender;
import org.telegram.telegrambots.bots.DefaultBotOptions;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.bots.AbsSender;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.meta.generics.BotSession;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;

@Slf4j
@Configuration
public class TelegramConfig {

    @Bean
    @Primary
    public AbsSender telegramAbsSender(TelegramProperties telegramProperties) {
        return new DefaultAbsSender(new DefaultBotOptions(), telegramProperties.getToken()) {};
    }

    @Bean
    public BotSession telegramBotSession(TaskManagerBot bot) throws TelegramApiException {
        return new TelegramBotsApi(DefaultBotSession.class).registerBot(bot);
    }
}