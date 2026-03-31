package framework.base;

import drivers.DriverFactory;
import framework.config.ConfigReader;
import org.openqa.selenium.WebDriver;
import org.testng.ITestResult;
import org.testng.annotations.*;

import java.time.Duration;

public abstract class BaseTest {

    /**
     * Lấy driver từ DriverFactory
     */
    protected WebDriver getDriver() {
        return DriverFactory.getDriver();
    }

    /**
     * Setup trước mỗi test
     */
    @Parameters({"browser", "env"})
    @BeforeMethod(alwaysRun = true)
    public void setUp(
            @Optional("chrome") String browser,
            @Optional("dev") String env) {

        // Ưu tiên lấy từ command line (CI/Maven)
        String browserFromSystem = System.getProperty("browser");
        if (browserFromSystem != null && !browserFromSystem.isBlank()) {
            browser = browserFromSystem;
        }

        String envFromSystem = System.getProperty("env");
        if (envFromSystem != null && !envFromSystem.isBlank()) {
            env = envFromSystem;
        }

        // FIX LỖI CHÍNH: env rỗng -> default dev
        if (env == null || env.isBlank()) {
            env = "dev";
        }

        // Set env cho ConfigReader
        System.setProperty("env", env);
        ConfigReader.reset();

        ConfigReader config = ConfigReader.getInstance();

        // Khởi tạo driver (local hoặc grid)
        DriverFactory.initDriver(browser);

        // Config driver
        getDriver().manage().window().maximize();
        getDriver().manage().timeouts()
                .implicitlyWait(Duration.ofSeconds(config.getImplicitWait()));

        // Mở URL
        getDriver().get(config.getBaseUrl());

        System.out.println("🚀 Browser: " + browser + " | ENV: " + env);
    }

    /**
     * TearDown sau mỗi test
     */
    @AfterMethod(alwaysRun = true)
    public void tearDown(ITestResult result) {

        // Nếu fail thì chụp screenshot
        if (result.getStatus() == ITestResult.FAILURE) {
            DriverFactory.takeScreenshot(result.getName());
        }

        // Quit driver
        DriverFactory.quitDriver();
    }
}