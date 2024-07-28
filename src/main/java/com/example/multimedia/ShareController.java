package com.example.multimedia;

import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendAudio;
import org.telegram.telegrambots.meta.api.methods.send.SendDocument;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.send.SendPhoto;
import org.telegram.telegrambots.meta.api.objects.InputFile;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.io.File;

public class ShareController extends TelegramLongPollingBot {


    public void onActionImageButton() {

        sendImageToBot();
    }

    public void onActionAudioButton() {
        sendAudioToBot();
    }

    public void onActionPdfButton() {
        sendDocumentToBot("report.pdf");
    }

    public void onActionZipButton() {
        sendDocumentToBot("compress.zip");
    }

    @Override
    public String getBotUsername() {
        return "@Kmw01_bot";
    }

    @Override
    public String getBotToken() {
        return "7470010293:AAHHc_Uuu2E0Rwi2v7Y1L8zlW5zd09FRg_E";
    }

    @Override
    public void onUpdateReceived(Update update) {
    }

    public void sendMessageToBot(String messageText) {
        new Thread(() -> {
            SendMessage message = new SendMessage();
            message.setChatId("1220853665");
            message.setText(messageText);
            try {
                execute(message);
            } catch (TelegramApiException e) {
                e.printStackTrace();
            }
            System.out.println("send success");
        }).start();
    }

    public void sendImageToBot() {
        new Thread(() -> {
            SendPhoto photo = new SendPhoto();
            photo.setChatId("1220853665");
            photo.setPhoto(new InputFile(new File("image.png")));
            try {
                execute(photo);
            } catch (TelegramApiException e) {
                e.printStackTrace();
            }
            System.out.println("send success");
        }).start();
    }

    public void sendAudioToBot() {
        if (!new File("test.wav").exists()) {
            System.out.println("audio not found");
            return;
        }
        new Thread(() -> {
            SendAudio audio = new SendAudio();
            audio.setChatId("1220853665");
            audio.setAudio(new InputFile(new File("test.wav")));
            try {
                execute(audio);
            } catch (TelegramApiException e) {
                e.printStackTrace();
            }
            System.out.println("send success");
        }).start();
    }

    public void sendDocumentToBot(String filePath) {
        new Thread(() -> {
            SendDocument document = new SendDocument();
            document.setChatId("1220853665");
            document.setDocument(new InputFile(new File(filePath)));
            try {
                execute(document);
            } catch (TelegramApiException e) {
                e.printStackTrace();
            }
            System.out.println("send success");
        }).start();
    }
}
