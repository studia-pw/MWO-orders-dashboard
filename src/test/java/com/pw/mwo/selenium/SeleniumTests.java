package com.pw.mwo.selenium;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.junit.jupiter.api.*;
import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@ActiveProfiles("test")
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class SeleniumTests {
    private static final String PRODUCT_FRONTEND = "http://localhost:4200/products";
    static WebDriver driver;

    @BeforeAll
    static void setUp() {
        WebDriverManager.chromiumdriver().setup();
    }

    @BeforeEach
    void beforeEach() {
        final var options = new ChromeOptions();
        options.addArguments("--headless", "--disable-extensions", "--disable-dev-shm-usage");
        driver = new ChromeDriver(options);
        driver.get(PRODUCT_FRONTEND);
    }

    @AfterEach
    void tearDown() {
        driver.quit();
    }

    @Test
    @Order(1)
    void givenProduct_whenAddNewProduct_thenListContainsProduct() throws InterruptedException {
        final var name = "Pencil";
        final var price = "1.99";
        final var quantity = "10";
        driver.findElement(By.xpath("//button[contains(text(),'Add new product')]")).click();
        driver.findElement(By.id("name")).sendKeys(name);
        driver.findElement(By.id("surname")).sendKeys(price);
        driver.findElement(By.id("email")).sendKeys(quantity);
        driver.findElement(By.xpath("//button[contains(text(), 'Save')]")).click();
        Thread.sleep(2000);
        final var element = driver.findElement(By.xpath("//td[contains(text(),'" + name + "')]"));
        assertThat(element.isDisplayed()).isTrue();
    }

    @Test
    @Order(2)
    void givenProduct_whenEditProduct_thenListContainsUpdatedProduct() throws InterruptedException {
        final var name = "Pen";
        driver.findElement(By.xpath("//button[contains(text(),'Edit')]")).click();
        Thread.sleep(1000);
        driver.findElement(By.id("name")).clear();
        driver.findElement(By.id("name")).sendKeys(name);
        driver.findElement(By.xpath("//button[contains(text(), 'Save')]")).click();
        Thread.sleep(2000);
        final var element = driver.findElement(By.xpath("//td[contains(text(),'" + name + "')]"));
        assertThat(element.isDisplayed()).isTrue();
    }

    @Test
    @Order(3)
    void givenProduct_whenDeleteProduct_thenListDoesNotContainProduct() throws InterruptedException {
        driver.findElement(By.xpath("//button[contains(text(),'Delete')]")).click();
        Thread.sleep(1000);
        driver.findElement(By.id("deleteConfirm")).click();
        Thread.sleep(2000);
        assertThat(driver.findElement(By.xpath("//td[contains(text(),'Pen')]")).isDisplayed()).isFalse();
        assertThatThrownBy(() -> driver.findElement(By.xpath("//td[contains(text(),'Pen')]")))
                .isInstanceOf(NoSuchElementException.class);
    }
}
