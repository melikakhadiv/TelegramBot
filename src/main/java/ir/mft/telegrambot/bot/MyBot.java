package ir.mft.telegrambot.bot;


import ir.mft.telegrambot.Test;
import ir.mft.telegrambot.common.TelegramScraper;
import ir.mft.telegrambot.config.ConfigFile;
import ir.mft.telegrambot.config.ConfigManager;
import ir.mft.telegrambot.model.entity.BotUser;
import ir.mft.telegrambot.model.BotUserService;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.ParseMode;
import org.telegram.telegrambots.meta.api.methods.groupadministration.GetChat;
import org.telegram.telegrambots.meta.api.methods.groupadministration.GetChatAdministrators;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.Chat;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.ArrayList;
import java.util.List;


public class MyBot extends TelegramLongPollingBot {
    //todo: messbah.a how to read chat list
    //todo: messbah.a how to read media


    public MyBot(String botToken) {
        super(botToken);
    }


    SendMessage sendMessage = new SendMessage();


    @Override
    public void onUpdateReceived(Update update) {
        Message message = update.getMessage();
        String command = message.getText();
        String userName = String.valueOf(message.getFrom().getUserName());
        String userId = String.valueOf(message.getFrom().getId());
        String chatId = String.valueOf(message.getChatId());
        String firstName = message.getFrom().getFirstName();
        String lastName = message.getFrom().getLastName();
        sendMessage.setChatId(chatId);
        System.out.println(command);
        System.out.println(userId);
        System.out.println(chatId);

// if bot is an administrator for channel
//        Chat chat = new Chat();
//        chat.setType("channel");
//        chat.setUserName("+7oY0qilxCPEyMTQ0");
//        message.setChat(chat);
//        update.getChannelPost().setChat(chat);
//        update.getMessage().setChat(chat);
//        update.getChannelPost().setChat(chat);
//        System.out.println("..... " + update.getChannelPost().getText());


        try {
            if (update.hasMessage()) {
                System.out.println("hasMessage");
                if (!registered(userId) && !command.equals("/signup")) {
                    System.out.println("check");
                    sendMessage.setText("You have to sign up first : " + "/signup");
                    execute(sendMessage);

                } else if (command.equals("/signup")) {
                    System.out.println("signUp");
                    System.out.println(chatId);
                    signUp(userName, userId, chatId, firstName, lastName);
                } else {
//                    sendMessage.setChatId(chatId);
//                    sendMessage.setText(firstName + " welcome to my bot!");
//                    execute(sendMessage);
                    sendMessage.setParseMode(ParseMode.MARKDOWN);
                    switch (command) {
                        case "/newchannelmessage":
                            System.out.println("Message");
                            sendNewMessage(chatId);
                            break;
                        case "/options":
                            System.out.println("option");
                            sendCustomKeyboard(chatId);
                            break;
                        case "Row 1 Button 1":
                            sendMessage.setText("1-1");
                            execute(sendMessage);
                            break;
                        case "Row 2 Button 1":
                            sendMessage.setText("2-1");
                            execute(sendMessage);
                            break;
                        case "Row 2 Button 2":
                            sendMessage.setText("2-2");
                            execute(sendMessage);
                            break;
                        case "/links":
                            System.out.println("links");
                            sendInlineKeyboard(chatId);
                            break;
                        case "/start":
                            System.out.println("start");
                            sendMessage.setText("Welcome back " + firstName + " !\nThe bot is already stared!");
                            execute(sendMessage);
                            break;
                        case "/restart":
                            System.out.println("restart");
                            sendMessage.setText("Restarting the Bot...\nPlease wait a few minutes before you try again!");
                            execute(sendMessage);
                            Test.main(null);
                            break;
                        default:
                            sendMessage.setText("Hi " + firstName + "! \n" +
                                    "Your '" + command + "' command is not recognized as an internal command"
                            );
                            System.out.println(sendMessage);
                            execute(sendMessage);
                    }

                }
            }
            //todo: messbah.a check - method wont invoke
            if (update.hasCallbackQuery()) {
                System.out.println("call back data");
                Message callBack = update.getCallbackQuery().getMessage();
                CallbackQuery callbackQuery = update.getCallbackQuery();
                String chat_id = callbackQuery.getMessage().getChat().getId().toString();
                String data = callbackQuery.getData();
                System.out.println(data);
                if (data.equals("button_1")) {
                    System.out.println(message);
                    sendMessage.setChatId(chat_id);
                    sendMessage.setText(ConfigManager.getKeys("URL_1"));
                    execute(sendMessage);
                } else if (data.equals("button_2")) {
                    System.out.println(message);
                    sendMessage.setChatId(chat_id);
                    sendMessage.setText(ConfigManager.getKeys("URL_2"));
                    execute(sendMessage);
                }
            }
        } catch (
                Exception e) {
            e.printStackTrace();
        }

    }


