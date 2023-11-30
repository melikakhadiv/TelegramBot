package ir.mft.telegrambot;



import ir.mft.telegrambot.bot.MyBot;
import ir.mft.telegrambot.config.ConfigFile;
import ir.mft.telegrambot.config.ConfigManager;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;

public class Test {
    public static void main(String[] args) throws Exception {
        ConfigFile.loadConfig();
        try {
            System.out.println(ConfigManager.getKeys("CHANNEL"));
            TelegramBotsApi telegramBotsApi = new TelegramBotsApi(DefaultBotSession.class);
            telegramBotsApi.registerBot(new MyBot(ConfigManager.getKeys("BOT_TOKEN")));
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }
}
