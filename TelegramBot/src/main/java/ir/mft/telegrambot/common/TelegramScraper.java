package ir.mft.telegrambot.common;

import ir.mft.telegrambot.config.ConfigFile;
import ir.mft.telegrambot.config.ConfigManager;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.htmlunit.HtmlUnitDriver;

import java.util.List;
import java.util.concurrent.TimeUnit;

public class TelegramScraper {
    public static String getNewMessage() {
        ConfigFile.loadConfig();
//        System.setProperty("webdriver.chrome.driver", "C:\\Program Files (x86)\\Google\\Chrome\\Application\\chrome.exe");
//        WebDriver driver = new ChromeDriver();
        WebDriver driver = new HtmlUnitDriver();
        driver.get("https://t.me/s/" + ConfigManager.getKeys("CHANNEL"));
        driver.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);
        List<WebElement> elements = driver.findElements(By.className("tgme_widget_message_text"));
        String lastMessage = elements.get(elements.size() - 1).getText().toString();
        driver.quit();
        return lastMessage;
    }
}
