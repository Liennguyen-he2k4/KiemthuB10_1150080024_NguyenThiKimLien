package tests;

import framework.base.BaseTest;
import framework.config.ConfigReader;
import framework.pages.InventoryPage;
import framework.pages.LoginPage;
import org.testng.Assert;
import org.testng.annotations.Test;

public class LoginTest extends BaseTest {

    @Test(description = "Đăng nhập thành công với tài khoản hợp lệ")
    public void testLoginSuccess() {
        LoginPage loginPage = new LoginPage(getDriver());
        ConfigReader config = ConfigReader.getInstance();

        InventoryPage inventoryPage = loginPage.login(
                config.getUsername(),
                config.getPassword()
        );

        Assert.assertTrue(inventoryPage.isLoaded(), "Trang inventory chưa load!");
        Assert.assertTrue(getDriver().getCurrentUrl().contains("inventory"),
                "URL không chứa 'inventory'");
    }

    @Test(description = "Đăng nhập thất bại - sai mật khẩu")
    public void testLoginWrongPassword() {
        LoginPage loginPage = new LoginPage(getDriver());
        ConfigReader config = ConfigReader.getInstance();

        LoginPage result = loginPage.loginExpectingFailure(
                config.getUsername(),
                "wrongpass"
        );

        Assert.assertTrue(result.isErrorDisplayed(), "Không thấy thông báo lỗi!");
        Assert.assertTrue(result.getErrorMessage().contains("do not match"),
                "Nội dung lỗi không đúng: " + result.getErrorMessage());
    }

    @Test(description = "Đăng nhập thất bại - tài khoản bị khoá")
    public void testLoginLockedUser() {
        LoginPage loginPage = new LoginPage(getDriver());
        ConfigReader config = ConfigReader.getInstance();

        LoginPage result = loginPage.loginExpectingFailure(
                "locked_out_user",
                config.getPassword()
        );

        Assert.assertTrue(result.isErrorDisplayed(), "Không thấy thông báo lỗi!");
        Assert.assertTrue(result.getErrorMessage().contains("locked out"),
                "Nội dung lỗi không đúng: " + result.getErrorMessage());
    }

    @Test(description = "Đăng nhập thất bại - để trống username")
    public void testLoginEmptyUsername() {
        LoginPage loginPage = new LoginPage(getDriver());
        ConfigReader config = ConfigReader.getInstance();

        LoginPage result = loginPage.loginExpectingFailure(
                "",
                config.getPassword()
        );

        Assert.assertTrue(result.isErrorDisplayed(), "Không thấy thông báo lỗi!");
        Assert.assertTrue(result.getErrorMessage().contains("Username is required"),
                "Nội dung lỗi không đúng: " + result.getErrorMessage());
    }
}