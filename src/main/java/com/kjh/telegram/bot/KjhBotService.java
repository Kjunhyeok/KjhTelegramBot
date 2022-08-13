package com.kjh.telegram.bot;

import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.meta.generics.BotSession;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;

import javax.annotation.PostConstruct;

@Service
public class KjhBotService {

    private String telegramBotName;

    private String telegramBotKey;

    @PostConstruct
    private void init() {
        try {
            TelegramBotsApi api = new TelegramBotsApi(DefaultBotSession.class);
            BotSession bot = api.registerBot(new KjhBot("junhKangBot", ""));
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }
}
