package br.com.cyclonector.kudos.config;

import io.github.bonigarcia.wdm.WebDriverManager;
import lombok.extern.log4j.Log4j2;
import org.openqa.selenium.PageLoadStrategy;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

@Log4j2
@ApplicationScoped
public class WebDriverConfig {
    @Inject
    protected Properties properties;
    private WebDriver driver;

    public WebDriver getWebDriver() {
        log.info("Configuring chrome driver");
        WebDriverManager.chromiumdriver().setup();
        ChromeOptions options = new ChromeOptions();
        options.addArguments("enable-automation");
        options.addArguments("--window-size=1920,1080");
        options.addArguments("--no-sandbox");
        options.addArguments("--disable-extensions");
        options.addArguments("--dns-prefetch-disable");
        options.addArguments("--disable-gpu");
        options.setPageLoadStrategy(PageLoadStrategy.NORMAL);

        if (properties.isHeadlessBrowser())
            options.addArguments("--headless");
        driver = new ChromeDriver(options);
        return driver;
    }
}