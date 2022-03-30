package placelab.tests;

import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.testng.Assert;
import org.testng.annotations.*;
import placelab.utilities.WebDriverSetup;

public class LoginTest {
    public WebDriver driver;
    private String host = System.getProperty("host");
    private String homePageUrl = System.getProperty("homepage");
    private String password = System.getProperty("password");
    private String username = System.getProperty("username");
    private String user = "Damir Čović";
    private String forgotPassUrl = "https://demo.placelab.com/password/forgot";
    private String termsUrl = "https://demo.placelab.com/terms_of_service";

    //Specify the driver and browser that will be used for this scenario

    @Parameters({"browser"})

    @BeforeTest (alwaysRun = true, groups = {"Positive, Negative"}, description = "Verify that user" +
            "is able to open PlaceLab web app.")
    public void openApp(String browser) {

        driver = WebDriverSetup.getWebDriver (browser);

        //Go to PlaceLab demo app
        driver.navigate().to(host);

        //Validate that user is redirected to the right page
        Assert.assertEquals(driver.getCurrentUrl(), host);
        Assert.assertEquals(driver.getTitle(), "PlaceLab");

        WebElement logo = driver.findElement(By.xpath("//div[@id='login']/img"));
        boolean logoPresent = logo.isDisplayed();
        Assert.assertTrue(logoPresent);

    }

    //Actual test implementation

    @Test (priority = 6, groups = ("Positive"), description = "This test verifies that user " +
            "is able to log in to PlaceLab with valid credentials", suiteName = "Smoke Test")

    public void testLoginPage() {

        //fill out login parameters
        WebElement enterUsername = driver.findElement(By.id("email"));
        enterUsername.sendKeys(username);

        WebElement enterPassword = driver.findElement(By.id("password"));
        enterPassword.sendKeys(password);

        //Click on Login button
        WebElement submit = driver.findElement(By.xpath("//input[@type='submit']"));
        submit.click();

        //Validate that user is successfully logged in
        Assert.assertEquals(driver.getCurrentUrl(),homePageUrl);

        try {
            WebElement userName = driver.findElement(By.id("user-name"));
            assert (userName.getText().contains(user));
        } catch (RuntimeException e) {
            e.printStackTrace();
            throw new RuntimeException("Expected user is not logged in!");
        }

        WebElement role = driver.findElement(By.xpath("//*[@id='user-role']"));
        Assert.assertEquals(role.getText(), "Group Admin");

        WebElement dropDownMenu = driver.findElement(By.xpath("//*[@id='actions-nav-item']"));
        dropDownMenu.click();

        WebElement signOut = driver.findElement(By.linkText("Sign out"));
        signOut.click();

    }


    @Test(priority = 1, groups = ("Negative"), description = "This test verifies that" +
            "user can not log in with empty password field." )

    public void testLoginEmptyPassword() {
        //fil out only e-mail parameters
        WebElement enterUsername = driver.findElement(By.id("email"));
        enterUsername.sendKeys(username);

        //Click on Login button
        WebElement submit = driver.findElement(By.xpath("//input[@type='submit']"));
        submit.click();

    }

    @Test(priority = 2, groups = ("Negative"), description = "This test verifies that" +
            "user can not log in with empty username field.")

    public void testLoginEmptyUsername() {
        //fil out login parameters
        WebElement enterPassword = driver.findElement(By.id("password"));
        enterPassword.sendKeys(password);

        //Click on Login button
        WebElement submit = driver.findElement(By.xpath("//input[@type='submit']"));
        submit.click();

    }

    @Test(priority = 5, groups = ("Positive"), description = "This test verifies that " +
            "user can log in with by pressing Enter key instead clicking submit button.")

