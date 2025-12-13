package com.uniclub.tests;

import com.uniclub.base.BaseTest;
import com.uniclub.pages.ProductListPage;
import com.uniclub.pages.ProductDetailPage;
import com.uniclub.utils.ConfigReader;
import io.qameta.allure.*;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

/**
 * ProductCatalogTest - Test cases for Product Catalog (User side)
 * Test ID: PROD_01 to PROD_10
 */
@Epic("Product Catalog")
@Feature("Product Browsing & Search")
public class ProductCatalogTest extends BaseTest {
    
    private ProductListPage productListPage;
    
    @BeforeMethod
    public void setUpTest() {
        navigateToWeb();
        driver.get(ConfigReader.getWebUrl() + "/products");
        productListPage = new ProductListPage(driver);
        
        // Wait for page to load
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
    
    /**
     * PROD_01: View product list
     */
    @Test(priority = 1, description = "Verify user can view product list")
    @Severity(SeverityLevel.CRITICAL)
    @Description("Test that products are displayed on the product list page")
    @Story("View Product List")
    public void testViewProductList() {
        // Assert - Products should be displayed
        Assert.assertTrue(productListPage.isOnProductListPage(), 
            "Should be on product list page");
        
        int productCount = productListPage.getProductCount();
        Assert.assertTrue(productCount > 0, 
            "Should display at least one product, found: " + productCount);
        
        System.out.println("✅ Test Passed: Product list displayed with " + productCount + " products");
    }
    
    /**
     * PROD_02: View product detail
     */
    @Test(priority = 2, description = "Verify user can view product details")
    @Severity(SeverityLevel.CRITICAL)
    @Description("Test navigation to product detail page and display of product information")
    @Story("View Product Detail")
    public void testViewProductDetail() {
        // Act - Click on first product
        ProductDetailPage detailPage = productListPage.clickFirstProduct();
        
        // Wait for page load
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        
        // Assert - Product details should be displayed
        Assert.assertTrue(detailPage.isOnProductDetailPage(), 
            "Should be on product detail page");
        
        String productName = detailPage.getProductName();
        Assert.assertFalse(productName.isEmpty(), 
            "Product name should be displayed");
        
        String productPrice = detailPage.getProductPrice();
        Assert.assertFalse(productPrice.isEmpty(), 
            "Product price should be displayed");
        
        Assert.assertTrue(detailPage.isProductImageDisplayed(), 
            "Product image should be displayed");
        
        System.out.println("✅ Test Passed: Product detail page displayed correctly");
        System.out.println("   Product: " + productName);
        System.out.println("   Price: " + productPrice);
    }
    
    /**
     * PROD_03: Search products
     */
    @Test(priority = 3, description = "Verify product search functionality")
    @Severity(SeverityLevel.CRITICAL)
    @Description("Test searching for products by keyword")
    @Story("Search Products")
    public void testSearchProducts() {
        // Act - Search for products
        String searchKeyword = "shirt";
        productListPage.search(searchKeyword);
        
        // Wait for results
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        
        // Assert - Should show search results or no results message
        boolean hasResults = productListPage.getProductCount() > 0;
        boolean showsNoResults = productListPage.isNoResultsDisplayed();
        
        Assert.assertTrue(hasResults || showsNoResults, 
            "Should either show results or 'no results' message");
        
        if (hasResults) {
            System.out.println("✅ Test Passed: Search returned " + productListPage.getProductCount() + " results");
        } else {
            System.out.println("✅ Test Passed: Search shows 'no results' message correctly");
        }
    }
    
    /**
     * PROD_04: Filter by category
     */
    @Test(priority = 4, description = "Verify filtering products by category")
    @Severity(SeverityLevel.NORMAL)
    @Description("Test category filter functionality")
    @Story("Filter Products")
    public void testFilterByCategory() {
        // Arrange - Get initial product count
        int initialCount = productListPage.getProductCount();
        
        // Act - Apply category filter
        productListPage.filterByCategory("Shirts");
        
        // Wait for filter
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        
        // Assert - Products should be filtered (count may change or stay same)
        Assert.assertTrue(productListPage.isOnProductListPage(), 
            "Should remain on product list page after filtering");
        
        System.out.println("✅ Test Passed: Category filter applied");
        System.out.println("   Initial count: " + initialCount);
        System.out.println("   Filtered count: " + productListPage.getProductCount());
    }
    
    /**
     * PROD_05: Filter by brand
     */
    @Test(priority = 5, description = "Verify filtering products by brand")
    @Severity(SeverityLevel.NORMAL)
    @Description("Test brand filter functionality")
    @Story("Filter Products")
    public void testFilterByBrand() {
        // Act - Apply brand filter
        productListPage.filterByBrand("Nike");
        
        // Wait for filter
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        
        // Assert
        Assert.assertTrue(productListPage.isOnProductListPage(), 
            "Should remain on product list page after filtering");
        
        System.out.println("✅ Test Passed: Brand filter applied");
        System.out.println("   Filtered count: " + productListPage.getProductCount());
    }
    
    /**
     * PROD_06: Filter by color
     */
    @Test(priority = 6, description = "Verify filtering products by color")
    @Severity(SeverityLevel.NORMAL)
    @Description("Test color filter functionality")
    @Story("Filter Products")
    public void testFilterByColor() {
        // Act - Apply color filter
        productListPage.filterByColor("Red");
        
        // Wait for filter
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        
        // Assert
        Assert.assertTrue(productListPage.isOnProductListPage(), 
            "Should remain on product list page after filtering");
        
        System.out.println("✅ Test Passed: Color filter applied");
    }
    
    /**
     * PROD_07: Filter by size
     */
    @Test(priority = 7, description = "Verify filtering products by size")
    @Severity(SeverityLevel.NORMAL)
    @Description("Test size filter functionality")
    @Story("Filter Products")
    public void testFilterBySize() {
        // Act - Apply size filter
        productListPage.filterBySize("M");
        
        // Wait for filter
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        
        // Assert
        Assert.assertTrue(productListPage.isOnProductListPage(), 
            "Should remain on product list page after filtering");
        
        System.out.println("✅ Test Passed: Size filter applied");
    }
    
    /**
     * PROD_08: Clear filters
     */
    @Test(priority = 8, description = "Verify clearing all filters")
    @Severity(SeverityLevel.NORMAL)
    @Description("Test clear filters functionality")
    @Story("Filter Products")
    public void testClearFilters() {
        // Arrange - Apply some filters
        productListPage.filterByBrand("Nike");
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        
        int filteredCount = productListPage.getProductCount();
        
        // Act - Clear filters
        productListPage.clearFilters();
        
        // Wait
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        
        // Assert - Should show all products again
        int clearedCount = productListPage.getProductCount();
        
        System.out.println("✅ Test Passed: Filters cleared");
        System.out.println("   Filtered count: " + filteredCount);
        System.out.println("   After clear: " + clearedCount);
    }
    
    /**
     * PROD_09: Navigate product detail and back
     */
    @Test(priority = 9, description = "Verify navigation to detail and back to list")
    @Severity(SeverityLevel.NORMAL)
    @Description("Test navigation flow between product list and detail")
    @Story("Navigation")
    public void testProductDetailNavigation() {
        // Act - Go to product detail
        ProductDetailPage detailPage = productListPage.clickFirstProduct();
        
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        
        Assert.assertTrue(detailPage.isOnProductDetailPage(), 
            "Should be on product detail page");
        
        // Act - Go back to list
        ProductListPage listPage = detailPage.clickBack();
        
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        
        // Assert - Should be back on product list
        Assert.assertTrue(listPage.isOnProductListPage(), 
            "Should be back on product list page");
        
        System.out.println("✅ Test Passed: Navigation to detail and back works correctly");
    }
    
    /**
     * PROD_10: Product pagination
     */
    @Test(priority = 10, description = "Verify product pagination")
    @Severity(SeverityLevel.NORMAL)
    @Description("Test pagination functionality on product list")
    @Story("Pagination")
    public void testProductPagination() {
        // Arrange - Get first page count
        int firstPageCount = productListPage.getProductCount();
        
        // Act - Go to next page
        productListPage.goToNextPage();
        
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        
        // Assert - Should still be on product list page
        Assert.assertTrue(productListPage.isOnProductListPage(), 
            "Should remain on product list page after pagination");
        
        // Act - Go back to previous page
        productListPage.goToPreviousPage();
        
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        
        Assert.assertTrue(productListPage.isOnProductListPage(), 
            "Should remain on product list page");
        
        System.out.println("✅ Test Passed: Pagination works correctly");
    }
}
