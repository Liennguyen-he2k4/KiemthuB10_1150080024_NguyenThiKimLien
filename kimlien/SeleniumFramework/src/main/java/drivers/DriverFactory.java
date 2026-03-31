package drivers;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.MutableCapabilities;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.remote.RemoteWebDriver;

import java.net.URL;

public class DriverFactory {
    private static final ThreadLocal<WebDriver> driver = new ThreadLocal<>();

    public static void initDriver(String browser) {
        String gridUrl = System.getProperty("grid.url");

        try {
            if (gridUrl != null && !gridUrl.isEmpty()) {
                driver.set(createRemoteDriver(browser, gridUrl));
            } else {
                driver.set(createLocalDriver(browser));
            }
        } catch (Exception e) {
            throw new RuntimeException("Failed to initialize driver: " + e.getMessage(), e);
        }
    }

    private static WebDriver createLocalDriver(String browser) {
        if (browser == null || browser.isEmpty()) {
            browser = "chrome";
        }

        switch (browser.toLowerCase()) {
            case "firefox":
                WebDriverManager.firefoxdriver().setup();
                FirefoxOptions firefoxOptions = new FirefoxOptions();
                firefoxOptions.addArguments("-headless");
                firefoxOptions.addArguments("--width=1920");
                firefoxOptions.addArguments("--height=1080");
                return new FirefoxDriver(firefoxOptions);

            case "chrome":
            default:
                WebDriverManager.chromedriver().setup();
                ChromeOptions chromeOptions = new ChromeOptions();
                chromeOptions.addArguments("--headless=new");
                chromeOptions.addArguments("--no-sandbox");
                chromeOptions.addArguments("--disable-dev-shm-usage");
                chromeOptions.addArguments("--window-size=1920,1080");
                return new ChromeDriver(chromeOptions);
        }
    }

    private static WebDriver createRemoteDriver(String browser, String gridUrl) throws Exception {
        MutableCapabilities options;

        if ("firefox".equalsIgnoreCase(browser)) {
            FirefoxOptions firefoxOptions = new FirefoxOptions();
            firefoxOptions.addArguments("-headless");
            firefoxOptions.addArguments("--width=1920");
            firefoxOptions.addArguments("--height=1080");
            options = firefoxOptions;
        } else {
            ChromeOptions chromeOptions = new ChromeOptions();
            chromeOptions.addArguments("--headless=new");
            chromeOptions.addArguments("--no-sandbox");
            chromeOptions.addArguments("--disable-dev-shm-usage");
            chromeOptions.addArguments("--window-size=1920,1080");
            options = chromeOptions;
        }

        return new RemoteWebDriver(new URL(gridUrl), options);
    }

    public static WebDriver getDriver() {
        return driver.get();
    }

    public static void quitDriver() {
        if (driver.get() != null) {
            driver.get().quit();
            driver.remove();
        }
    }

    public static void takeScreenshot(String testName) {
        try {
            String screenshotPath = framework.config.ConfigReader.getInstance().getScreenshotPath();
            java.io.File dir = new java.io.File(screenshotPath);
            if (!dir.exists()) dir.mkdirs();

            String timestamp = new java.text.SimpleDateFormat("yyyyMMdd_HHmmss").format(new java.util.Date());
            String fileName = testName + "_" + timestamp + ".png";

            java.io.File screenshot = ((org.openqa.selenium.TakesScreenshot) getDriver())
                    .getScreenshotAs(org.openqa.selenium.OutputType.FILE);

            java.nio.file.Files.copy(
                    screenshot.toPath(),
                    new java.io.File(screenshotPath + fileName).toPath()
            );

            System.out.println("[Screenshot] ✅ Đã lưu: " + screenshotPath + fileName);
        } catch (Exception e) {
            System.out.println("[Screenshot] ❌ Lỗi: " + e.getMessage());
        }
    }
}