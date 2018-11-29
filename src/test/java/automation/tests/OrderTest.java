package automation.tests;

import automation.BaseTest;
import automation.utils.CustomReporter;
import automation.utils.Properties;
import org.openqa.selenium.By;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.testng.Assert;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import java.util.Date;

public class OrderTest extends BaseTest {
  private String prodName;
  private String prodPrice;
  private String prodQty;
  private int prodQtyStart;
  private int prodQtyEnd;


  @DataProvider(name = "Authentication")
  public Object [][] getloginData (){
    return new String [][] {
            {"webinar.test@gmail.com","Xcg7299bnSmMuRLp9ITw"}
    };
  }

  @Test
  public void checkSite() {
    CustomReporter.logAction("Open the site");
    driver.navigate().to(Properties.getBaseUrl());
    wait.until(ExpectedConditions.presenceOfElementLocated(By.id("main")));
  }
  @Test
  public void testOfOrder(){

    openTheSiteAndAllProducts ();

    openTheProductPageAndGetTheProductVariables();

    addProductToCartAndCheckOfAdding();

    checkCart();

    productDeliveryFirstPage ();

    productDeliverySecondPage ();

    productDeliveryLastPage();

    checkOrderAfterConfirmation ();

    checkProductFinishQuantity();
  }

  private void checkProductFinishQuantity(){
    driver.findElement(By.linkText("Blouse")).click();
    driver.findElement(By.cssSelector("a[href*='#product']")).click();

    prodQty = driver.findElement(By.id("product-details")).getAttribute("data-product");
    prodQtyEnd = prodQuantity(prodQty);

    Assert.assertTrue(prodQtyStart > prodQtyEnd);
    CustomReporter.logAction("Quantity of the product is "+prodQtyEnd+ " and was "+ prodQtyStart);
  }

  private void checkOrderAfterConfirmation (){
    Assert.assertTrue(driver.findElement(By
            .cssSelector("h3[class*='card-title']")).getText()
            .toLowerCase().contains("подтверждён"),"The message 'Ваш заказ подтверждён' is not shown");

    Assert.assertTrue(driver.findElement(By
            .cssSelector("div[class*='details'] span")).getText()
            .toLowerCase().startsWith(prodName.toLowerCase()),"The error name product");

    Assert.assertEquals(driver.findElement(By.cssSelector("div[class='col-xs-2'")).getText(),"1");

    Assert.assertEquals(prodPrice.toLowerCase(),
            driver.findElement(By.cssSelector("div[class*='text-xs-right'")).getText());

  }

  private void productDeliveryLastPage(){
    driver.findElement(By.cssSelector("button[name='confirmDeliveryOption']")).click();
    CustomReporter.logAction("Transition to the checkout");
    driver.findElement(By.cssSelector("input[id='payment-option-1']")).click();
    driver.findElement(By.cssSelector("input[id*='terms-and-conditions']")).click();
    driver.findElement(By.id("payment-confirmation")).click();
    CustomReporter.logAction("Order is finished");
  }

  private void productDeliverySecondPage () {
    driver.findElement(By.cssSelector("input[name='address1']")).sendKeys("Serafimovycha 19");
    driver.findElement(By.cssSelector("input[name='postcode']")).sendKeys("02152");
    driver.findElement(By.cssSelector("input[name='city']")).sendKeys("Kyiv");
    driver.findElement(By.cssSelector("button[name*='confirm']")).click();
  }

  private void productDeliveryFirstPage () {
    wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("a[class*='btn-primary']")));
    driver.findElement(By.cssSelector("a[class*='btn-primary']")).click();
    CustomReporter.logAction("Checkout the order");

    driver.findElement(By.cssSelector("input[name='firstname']")).sendKeys("TestName");
    driver.findElement(By.cssSelector("input[name='lastname']")).sendKeys("TestLastName");
    driver.findElement(By.cssSelector("input[name='email']")).sendKeys(randomEmail("h"));
    driver.findElement(By.cssSelector("button[name='continue']")).click();
    CustomReporter.logAction("Entered first page of the order");

  }

  private void checkCart () {
    Assert.assertEquals(prodName.toLowerCase(), driver
            .findElement(By.cssSelector("a[class*='label'")).getAttribute("text").toLowerCase());
    CustomReporter.logAction("Check the product name in the cart");
//    String priceProduct
    Assert.assertEquals(prodPrice.toLowerCase(),
            driver.findElement(By.cssSelector("span[class='value'")).getText());
    CustomReporter.logAction("Check the product price in the cart");
//    String qtyProduct
    Assert.assertEquals("1",
            driver.findElement(By.cssSelector("input[class*='form-control'")).getAttribute("value"));
    CustomReporter.logAction("Check the product quantity in the cart");

  }


  private void addProductToCartAndCheckOfAdding() {
    CustomReporter.logAction("Add the Blouse to the bascket");
    driver.findElement(By.cssSelector("button[class*='add-to-cart']")).click();
    Assert.assertTrue(driver.findElements(By
            .id("myModalLabel")).size()>0, "The product is not added to cart");
    CustomReporter.logAction("Go to the order");
    driver.findElement(By.cssSelector("a[class*='btn-primary']")).click();
  }

  private void openTheProductPageAndGetTheProductVariables(){
    CustomReporter.logAction("Open the Blouse product page");
    driver.findElement(By.linkText("Blouse")).click();

    prodName = driver.findElement(By.cssSelector("h1[itemprop='name']")).getText();
    prodPrice = driver.findElement(By.cssSelector("span[itemprop='price']")).getText();


    driver.findElement(By.cssSelector("a[href*='#product']")).click();
    prodQty = driver.findElement(By.id("product-details")).getAttribute("data-product");
    prodQtyStart = prodQuantity(prodQty);
    CustomReporter.logAction("Received quantity of product");
  }

  private void openTheSiteAndAllProducts (){
    CustomReporter.logAction("Open the site");
    driver.navigate().to(Properties.getBaseUrl());
    wait.until(ExpectedConditions.presenceOfElementLocated(By.id("main")));
    CustomReporter.logAction("Open the All products page");
    driver.findElement(By.cssSelector("a[class*='all-product-link']")).click();
  }

  private int prodQuantity (String s) {
    String[] prodQtyparts = s.split(",");
    for (int i = 0; i < prodQtyparts.length; i++) {
      String tt = prodQtyparts[i];
      if (tt.startsWith("\"quantity\":")) {

//        System.out.println(i + ":" + tt);
//        System.out.println(tt.substring(tt.indexOf(':')+1));
        return Integer.parseInt(tt.substring(tt.indexOf(':')+1));
      } else {
        continue;
      }
    }

    return 0;
  }
  private static String randomEmail(String nameStart) {
    Date dateCat = new Date();
    long dateCatlong = dateCat.getTime();
    return nameStart + "_" + dateCatlong+"@mailinator.com";

  }


}
