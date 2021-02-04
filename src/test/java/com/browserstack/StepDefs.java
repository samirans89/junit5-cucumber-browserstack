package com.browserstack;

import com.browserstack.local.Local;
import io.cucumber.java.After;
import io.cucumber.java.Before;
import io.cucumber.java.Scenario;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.apache.commons.lang3.RandomStringUtils;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.junit.jupiter.api.Assertions;
import org.openqa.selenium.*;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;

import java.io.FileReader;
import java.net.URL;
import java.util.Iterator;
import java.util.Map;

public class StepDefs {

    private WebDriver webDriver;
    private Local local;

    private static final String PASSED = "passed";
    private static final String FAILED = "failed";

    @Before
    public void setUp(Scenario scenario) throws Exception {
        JSONObject config;
        JSONParser parser = new JSONParser();
        DesiredCapabilities caps = new DesiredCapabilities();
        if(System.getenv("caps")!= null) {
            config = (JSONObject) parser.parse(System.getenv("caps"));
        } else if(System.getProperty("parallel") != null) {
            config = ParallelTest.threadLocalValue.get();
        } else if(System.getProperty("config") != null) {
            config = (JSONObject) parser.parse(new FileReader(String.format("src/test/resources/config/%s", System.getProperty("config"))));
        } else {
            config = (JSONObject) parser.parse(new FileReader("src/test/resources/config/single.config.json"));
        }

        Map<String, String> commonCapabilities = (Map<String, String>) config.get("capabilities");
        Iterator it = commonCapabilities.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry pair = (Map.Entry)it.next();
            if(caps.getCapability(pair.getKey().toString()) == null){
                caps.setCapability(pair.getKey().toString(), pair.getValue().toString());
            }
        }
        caps.setCapability("name",scenario.getName());

        String username = System.getenv("BROWSERSTACK_USERNAME");
        if(username == null) {
            username = (String) config.get("user");
        }
        String accessKey = System.getenv("BROWSERSTACK_ACCESS_KEY");
        if(accessKey == null) {
            accessKey = (String) config.get("key");
        }

        if (caps.getCapability("browserstack.local")!= null && caps.getCapability("browserstack.local").equals("true")) {
            String localIdentifier = RandomStringUtils.randomAlphabetic(8);
            caps.setCapability("browserstack.localIdentifier", localIdentifier);
            local = new Local();
            Map<String, String> options = Utility.getLocalOptions();
            options.put("key", accessKey);
            options.put("localIdentifier", localIdentifier);
            local.start(options);
        }

