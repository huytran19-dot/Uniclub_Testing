package com.uniclub.utils;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

/**
 * ConfigReader - Utility class to read configuration from config.properties file
 */
public class ConfigReader {
    
    private static Properties properties;
    private static final String CONFIG_FILE_PATH = "config/config.properties";
    
    static {
        try {
            loadProperties();
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException("Failed to load config.properties file");
        }
    }
    
    /**
     * Load properties from config file
     */
    private static void loadProperties() throws IOException {
        properties = new Properties();
        FileInputStream fis = new FileInputStream(CONFIG_FILE_PATH);
        properties.load(fis);
        fis.close();
    }
    
    /**
     * Get property value by key
     */
    public static String getProperty(String key) {
        String value = properties.getProperty(key);
        if (value == null) {
            throw new RuntimeException("Property '" + key + "' not found in config.properties");
        }
        return value;
    }
    
    /**
     * Get property with default value
     */
    public static String getProperty(String key, String defaultValue) {
        return properties.getProperty(key, defaultValue);
    }
    
    // Convenience methods for common properties
    
    public static String getAdminUrl() {
        return getProperty("admin.url");
    }
    
    public static String getWebUrl() {
        return getProperty("web.url");
    }
    
    public static String getBackendUrl() {
        return getProperty("backend.url");
    }
    
    public static String getBrowser() {
        return getProperty("browser");
    }
    
    public static boolean isHeadless() {
        return Boolean.parseBoolean(getProperty("headless", "false"));
    }
    
    public static int getImplicitWait() {
        return Integer.parseInt(getProperty("implicit.wait", "10"));
    }
    
    public static int getExplicitWait() {
        return Integer.parseInt(getProperty("explicit.wait", "20"));
    }
    
    public static int getPageLoadTimeout() {
        return Integer.parseInt(getProperty("page.load.timeout", "30"));
    }
    
    public static String getAdminEmail() {
        return getProperty("admin.email");
    }
    
    public static String getAdminPassword() {
        return getProperty("admin.password");
    }
    
    public static String getUserEmail() {
        return getProperty("user.email");
    }
    
    public static String getUserPassword() {
        return getProperty("user.password");
    }
    
    public static boolean isScreenshotOnFailure() {
        return Boolean.parseBoolean(getProperty("screenshot.on.failure", "true"));
    }
    
    public static String getScreenshotPath() {
        return getProperty("screenshot.path");
    }
    
    public static String getReportPath() {
        return getProperty("report.path");
    }
}
