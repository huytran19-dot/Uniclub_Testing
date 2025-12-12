package com.uniclub.pages;

import com.uniclub.base.BasePage;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

/**
 * DashboardPage - Page Object for Dashboard page
 * URL: http://localhost:5174/dashboard
 */
public class DashboardPage extends BasePage {
    
    // Locators
    private final By dashboardTitle = By.xpath("//h1[contains(text(),'Dashboard')]");
    private final By welcomeMessage = By.cssSelector(".welcome-message");
    private final By userMenuButton = By.xpath("//header//button[contains(@class, 'p-2')]"); // User icon button in header
    private final By logoutButton = By.xpath("//button[contains(text(),'Đăng xuất')]");
    private final By sidebar = By.cssSelector("aside");
    private final By topbar = By.cssSelector("header");
    
    // Sidebar menu items
    private final By dashboardMenuItem = By.xpath("//a[contains(text(),'Dashboard')]");
    private final By productsMenuItem = By.xpath("//a[contains(text(),'Products') or contains(text(),'Sản phẩm')]");
    private final By ordersMenuItem = By.xpath("//a[contains(text(),'Orders') or contains(text(),'Đơn hàng')]");
    private final By usersMenuItem = By.xpath("//a[contains(text(),'Users') or contains(text(),'Người dùng')]");
    private final By categoriesMenuItem = By.xpath("//a[contains(text(),'Categories') or contains(text(),'Danh mục')]");
    
    // Constructor
    public DashboardPage(WebDriver driver) {
        super(driver);
    }
    
    /**
     * Check if dashboard is displayed
     */
    public boolean isDashboardDisplayed() {
        try {
            // Wait for URL to contain dashboard
            waitForUrlContains("dashboard");
            // Check if either title or sidebar is present
            return isDisplayed(sidebar) || getCurrentUrl().contains("dashboard");
        } catch (Exception e) {
            return false;
        }
    }
    
    /**
     * Check if dashboard title is displayed
     */
    public boolean isDashboardTitleDisplayed() {
        return isDisplayed(dashboardTitle);
    }
    
    /**
     * Get dashboard title text
     */
    public String getDashboardTitle() {
        try {
            return getText(dashboardTitle);
        } catch (Exception e) {
            return "";
        }
    }
    
    /**
     * Check if sidebar is displayed
     */
    public boolean isSidebarDisplayed() {
        return isDisplayed(sidebar);
    }
    
    /**
     * Check if topbar is displayed
     */
    public boolean isTopbarDisplayed() {
        return isDisplayed(topbar);
    }
    
    /**
     * Click logout button
     */
    public LoginPage clickLogout() {
        try {
            // Wait for topbar to be visible
            waitUtils.waitForElementVisible(topbar);
            
            // Find all buttons in header and click the last one (User menu)
            driver.findElements(By.xpath("//header//button")).stream()
                .reduce((first, second) -> second) // Get last button
                .ifPresent(button -> button.click());
            
            // Wait for dropdown to appear
            Thread.sleep(1000);
            
            // Click logout button
            click(logoutButton);
        } catch (Exception e) {
            System.err.println("Error during logout: " + e.getMessage());
            e.printStackTrace();
        }
        return new LoginPage(driver);
    }
    
    /**
     * Check if logout button is displayed
     */
    public boolean isLogoutButtonDisplayed() {
        return isDisplayed(logoutButton);
    }
    
    /**
     * Navigate to Products page
     */
    public void navigateToProducts() {
        click(productsMenuItem);
    }
    
    /**
     * Navigate to Orders page
     */
    public void navigateToOrders() {
        click(ordersMenuItem);
    }
    
    /**
     * Navigate to Users page
     */
    public void navigateToUsers() {
        click(usersMenuItem);
    }
    
    /**
     * Navigate to Categories page
     */
    public void navigateToCategories() {
        click(categoriesMenuItem);
    }
    
    /**
     * Check if on dashboard page
     */
    public boolean isOnDashboardPage() {
        return getCurrentUrl().contains("dashboard");
    }
    
    /**
     * Get current page URL
     */
    public String getPageUrl() {
        return getCurrentUrl();
    }
    
    /**
     * Wait for dashboard to load
     */
    public DashboardPage waitForDashboardToLoad() {
        waitForUrlContains("dashboard");
        waitForPageLoad();
        return this;
    }
    
    /**
     * Get welcome message
     */
    public String getWelcomeMessage() {
        try {
            return getText(welcomeMessage);
        } catch (Exception e) {
            return "";
        }
    }
    
    /**
     * Verify user is logged in
     */
    public boolean isUserLoggedIn() {
        return isOnDashboardPage() && (isSidebarDisplayed() || isTopbarDisplayed());
    }
}