        String URL = String.format("https://%s:%s@hub.browserstack.com/wd/hub", username, accessKey);
        webDriver = new RemoteWebDriver(new URL(URL), caps);
    }

    @When("I navigate to {string}")
    public void iNavigateTo(String url) {
        webDriver.get(url);
    }

    @And("I click on {string} link")
    public void iClickOnLink(String linkText) throws InterruptedException {
        webDriver.findElement(By.linkText(linkText)).click();
        Thread.sleep(1000);
    }

    @And("I type {string} in {string}")
    public void iTypeInInput(String text, String inputName) {
        webDriver.findElement(By.id(inputName)).sendKeys(text);
    }

    @And("I press Log In Button")
    public void iPressLogin() throws InterruptedException {
        webDriver.findElement(By.cssSelector(".Button_root__24MxS")).click();
        Thread.sleep(1000);
    }

    @And("I add two products to cart")
    public void iAddProductsToCart() {
        webDriver.findElement(By.cssSelector("#\\31 > .shelf-item__buy-btn")).click();
        webDriver.findElement(By.cssSelector("#\\32 > .shelf-item__buy-btn")).click();
    }

    @And("I click on Buy Button")
    public void iClickOnBuyButton() throws InterruptedException {
        webDriver.findElement(By.cssSelector(".buy-btn")).click();
        Thread.sleep(1000);
    }

    @And("I type {string} in Post Code")
    public void iTypeInPostCode(String postCode) {
        webDriver.findElement(By.cssSelector(".dynamic-form-field--postCode #provinceInput")).sendKeys(postCode);
    }

    @And("I click on Checkout Button")
    public void iClickOnCheckoutButton() throws InterruptedException {
        webDriver.findElement(By.id("checkout-shipping-continue")).click();
        Thread.sleep(1500);
        webDriver.findElement(By.cssSelector(".button")).click();
        Thread.sleep(1000);
    }


    @And("I press the Apple Vendor Filter")
    public void iPressTheAppleVendorFilter() throws InterruptedException {
        webDriver.findElement(By.cssSelector(".filters-available-size:nth-child(2) .checkmark")).click();
        Thread.sleep(1000);
    }

    @And("I order by lowest to highest")
    public void iOrderByLowestToHighest() throws InterruptedException {
        WebElement dropdown = webDriver.findElement(By.cssSelector("select"));
        dropdown.findElement(By.xpath("//option[. = 'Lowest to highest']")).click();
        Thread.sleep(1000);
    }

    @Then("I should see user {string} logged in")
    public void iShouldUserLoggedIn(String user) {
        try {
            String loggedInUser = webDriver.findElement(By.cssSelector(".username")).getText();
            Assertions.assertEquals(user, loggedInUser);
        } catch (NoSuchElementException e) {
            throw new AssertionError(user+" is not logged in");
        }
    }

    @Then("I should see {string} as Login Error Message")
    public void iShouldSeeAsLoginErrorMessage(String expectedMessage) {
        try {
            String errorMessage = webDriver.findElement(By.cssSelector(".api-error")).getText();
            Assertions.assertEquals(expectedMessage, errorMessage);
        } catch (NoSuchElementException e) {
            throw new AssertionError("Error in logging in");
        }
    }

    @Then("I should see no image loaded")
    public void iShouldSeeNoImageLoaded() {
        try {
            String src = webDriver.findElement(By.xpath("//img[@alt='iPhone 12']")).getAttribute("src");
            Assertions.assertEquals("", src);
        } catch (NoSuchElementException e) {
            throw new AssertionError("Error in logging in");
        }
    }

    @Then("I should see elements in list")
    public void iShouldSeeElementsInList() {
        WebElement element = null;
        try {
            element = webDriver.findElement(By.cssSelector("#__next > main > div > div > h2"));
            throw new AssertionError("There are no orders");
        } catch (NoSuchElementException e) {
            Assertions.assertNull(element);
        }
    }

    @Then("I should see Offer elements")
    public void iShouldSeeOfferElements() {
        try {
            WebElement element = webDriver.findElement(By.cssSelector(".pt-6"));
            Assertions.assertNotNull(element);
        } catch (NoSuchElementException e) {
            throw new AssertionError("There are no offers");
        }
    }

    @Then("I should see {int} items in the list")
    public void iShouldSeeItemsInTheList(int productCount) {
        try{
            String products = webDriver.findElement(By.cssSelector(".products-found > span")).getText();
            Assertions.assertEquals(products,productCount+" Product(s) found.");
        } catch (NoSuchElementException e) {
            throw new AssertionError("Error in page load");
        }
    }

    @Then("I should see prices in ascending order")
    public void iShouldSeePricesInAscendingOrder() {
        try {
            int secondElementPrice = Integer.parseInt(webDriver.findElement(By.cssSelector("#\\32 5 .val > b")).getText());
            int firstElementPrice = Integer.parseInt(webDriver.findElement(By.cssSelector("#\\31 9 .val > b")).getText());
            Assertions.assertTrue(secondElementPrice>firstElementPrice);
        } catch (NoSuchElementException e) {
            throw new AssertionError("Error in page load");
        }
    }

    @After
    public void teardown(Scenario scenario) throws Exception {
        if (scenario.isFailed()) {
            Utility.setSessionStatus(webDriver, FAILED, String.format("%s failed.", scenario.getName()));
        } else {
            Utility.setSessionStatus(webDriver, PASSED, String.format("%s passed.", scenario.getName()));
        }
        webDriver.quit();
        if(local!=null) local.stop();
    }

}
