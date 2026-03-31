package tests;

import framework.base.BaseTest;
import framework.config.ConfigReader;
import framework.pages.CartPage;
import framework.pages.CheckoutPage;
import framework.pages.LoginPage;
import framework.utils.TestDataFactory;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.util.Map;

public class CheckoutTest extends BaseTest {

    @Test(description = "Checkout với dữ liệu ngẫu nhiên từ Faker - lần 1")
    public void testCheckoutWithFakerData() {
        ConfigReader config = ConfigReader.getInstance();

        Map<String, String> checkoutData = TestDataFactory.randomCheckoutData();

        System.out.println(">>> Dữ liệu Faker lần này:");
        System.out.println("    First Name : " + checkoutData.get("firstName"));
        System.out.println("    Last Name  : " + checkoutData.get("lastName"));
        System.out.println("    Postal Code: " + checkoutData.get("postalCode"));

        CartPage cartPage = new LoginPage(getDriver())
                .login(config.getUsername(), config.getPassword())
                .addFirstItemToCart()
                .goToCart();

        Assert.assertEquals(cartPage.getItemCount(), 1);

        CheckoutPage checkoutPage = cartPage.goToCheckout();
        checkoutPage.fillInfo(
                checkoutData.get("firstName"),
                checkoutData.get("lastName"),
                checkoutData.get("postalCode")
        );
        checkoutPage.clickContinue();

        Assert.assertTrue(
                getDriver().getCurrentUrl().contains("checkout-step-two"),
                "Chưa đến trang checkout step 2"
        );
    }

    @Test(description = "Chạy lần 2 - dữ liệu Faker khác lần 1")
    public void testCheckoutWithFakerDataRun2() {
        ConfigReader config = ConfigReader.getInstance();

        Map<String, String> checkoutData = TestDataFactory.randomCheckoutData();

        System.out.println(">>> Dữ liệu Faker lần này:");
        System.out.println("    First Name : " + checkoutData.get("firstName"));
        System.out.println("    Last Name  : " + checkoutData.get("lastName"));
        System.out.println("    Postal Code: " + checkoutData.get("postalCode"));

        CartPage cartPage = new LoginPage(getDriver())
                .login(config.getUsername(), config.getPassword())
                .addFirstItemToCart()
                .goToCart();

        CheckoutPage checkoutPage = cartPage.goToCheckout();
        checkoutPage.fillInfo(
                checkoutData.get("firstName"),
                checkoutData.get("lastName"),
                checkoutData.get("postalCode")
        );
        checkoutPage.clickContinue();

        Assert.assertTrue(
                getDriver().getCurrentUrl().contains("checkout-step-two"),
                "Chưa đến trang checkout step 2"
        );
    }
}