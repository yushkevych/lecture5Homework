package automation;

import com.beust.jcommander.Parameter;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.ie.InternetExplorerDriver;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Optional;
import org.testng.annotations.Parameters;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

public class BaseTest {
  protected WebDriver driver;
  protected WebDriverWait wait;

  private WebDriver getDriver(String browser) {
    switch (browser) {
      case "firefox":
        System.setProperty(
                "webdriver.gecko.driver",
//                new File(BaseTest.class.getResource("/geckodriver.exe").getFile()).getPath());
                System.getProperty("user.dir")+"//drivers//geckodriver.exe");
        return new FirefoxDriver();
      case "ie":
      case "internet explorer":
        System.setProperty(
                "webdriver.ie.driver",
//                new File(BaseTest.class.getResource("/IEDriverServer.exe").getFile()).getPath());
                System.getProperty("user.dir")+"//drivers//IEDriverServer.exe");
        return new InternetExplorerDriver();
      case "headless-chrome":
        System.setProperty(
                "webdriver.chrome.driver",
//                new File(BaseTest.class.getResource("/chromedriver.exe").getFile()).getPath());
                System.getProperty("user.dir")+"//drivers//chromedriver.exe");
        ChromeOptions options = new ChromeOptions();
        options.addArguments("headless");
        options.addArguments("window-size=800x600");
        return new ChromeDriver(options);
      case "remote-chrome":
        ChromeOptions optionsRemote = new ChromeOptions();
        optionsRemote.addArguments("headless");
        optionsRemote.addArguments("window-size=800x600");
        try {
          return new RemoteWebDriver(new URL("http://localhost:4444/wd/hub"), optionsRemote);
        } catch (MalformedURLException e) {
          e.printStackTrace();
        }
        return null;
      case "remote-firefox":
        FirefoxOptions firefoxOptions = new FirefoxOptions();
//        firefoxOptions.addArguments("--headless");
        try {
          return new RemoteWebDriver(new URL("http://localhost:4444/wd/hub"), firefoxOptions);
        } catch (MalformedURLException e) {
          e.printStackTrace();
        }
        return null;
      case "mobile":
        System.setProperty(
                "webdriver.chrome.driver",
//                new File(BaseTest.class.getResource("/chromedriver.exe").getFile()).getPath());
                System.getProperty("user.dir")+"//drivers//chromedriver.exe");
        Map<String,String> mobileEmulation = new HashMap<>();
        mobileEmulation.put("devicename","iPhone 6");

        ChromeOptions chromeOptions = new ChromeOptions();
        chromeOptions.setExperimentalOption("mobileEmulation", mobileEmulation);
        return new ChromeDriver(chromeOptions);
      case "chrome":
        default:
          System.setProperty(
                  "webdriver.chrome.driver",
//                  new File(BaseTest.class.getResource("/chromedriver.exe").getFile()).getPath());
                  System.getProperty("user.dir")+"//drivers//chromedriver.exe");
          return  new ChromeDriver();

    }
  }

  @BeforeClass
  @Parameters("selenium.browser")
  public void setUp(@Optional("remote-chrome") String browser){
    driver = getDriver(browser);
    driver.manage().timeouts().implicitlyWait(15, TimeUnit.SECONDS);
    driver.manage().timeouts().pageLoadTimeout(30, TimeUnit.SECONDS);
    driver.manage().window().maximize();

    wait = new WebDriverWait(driver, 5);
  }

  @AfterClass
  public void releaseDriver(){
    if (driver!= null) {
      driver.quit();
    }
  }
}