    public boolean registered(String userId) {
        try {
            return BotUserService.getService().findByUserId(userId) != null;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public void signUp(String userName, String userId, String chatId, String firstName, String lastName) {
        try {
            if (!registered(userId)) {
                sendMessage.setChatId(chatId);
                sendMessage.setText(firstName + " " + lastName + "\n" + "is signed up now!");
                execute(sendMessage);
                BotUser botUser = BotUser.builder()
                        .userName(userName)
                        .userId(userId)
                        .chatId(chatId)
                        .build();
                System.out.println(BotUserService.getService().save(botUser));
                sendMessage.setChatId(chatId);
                sendMessage.setText(firstName + " welcome to my bot!");
                execute(sendMessage);

            } else {
                sendMessage.setChatId(chatId);
                sendMessage.setText(firstName + " you have been signed up!");
                execute(sendMessage);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public void sendCustomKeyboard(String chatId) {
        sendMessage.setChatId(chatId);
        sendMessage.setText("Choose the option:");

        ReplyKeyboardMarkup keyboardMarkup = new ReplyKeyboardMarkup();
        int rowNumber = Integer.parseInt(ConfigManager.getKeys("MARKUP_ROW_NUMBER"));
        int buttonNumber = Integer.parseInt(ConfigManager.getKeys("MARKUP_BUTTON_NUMBER"));
        keyboardMarkup.setOneTimeKeyboard(true);
        keyboardMarkup.setSelective(true);
        List<KeyboardRow> keyboard = new ArrayList<>();
        KeyboardRow[] row = new KeyboardRow[rowNumber];

        for (int i = 0; i < rowNumber; i++) {
            row[i] = new KeyboardRow();
            for (int j = 0; j < buttonNumber; j++) {
                row[i].add(String.valueOf(j + 1));
            }
            keyboard.add(row[i]);
        }


//        row.add("Row 1 Button 1");
//        keyboard.add(row);
//
//        row = new KeyboardRow();
//        row.add("Row 2 Button 1 ");
//        row.add("Row 2 Button 2");
//        keyboard.add(row);


//        row = new KeyboardRow();
//        row.add("Row 3 Button 1");
//        row.add("Row 3 Button 2");
//        row.add("Row 3 Button 3");
//        keyboard.add(row);
//
//        row = new KeyboardRow();
//        row.add("Row 4 Button 1");
//        row.add("Row 4 Button 2");
//        row.add("Row 4 Button 3");
//        row.add("Row 4 Button 4");
//        keyboard.add(row);

        keyboardMarkup.setKeyboard(keyboard);
        sendMessage.setReplyMarkup(keyboardMarkup);
        try {
            execute(sendMessage);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }

    public void sendInlineKeyboard(String chatId) {
        sendMessage.setChatId(chatId);
        sendMessage.setText("Select your link:");

        InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();

        List<List<InlineKeyboardButton>> keyboard = new ArrayList<>();

        List<InlineKeyboardButton> button = new ArrayList<>();

        int count = Integer.parseInt(ConfigManager.getKeys("INLINE_NUMBER"));
        InlineKeyboardButton[] buttons = new InlineKeyboardButton[count];
        for (int i = 0; i < count; i++) {
            buttons[i] = new InlineKeyboardButton();
            buttons[i].setText(String.valueOf(i + 1));
            buttons[i].setCallbackData("button_" + (i + 1));
//            buttons[i].setUrl(ConfigManager.getKeys("URL_"+(i+1)));
            button.add(buttons[i]);
        }

//        InlineKeyboardButton button1 = new InlineKeyboardButton();
//        button1.setText("Button 1");
//        button1.setCallbackData("button1");
//        button1.setUrl(ConfigManager.getKeys("URL_1"));
//        button.add(button1);
//
//        InlineKeyboardButton button2 = new InlineKeyboardButton();
//        button2.setText("Button 2");
//        button2.setCallbackData("button2");
//        button2.setUrl(ConfigManager.getKeys("URL_2"));
//        Buttons.add(button2);

        keyboard.add(button);
        inlineKeyboardMarkup.setKeyboard(keyboard);

        sendMessage.setReplyMarkup(inlineKeyboardMarkup);
        try {
            execute(sendMessage);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }

    public void sendNewMessage(String chatId) {
        ConfigFile.loadConfig();
        sendMessage.setChatId(chatId);
        sendMessage.setText("The last message in '" + ConfigManager.getKeys("CHANNEL") + "' channel is :\n" + TelegramScraper.getNewMessage());

        try {
            execute(sendMessage);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }

    @Override
    public String getBotUsername() {
        return ConfigManager.getKeys("BOT_NAME");
    }
}
