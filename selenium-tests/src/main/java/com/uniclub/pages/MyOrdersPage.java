package com.uniclub.pages;

import com.uniclub.base.BasePage;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import java.util.List;

/**
 * MyOrdersPage - Page Object for User Orders page
 * URL: http://localhost:5173/orders
 */
public class MyOrdersPage extends BasePage {
    
    // Locators - Orders List Page
    private final By pageTitle = By.xpath("//h1[contains(text(),'Đơn hàng') or contains(text(),'Orders')]");
    private final By orderCards = By.xpath("//div[contains(@class, 'card') and contains(@class, 'p-6')]//a[contains(@href,'/orders/')]");
    private final By orderLinks = By.xpath("//a[contains(@href,'/orders/') and .//div[contains(text(),'Đơn hàng #')]]");
    private final By noOrdersMessage = By.xpath("//*[contains(text(),'Chưa có đơn hàng') or contains(text(),'No orders')]");
    private final By orderStatusBadge = By.xpath("//div[contains(@class,'rounded-full') and contains(@class,'text-xs')]");
    
    // Locators - Order Detail Page
    private final By orderDetailTitle = By.xpath("//div[contains(text(),'Chi tiết đơn hàng') or contains(text(),'Đơn hàng #')]");
    private final By viewDetailsButton = By.xpath("//button[contains(text(),'Xem chi tiết')]");
    
    // Locators - Cancel Order (on Order Detail Page)
    private final By cancelButton = By.xpath("//button[contains(text(),'Hủy đơn hàng') or contains(text(),'Cancel')]");
    private final By cancelSuccessMessage = By.xpath("//*[contains(text(),'Đơn hàng đã được hủy') or contains(text(),'Order cancelled')]");
    
    // Constructor
    public MyOrdersPage(WebDriver driver) {
        super(driver);
    }
    
    /**
     * Check if on my orders page
     */
    public boolean isOnMyOrdersPage() {
        return driver.getCurrentUrl().contains("/orders");
    }
    
    /**
     * Check if has orders
     */
    public boolean hasOrders() {
        try {
            // First check if "no orders" message is shown
            List<WebElement> noOrdersMsg = driver.findElements(noOrdersMessage);
            if (!noOrdersMsg.isEmpty() && noOrdersMsg.get(0).isDisplayed()) {
                return false;
            }
            
            // Check for order links
            List<WebElement> orders = driver.findElements(orderLinks);
            System.out.println("  Found " + orders.size() + " orders");
            return !orders.isEmpty();
        } catch (Exception e) {
            System.out.println("  Error checking orders: " + e.getMessage());
            return false;
        }
    }
    
    /**
     * Get number of orders
     */
    public int getOrderCount() {
        try {
            List<WebElement> orders = driver.findElements(orderLinks);
            return orders.size();
        } catch (Exception e) {
            return 0;
        }
    }
    
    /**
     * Get status of first order
     */
    public String getFirstOrderStatus() {
        try {
            List<WebElement> statusElements = driver.findElements(orderStatusBadge);
            if (!statusElements.isEmpty()) {
                return statusElements.get(0).getText();
            }
        } catch (Exception e) {
            // Ignore
        }
        return "";
    }
    
    /**
     * Click on first order to view details
     */
    public void clickFirstOrder() {
        try {
            List<WebElement> orders = driver.findElements(orderLinks);
            if (!orders.isEmpty()) {
                orders.get(0).click();
                Thread.sleep(2000);
            }
        } catch (Exception e) {
            System.err.println("Error clicking first order: " + e.getMessage());
        }
    }
    
    /**
     * Click view details for first order
     */
    public void viewFirstOrderDetails() {
        clickFirstOrder();
    }
    
    /**
     * Click cancel button (on order detail page)
     * Returns true if cancel button was clicked
     */
    public boolean cancelCurrentOrder() {
        try {
            List<WebElement> buttons = driver.findElements(cancelButton);
            if (!buttons.isEmpty() && buttons.get(0).isEnabled()) {
                buttons.get(0).click();
                Thread.sleep(1000);
                
                // Handle the confirm dialog (JavaScript confirm)
                try {
                    org.openqa.selenium.Alert alert = driver.switchTo().alert();
                    alert.accept();
                    System.out.println("  ✅ Confirmed cancel in alert");
                    Thread.sleep(2000);
                    return true;
                } catch (org.openqa.selenium.NoAlertPresentException e) {
                    // No alert, maybe already cancelled
                    return true;
                }
            }
        } catch (Exception e) {
            System.err.println("Error clicking cancel: " + e.getMessage());
        }
        return false;
    }
    
    /**
     * Click cancel button for first pending order (legacy method)
     */
    public void cancelFirstPendingOrder() {
        cancelCurrentOrder();
    }
    
    /**
     * Confirm cancel order (no-op as we handle it in cancelCurrentOrder)
     */
    public void confirmCancel() {
        // Alert confirmation is already handled in cancelCurrentOrder
    }
    
    /**
     * Check if cancel was successful
     */
    public boolean isCancelSuccessful() {
        try {
            // Check for alert with success message
            org.openqa.selenium.Alert alert = driver.switchTo().alert();
            String text = alert.getText();
            alert.accept();
            return text.contains("hủy thành công") || text.contains("cancelled");
        } catch (Exception e) {
            // Check for success message on page
            return isDisplayed(cancelSuccessMessage);
        }
    }
    
    /**
     * Check if cancel button is available
     */
    public boolean canCancelFirstOrder() {
        try {
            List<WebElement> buttons = driver.findElements(cancelButton);
            return !buttons.isEmpty() && buttons.get(0).isEnabled();
        } catch (Exception e) {
            return false;
        }
    }
    
    /**
     * Wait for orders page to load
     */
    public void waitForPageToLoad() {
        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}
