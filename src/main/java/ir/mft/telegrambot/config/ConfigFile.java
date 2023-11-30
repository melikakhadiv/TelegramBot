package ir.mft.telegrambot.config;

import ir.mft.telegrambot.Test;
import org.apache.commons.io.IOUtils;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

public class ConfigFile {
    public static void loadConfig() {
        String fileName = "config.yml";
        ClassLoader classLoader = Test.class.getClassLoader();

        try (InputStream inputStream = classLoader.getResourceAsStream(fileName)) {
            String result = IOUtils.toString(inputStream , StandardCharsets.UTF_8);
            File file = new File(fileName);
            if (!file.exists()){
                file.createNewFile();
                BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(fileName));
                bufferedWriter.write(result);
                bufferedWriter.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
