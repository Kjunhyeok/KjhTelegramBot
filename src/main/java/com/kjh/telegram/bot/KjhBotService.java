package com.kjh.telegram.bot;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.ApiContextInitializer;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.exceptions.TelegramApiRequestException;
import org.telegram.telegrambots.meta.generics.BotSession;

import javax.annotation.PostConstruct;

@Service
public class KjhBotService {

    @Value("${bot.name}")
    private String telegramBotName;

    @Value("${bot.key}")
    private String telegramBotKey;

    @PostConstruct
    private void init() {
        ApiContextInitializer.init();
        TelegramBotsApi api = new TelegramBotsApi();
        try {
            BotSession bot = api.registerBot(new KjhBot(telegramBotName, telegramBotKey));
        } catch (TelegramApiRequestException e) {
            e.printStackTrace();
        }
    }
}
