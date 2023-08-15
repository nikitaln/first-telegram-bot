package org.example;

import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

public class Bot extends TelegramLongPollingBot {


    @Override
    public String getBotUsername() {
        return "java_test_byln_bot";
    }

    @Override
    public String getBotToken() {
        return "6278798754:AAGwq2cW3AcxiKScf051aC9gfhAPbDvdnts";
    }

    //Метод для ответов
    public void sendText(Long who, String what){
        SendMessage sm = SendMessage.builder()
                .chatId(who.toString()) //Who are we sending a message to
                .text(what).build();    //Message content
        try {
            execute(sm);                        //Actually sending the message
        } catch (TelegramApiException e) {
            throw new RuntimeException(e);      //Any error will be printed here
        }
    }


    //Метод по получению информации из Бота
    @Override
    public void onUpdateReceived(Update update) {
        Message message = update.getMessage();
        long userId = message.getFrom().getId();

        if (message.getText().equals("/start")) {
            sendText(userId, "Welcome to new bot by LN");
        } else {
            sendText(userId, "Write any message, please");
        }

        System.out.println(message.getText());
    }
}
