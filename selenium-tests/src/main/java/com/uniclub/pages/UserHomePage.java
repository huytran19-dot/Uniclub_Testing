package com.uniclub.pages;

import com.uniclub.base.BasePage;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

/**
 * UserHomePage - Page Object for User Home page
 * URL: http://localhost:5173/
 */
public class UserHomePage extends BasePage {
    
    // Locators - Header elements
    private final By logo = By.xpath("//a[@href='/']//h1[contains(text(),'UniClub')]");
    private final By searchButton = By.xpath("//button[.//*[local-name()='svg' and contains(@class, 'lucide-search')]]");
    private final By cartButton = By.xpath("//a[@href='/cart']");
    private final By cartCount = By.xpath("//a[@href='/cart']//span[contains(@class, 'absolute')]");
    // User menu button - shows as avatar button with user's initial when logged in
    private final By userMenuButton = By.xpath("//button[contains(@class, 'rounded-full') and contains(@class, 'text-white')]");
    // Login button - shows User icon when NOT logged in
    private final By loginButton = By.xpath("//button[.//*[local-name()='svg' and contains(@class, 'lucide-user')]]");
    // Multiple locators for logout button to handle different scenarios
    private final By logoutButton = By.xpath("//div[contains(text(),'Đăng xuất') or contains(text(),'logout') or contains(text(),'Logout') or contains(text(),'Log out')]");
    private final By logoutButtonAlt = By.xpath("//button[contains(text(),'Đăng xuất') or contains(text(),'logout')]");
    private final By logoutIconButton = By.xpath("//button[.//*[local-name()='svg' and contains(@class, 'lucide-log-out')]]");
    
    // Dropdown menu items
    private final By profileMenuItem = By.xpath("//div[contains(text(),'Tài khoản') or contains(text(),'Profile') or contains(text(),'Account')]");
    private final By ordersMenuItem = By.xpath("//div[contains(text(),'Đơn hàng') or contains(text(),'Orders')]");
    
    // Main content
    private final By heroSection = By.xpath("//section[contains(@class, 'hero')]");
    private final By productsSection = By.xpath("//div[contains(@class, 'products')]");
    
    // Constructor
    public UserHomePage(WebDriver driver) {
        super(driver);
    }
    
    /**
     * Check if home page is displayed
     */
    public boolean isHomePageDisplayed() {
        try {
            waitForUrlContains("localhost:5173");
            return isDisplayed(logo) || getCurrentUrl().equals("http://localhost:5173/");
        } catch (Exception e) {
            return false;
        }
    }
    
    /**
     * Check if logo is displayed
     */
    public boolean isLogoDisplayed() {
        return isDisplayed(logo);
    }
    
    /**
     * Check if user is logged in (user menu button visible)
     * When logged in: Avatar button with user's initial is displayed
     * When NOT logged in: User icon button is displayed
     */
    public boolean isUserLoggedIn() {
        try {
            // Check if avatar button is displayed (means user is logged in)
            return isDisplayed(userMenuButton);
        } catch (Exception e) {
            // If avatar button not found, check if login button exists
            try {
                return !isDisplayed(loginButton);
            } catch (Exception ex) {
                return false;
            }
        }
    }
    
    /**
     * Click on user menu button
     */
    public UserHomePage clickUserMenu() {
        click(userMenuButton);
        return this;
    }
    
    /**
     * Click logout button
     */
    public UserLoginPage clickLogout() {
        try {
            // Click user menu first
            clickUserMenu();
            
            // Wait for dropdown to appear
            Thread.sleep(1500);
            
            // Try multiple ways to find and click logout button
            try {
                click(logoutButton);
            } catch (Exception e1) {
                try {
                    click(logoutButtonAlt);
                } catch (Exception e2) {
                    try {
                        click(logoutIconButton);
                    } catch (Exception e3) {
                        // Last resort: try to find any element with logout text
                        driver.findElement(By.xpath("//*[contains(translate(text(), 'ĐĂNGXUẤT', 'đăngxuất'), 'đăng xuất')]")).click();
                    }
                }
            }
            
            // Small wait for logout action
            Thread.sleep(1000);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new UserLoginPage(driver);
    }
    
    /**
     * Navigate to profile page
     */
    public void navigateToProfile() {
        clickUserMenu();
        click(profileMenuItem);
    }
    
    /**
     * Navigate to orders page
     */
    public void navigateToOrders() {
        clickUserMenu();
        click(ordersMenuItem);
    }
    
    /**
     * Navigate to cart page
     */
    public void navigateToCart() {
        click(cartButton);
    }
    
    /**
     * Get cart count
     */
    public String getCartCount() {
        try {
            return getText(cartCount);
        } catch (Exception e) {
            return "0";
        }
    }
    
    /**
     * Check if on home page
     */
    public boolean isOnHomePage() {
        String url = getCurrentUrl();
        return url.equals("http://localhost:5173/") || 
               url.equals("http://localhost:5173") ||
               url.contains("localhost:5173/#");
    }
    
    /**
     * Wait for home page to load
     */
    public UserHomePage waitForHomePageToLoad() {
        waitForPageLoad();
        try {
            waitUtils.waitForElementVisible(logo, 10);
        } catch (Exception e) {
            // Logo might not be visible immediately
        }
        return this;
    }
    
    /**
     * Get logo text
     */
    public String getLogoText() {
        try {
            return getText(logo);
        } catch (Exception e) {
            return "";
        }
    }
    
    /**
     * Check if logout button is displayed in dropdown
     */
    public boolean isLogoutButtonDisplayed() {
        try {
            clickUserMenu();
            Thread.sleep(500);
            return isDisplayed(logoutButton);
        } catch (Exception e) {
            return false;
        }
    }
}
