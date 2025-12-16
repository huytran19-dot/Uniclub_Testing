package com.uniclub.pages;

import com.uniclub.base.BasePage;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import java.util.List;

/**
 * CartPage - Page Object for Shopping Cart page
 * URL: http://localhost:5173/cart
 */
public class CartPage extends BasePage {
    
    // Locators
    private final By cartTitle = By.xpath("//h1[contains(text(),'Giỏ hàng') or contains(text(),'Shopping Cart')]");
    private final By cartItems = By.xpath("//div[contains(@class, 'cart-item')] | //div[contains(@class,'border')]//img[contains(@src,'http')]/..");
    private final By emptyCartMessage = By.xpath("//*[contains(text(),'Giỏ hàng trống') or contains(text(),'Giỏ hàng của bạn đang rỗng') or contains(text(),'Your cart is empty')]");
    private final By checkoutButton = By.xpath("//a[contains(@href,'/checkout')] | //button[contains(text(),'Thanh toán') or contains(text(),'Checkout')]");
    private final By continueShoppingButton = By.xpath("//a[contains(@href,'/products')] | //button[contains(text(),'Tiếp tục mua sắm') or contains(text(),'Continue Shopping')]");
    private final By itemQuantity = By.xpath("//input[@type='number']");
    private final By removeItemButton = By.xpath("//button[contains(@class, 'remove') or contains(text(),'Xóa')]");
    private final By totalPrice = By.xpath("//*[contains(text(),'Tổng') or contains(text(),'Total')]//following-sibling::*");
    
    // Constructor
    public CartPage(WebDriver driver) {
        super(driver);
    }
    
    /**
     * Check if on cart page
     */
    public boolean isOnCartPage() {
        return driver.getCurrentUrl().contains("/cart");
    }
    
    /**
     * Check if cart is empty
     */
    public boolean isCartEmpty() {
        return isDisplayed(emptyCartMessage);
    }
    
    /**
     * Get number of items in cart
     */
    public int getCartItemCount() {
        try {
            List<WebElement> items = driver.findElements(cartItems);
            return items.size();
        } catch (Exception e) {
            return 0;
        }
    }
    
    /**
     * Click checkout button
     */
    public void clickCheckout() {
        click(checkoutButton);
    }
    
    /**
     * Check if checkout button is enabled
     */
    public boolean isCheckoutButtonEnabled() {
        return isEnabled(checkoutButton);
    }
    
    /**
     * Click continue shopping
     */
    public void clickContinueShopping() {
        click(continueShoppingButton);
    }
    
    /**
     * Get total price
     */
    public String getTotalPrice() {
        return getText(totalPrice);
    }
    
    /**
     * Remove item from cart (first item)
     */
    public void removeFirstItem() {
        click(removeItemButton);
    }
    
    /**
     * Wait for cart to load
     */
    public void waitForCartToLoad() {
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}