    public void testLoginEnterKey() {
        //fil out login parameters
        WebElement enterUsername = driver.findElement(By.id("email"));
        enterUsername.sendKeys(username);

        WebElement enterPassword = driver.findElement(By.id("password"));
        enterPassword.sendKeys(password);

        //Validate if Login button works with pressing Enter key on keyboard
        WebElement submit = driver.findElement(By.xpath("//input[@type='submit']"));
        submit.sendKeys(Keys.RETURN);

        //Validate that user is successfully logged in
        Assert.assertEquals(driver.getCurrentUrl(),homePageUrl);
        try {
            WebElement userName = driver.findElement(By.id("user-name"));
            assert (userName.getText().contains(user));
        } catch (RuntimeException e) {
            e.printStackTrace();
            throw new RuntimeException("Expected user is not logged in!");
        }

        WebElement role = driver.findElement(By.id("user-role"));
        Assert.assertEquals(role.getText(), "Group Admin");

        WebElement dropDownMenu = driver.findElement(By.xpath("//*[@id='actions-nav-item']"));
        dropDownMenu.click();

        WebElement signOut = driver.findElement(By.linkText("Sign out"));
        signOut.click();

    }

    @Test(priority = 3, groups = ("Negative"), description = "This test verifies that user" +
            "can not log in with invalid password.")

    public void testLoginInvalidPassword() {
        //fil out only e-mail parameters
        WebElement enterUsername = driver.findElement(By.id("email"));
        enterUsername.sendKeys(username);

        WebElement enterPassword = driver.findElement(By.id("password"));
        enterPassword.sendKeys("thisIsSomeWrongPassword");

        //Click on Login button
        WebElement submit = driver.findElement(By.xpath("//input[@type='submit']"));
        submit.click();

    }

    @Test(priority = 4, groups = ("Negative"), description = "This test verifies that user " +
            "can not log in with invalid username")

    public void loginTestInvalidUsername() {
        //fill out only e-mail parameters
        WebElement enterUsername = driver.findElement(By.id("email"));
        enterUsername.sendKeys("damir.covic@gmail.com");

        WebElement enterPassword = driver.findElement(By.id("password"));
        enterPassword.sendKeys(password);

        //Click on Login button
        WebElement submit = driver.findElement(By.xpath("//input[@type='submit']"));
        submit.click();

    }

    @Test(enabled = false, priority = 7, groups = ("Positive"), description = "This test verifies that user" +
            "can open 'Forgot your password' page")

    public void forgotPasswordPageTest() {
        //Click on Forgot your password? link
        WebElement forgotPassword = driver.findElement(By.xpath("//div[@ id = 'password-area']/a"));
        forgotPassword.click();

        //Validate that the Forgot your password site opens
        Assert.assertEquals(driver.getCurrentUrl(),forgotPassUrl);

        WebElement textOne = driver.findElement(By.xpath("//*[@id='login'']/p/text()"));
        Assert.assertEquals(textOne.getText(), "Change your password");

        WebElement textTwo = driver.findElement(By.xpath("//div[@id = 'login']/p/small"));
        Assert.assertEquals(textTwo.getText(), "Let's find your account");

    }

    @Test(priority = 8, groups = ("Positive"), description = "This test verifies that user can" +
            "click and open 'Terms Of Use' page")

    public void termsOfUsePageTest() {
        //Click on Terms of Use link
        WebElement termsOfUse = driver.findElement(By.linkText("Terms of Use"));
        termsOfUse.click();

        //Get current window handle
        String originalWindow = driver.getWindowHandle();

        //loop to find new window handle
        for (String windowHandle : driver.getWindowHandles()) {
            if(!originalWindow.contentEquals(windowHandle)) {
                driver.switchTo().window(windowHandle);
                break;
            }
        }

        //Validate that user is redirected to the Terms of use page
        Assert.assertEquals(driver.getCurrentUrl(),termsUrl);

        WebElement logo = driver.findElement(By.xpath("//div[@id=\"terms_of_service_container\"]/h3/img"));
        boolean logoPresent = logo.isDisplayed();
        Assert.assertTrue(logoPresent);

        WebElement termsTitleCurrent = driver.findElement(By.xpath("//div[@class = 'terms-header']"));
        Assert.assertEquals(termsTitleCurrent.getText(), "Terms and conditions of use");

    }

    @AfterTest(dependsOnGroups = {"Negative"}, description = "Verify that user is not logged in.")
    public void failedLogin() {
        //Validate that the user is not logged in
        Assert.assertEquals(driver.getCurrentUrl(),host);

        WebElement error = driver.findElement(By.xpath("//div[@class = 'error-area']"));
        Assert.assertEquals(error.getText(), "Invalid credentials!");

    }

    //Clean up - close the browser
    @AfterSuite (alwaysRun = true)
    public void quitDriver() {
        driver.quit();
    }
}