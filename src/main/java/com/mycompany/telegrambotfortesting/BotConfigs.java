package com.mycompany.telegrambotfortesting;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;

import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;

public class BotConfigs extends TelegramLongPollingBot {

    Socket c;
    DataOutputStream dout;
    BufferedReader in;
    private String token=""; // change to your bot token I DELETED the bot token
    private String botUsername="";  // write down here your bot username

    private void log(String first_name, String last_name, String user_id, String gelen, String giden) {
        System.out.println("\n ----------------------------");
        DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
        Date date = new Date();
        System.out.println(dateFormat.format(date) + " Message from " + first_name + " " + last_name + ". (id = " + user_id + ") InputText - " + gelen+" OutputText - "+giden);
    }

    @Override
    public void onUpdateReceived(Update update) {
        // We check if the update has a message and the message has text
        if (update.hasMessage() && update.getMessage().hasText()) {
            // Set variables
            String message_text = update.getMessage().getText();
            String chat_id = Long.toString(update.getMessage().getChatId());
            String replyM = "";
            try{      
                c = new Socket("localhost", 9999);
                dout = new DataOutputStream(c.getOutputStream());
                in = new BufferedReader(new InputStreamReader(c.getInputStream()));
                dout.writeUTF(message_text);
                dout.flush();
                replyM = in.readLine();
            } catch (IOException e) {
                System.out.println(e);
            }
            SendMessage message = SendMessage.builder()
                    .chatId(chat_id)
                    .text(replyM)
                    .build();
            log(update.getMessage().getChat().getFirstName(), update.getMessage().getChat().getLastName(), Long.toString(update.getMessage().getChat().getId()), message_text,replyM);
            try {
                execute(message); // Sending our message object to user
            } catch (TelegramApiException e) {
                e.printStackTrace();
            }
            try {
                c.close();
            } catch (IOException ex) {
                Logger.getLogger(BotConfigs.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    @Override
    public String getBotUsername() {
        // bot username
        return botUsername;
    }

    @Override
    public String getBotToken() {
        // bot token
        return token;
    }
}
