# MWO-orders-dashboard
Projekt napisany w Javie wersji 17 oraz Angularze wersji 17. Na backendzie został użyty Spring Boot, natomiast na frontedzie używałem ng-bootstrap. Podpięta baza danych to baza w pamięci RAM H2. Zarządzaniem zależnościami w projekcie zajmował się Maven, którego treść zamieszczam poniżej:

`pom.xml:`
```xml
<dependencies>
  <dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-web</artifactId>
  </dependency>
  <dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-devtools</artifactId>
    <scope>runtime</scope>
    <optional>true</optional>
  </dependency>
  <dependency>
    <groupId>org.projectlombok</groupId>
    <artifactId>lombok</artifactId>
    <optional>true</optional>
  </dependency>
  <dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-test</artifactId>
    <scope>test</scope>
  </dependency>
  <dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-data-jpa</artifactId>
  </dependency>
  <dependency>
    <groupId>com.h2database</groupId>
    <artifactId>h2</artifactId>
    <scope>runtime</scope>
  </dependency>
  <dependency>
    <groupId>org.assertj</groupId>
    <artifactId>assertj-core</artifactId>
    <version>3.24.2</version>
    <scope>test</scope>
  </dependency>
  <dependency>
    <groupId>org.apache.logging.log4j</groupId>
    <artifactId>log4j-api</artifactId>
    <version>2.21.1</version>
  </dependency>
  <dependency>
    <groupId>org.apache.logging.log4j</groupId>
    <artifactId>log4j-core</artifactId>
    <version>2.21.1</version>
  </dependency>
  <dependency>
    <groupId>io.github.bonigarcia</groupId>
    <artifactId>webdrivermanager</artifactId>
    <version>5.6.2</version>
    <scope>test</scope>
  </dependency>
  <dependency>
    <groupId>org.seleniumhq.selenium</groupId>
    <artifactId>selenium-chrome-driver</artifactId>
    <version>4.8.3</version>
    <scope>test</scope>
  </dependency>
  <dependency>
    <groupId>org.seleniumhq.selenium</groupId>
    <artifactId>selenium-firefox-driver</artifactId>
    <version>4.8.3</version>
    <scope>test</scope>
  </dependency>
</dependencies>
```

## Konfiguracja selenium i przykładowy test

Do konfiguracji sterownika selenium użyłem WebDriverManager'a 
https://github.com/bonigarcia/webdrivermanager

`SeleniumTest.java:`
```java
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
}
```

## Frontend
![Zrzut ekranu z 2023-12-06 18-04-35](https://github.com/studia-pw/MWO-orders-dashboard/assets/62408118/3427ac57-8bc7-423c-8b59-70f3dcfef9cf)
![Zrzut ekranu z 2023-12-06 18-04-46](https://github.com/studia-pw/MWO-orders-dashboard/assets/62408118/593ac0d9-ca12-4894-8dcc-de5d669d985c)
![Zrzut ekranu z 2023-12-06 18-05-49](https://github.com/studia-pw/MWO-orders-dashboard/assets/62408118/80c38b5a-60cc-4ce2-9d1d-bb3dec447c93)
![Zrzut ekranu z 2023-12-06 18-05-56](https://github.com/studia-pw/MWO-orders-dashboard/assets/62408118/c3e140de-d471-4a05-9ac3-55e10b2ebbe3)
![Zrzut ekranu z 2023-12-06 18-06-04](https://github.com/studia-pw/MWO-orders-dashboard/assets/62408118/e09dd8bf-e3b7-44a1-97f7-bff782b0c105)

## Github actions
Pipeline buduje najpierw aplikację napisaną w Angularze oraz ją uruchamia, następnie odpala testy Selenium napisane w Javie. W razie wystąpienia błędów raportuje je na platformie Azure DevOps w postacji nowego zadania o typie 'Bug'. Pipeline wykonuje się dla każdego pull requesta / pusha do gałęzi main. Do raportowania błędów na platformie Azure użyłem workflow https://github.com/marketplace/actions/github-action-to-create-an-azure-devops-bug-workitem-when-a-workflow-fails.

`node.js.yml:`
```yml
name: Node.js CI

on:
  push:
    branches: [ "main" ]
  pull_request:
    branches: [ "main" ]

jobs:
  frontend:

    runs-on: ubuntu-latest

    strategy:
      matrix:
        node-version: [18.x]
        # See supported Node.js release schedule at https://nodejs.org/en/about/releases/
        

    steps:
      - uses: actions/checkout@v3

      - name: Use Node.js ${{ matrix.node-version }}
        uses: actions/setup-node@v3
        with:
          node-version: ${{ matrix.node-version }}
          cache: 'npm'
          cache-dependency-path: orders-dashboard-frontend/package-lock.json
      - run: npm ci
        working-directory: ./orders-dashboard-frontend
      - run: npm run build --if-present
        working-directory: ./orders-dashboard-frontend
      - run: npm start &
        working-directory: ./orders-dashboard-frontend
    
      - name: Set up Java version
        uses: actions/setup-java@v1
        with:
          java-version: '17'

      - name: Build and test with Maven
        run: mvn clean test

      - name: GitHub Action to create an Azure DevOps Bug Workitem when a workflow fails
        uses: stefanstranger/azuredevops-bug-action@1.1
        if: failure()
        with:
          OrganizationName: "01169599"
          PAT: "PAT"
          ProjectName: "MWO-project"
          AreaPath: "MWO-project"
          IterationPath: "MWO-project"
          GithubToken: "GithubToken"
          WorkflowFileName: "node.js.yml"
        env:
          PAT: ${{ secrets.PAT}}
          GithubToken: ${{ secrets.githubtoken}}
```

## Przykład zgłoszonego Bug'a

![Zrzut ekranu z 2023-12-06 18-18-16](https://github.com/studia-pw/MWO-orders-dashboard/assets/62408118/20105954-0d55-430f-a483-b148b43fc3fd)

## Prezentacja wideo


