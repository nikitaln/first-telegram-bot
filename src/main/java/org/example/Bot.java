package org.example;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class Bot extends TelegramLongPollingBot {

    private String pathScanFolder = "O:\\Печать в Печатном центре";

    private static Logger logger = LogManager.getLogger(Bot.class);

    private List<String> list1 = new ArrayList<>();
    private List<String> list2 = new ArrayList<>();


    private String folderDone = "Выполнено";
    private String txtFile1 = "!!!_Переименовывать файлы и папки только на своих компьютерах.txt";
    private String txtFile2 = "!!!_Присылать только файлы PDF.txt";
    private String docFile = "!_ПРОЧИТАТЬ правила.docx";
    private String dbTumbs = "Thumbs.db";
    private String hiddenFile = "~$ПРОЧИТАТЬ правила.docx";


    @Override
    public String getBotUsername() {
        return "gapu_print_bot";
    }


    @Override
    public String getBotToken() {
        return "6278798754:AAGwq2cW3AcxiKScf051aC9gfhAPbDvdnts";
    }


    //Метод для ответов в телеграм
    public void sendText(Long who, String what){
        SendMessage sm = SendMessage.builder()
                .chatId(who.toString())     //Who are we sending a message to
                .text(what).build();    //Message content

        try {
            execute(sm); //Actually sending the message
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
            logger.info("scan folder after /start");
            sendText(userId, "Добро пожаловать в бот");
            scanFolderProcess(userId);

        } else {
            logger.info("scan folder after any message");
            scanFolderProcess(userId);
        }
    }


    //Метод сканирования директории
    private void scanFolderProcess(long userId) {


        File file = new File(pathScanFolder);
        StringBuilder stringBuilder = new StringBuilder();

        int number = 0;

        logger.info("start scan folder in cycle 'while'");
        while (true) {

            try {
                Thread.sleep(2000);
                String[] files = file.list();
                list2.clear();

                for (int i = 0; i < files.length; i++) {

                    String fileName = files[i];

                    if (!fileName.equals(folderDone)
                            && !fileName.equals(txtFile1)
                            && !fileName.equals(txtFile2)
                            && !fileName.equals(docFile)
                            && !fileName.equals(dbTumbs)
                            && !fileName.equals(hiddenFile)) {

                        if (!list1.contains(fileName)) {
                            list2.add(fileName);
                        }
                    }
                }

                if (list2.size() > 0) {
                    for (String name : list2) {
                        number = number + 1;
                        if (name.length() > 30) {
                            String shortName = name.substring(0, 30);
                            stringBuilder.append(number + ".  " + shortName + "...\n");
                        } else {
                            stringBuilder.append(number + ".  " + name + "\n");
                        }
                    }
                    sendText(userId, "Новые заявки на печать:\n\n" + stringBuilder.toString());
                }

                stringBuilder.delete(0, stringBuilder.length());
                number = 0;

                list1.clear();
                for (int i = 0; i < files.length; i++) {
                    list1.add(files[i]);
                }

            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
