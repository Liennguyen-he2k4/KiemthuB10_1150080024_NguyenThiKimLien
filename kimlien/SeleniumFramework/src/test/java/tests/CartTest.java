package tests;

import framework.base.BaseTest;
import framework.config.ConfigReader;
import framework.pages.CartPage;
import framework.pages.InventoryPage;
import framework.pages.LoginPage;
import org.testng.Assert;
import org.testng.annotations.Test;

public class CartTest extends BaseTest {

    @Test(description = "Thêm 1 sản phẩm vào giỏ - kiểm tra badge số lượng")
    public void testAddItemToCart() {
        ConfigReader config = ConfigReader.getInstance();

        InventoryPage inventoryPage = new LoginPage(getDriver())
                .login(config.getUsername(), config.getPassword());

        inventoryPage.addFirstItemToCart();

        Assert.assertEquals(
                inventoryPage.getCartItemCount(),
                1,
                "Badge giỏ hàng phải hiện số 1"
        );
    }

    @Test(description = "Kiểm tra sản phẩm xuất hiện trong CartPage")
    public void testItemAppearsInCart() {
        ConfigReader config = ConfigReader.getInstance();

        CartPage cartPage = new LoginPage(getDriver())
                .login(config.getUsername(), config.getPassword())
                .addFirstItemToCart()
                .goToCart();

        Assert.assertEquals(
                cartPage.getItemCount(),
                1,
                "Giỏ hàng phải có 1 sản phẩm"
        );
    }

    @Test(description = "Xóa sản phẩm khỏi giỏ - giỏ trở thành rỗng")
    public void testRemoveItemFromCart() {
        ConfigReader config = ConfigReader.getInstance();

        CartPage cartPage = new LoginPage(getDriver())
                .login(config.getUsername(), config.getPassword())
                .addFirstItemToCart()
                .goToCart()
                .removeFirstItem();

        Assert.assertEquals(
                cartPage.getItemCount(),
                0,
                "Giỏ hàng phải rỗng sau khi xóa"
        );
    }
}