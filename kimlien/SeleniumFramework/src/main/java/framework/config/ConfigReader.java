package framework.config;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

public class ConfigReader {
    private static ConfigReader instance;
    private final Properties properties = new Properties();

    private ConfigReader() {
        String env = System.getProperty("env");

        if (env == null || env.isBlank()) {
            env = "dev";
        }

        String filePath = "src/test/resources/config-" + env + ".properties";

        try (FileInputStream fis = new FileInputStream(filePath)) {
            properties.load(fis);
        } catch (IOException e) {
            throw new RuntimeException("Không tìm thấy file config: " + filePath, e);
        }
    }

    public static ConfigReader getInstance() {
        if (instance == null) {
            instance = new ConfigReader();
        }
        return instance;
    }

    public static void reset() {
        instance = null;
    }

    public String getProperty(String key) {
        String envKey = key.toUpperCase().replace(".", "_");
        String envValue = System.getenv(envKey);

        if (envValue != null && !envValue.isBlank()) {
            return envValue;
        }

        return properties.getProperty(key);
    }

    public String getBaseUrl() {
        return getProperty("base.url");
    }

    public long getImplicitWait() {
        return Long.parseLong(getProperty("implicit.wait"));
    }

    public long getExplicitWait() {
        return Long.parseLong(getProperty("explicit.wait"));
    }

    public String getScreenshotPath() {
        return getProperty("screenshot.path");
    }

    public String getUsername() {
        String envUsername = System.getenv("SAUCEDEMO_USERNAME");
        if (envUsername != null && !envUsername.isBlank()) {
            return envUsername;
        }
        return properties.getProperty("username");
    }

    public String getPassword() {
        String envPassword = System.getenv("SAUCEDEMO_PASSWORD");
        if (envPassword != null && !envPassword.isBlank()) {
            return envPassword;
        }
        return properties.getProperty("password");
    }
}