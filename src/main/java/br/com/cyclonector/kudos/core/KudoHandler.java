package br.com.cyclonector.kudos.core;

import br.com.cyclonector.kudos.config.Properties;
import br.com.cyclonector.kudos.config.WebDriverConfig;
import io.quarkus.scheduler.Scheduled;
import lombok.extern.log4j.Log4j2;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import javax.inject.Inject;
import java.util.List;

@Log4j2
public class KudoHandler {
    public static final String XPATH_KUDOS_BUTTON = "//*[@title='Give Kudos']";
    public static final String ATHLETE_PROFILE_ID = "athlete-profile";
    @Inject
    protected WebDriverConfig webDriverConf;

    @Inject
    protected Properties properties;

    @Scheduled(every = "1m")
    public void giveLikes() {
        WebDriver webDriver = webDriverConf.getWebDriver();
        WebDriverWait webDriverWait = new WebDriverWait(webDriver, 10);

        loginOnStrava(webDriver, webDriverWait);

        List<WebElement> htmlElements = getElements(webDriver, webDriverWait);

        if (htmlElements.size() != 0) {
            log.info("Giving " + htmlElements.size() + " kudos!");
            giveKudos(webDriver, htmlElements);
        } else
            log.info("No kudos to give");

        log.info("Closing web webDriver");
        webDriver.close();
    }

    private List<WebElement> getElements(WebDriver driver, WebDriverWait webDriverWait) {
        log.info("Getting HTML elements");
        webDriverWait.until(ExpectedConditions.elementToBeClickable(By.id(ATHLETE_PROFILE_ID)));
        return driver.findElements(By.xpath(XPATH_KUDOS_BUTTON));
    }

    private void giveKudos(WebDriver driver, List<WebElement> htmlElements) {
        htmlElements.forEach(webElement -> ((JavascriptExecutor) driver)
                .executeScript("arguments[0].click();", webElement));
    }

    public void loginOnStrava(WebDriver driver, WebDriverWait wait) {
        log.info("Login in on Strava");
        driver.get("https://www.strava.com/dashboard/following/1000");

        wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("email")));
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("password")));
        wait.until(ExpectedConditions.elementToBeClickable(By.id("login-button")));

        driver.findElement(By.id("email")).sendKeys(properties.getLogin());
        driver.findElement(By.id("password")).sendKeys(properties.getPassword());
        driver.findElement(By.id("login-button")).click();
    }
}
