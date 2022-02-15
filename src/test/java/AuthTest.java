import com.github.javafaker.Faker;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.filter.log.LogDetail;
import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.Keys;

import java.util.Locale;

import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.open;
import static io.restassured.RestAssured.given;

public class AuthTest {
    private static RequestSpecification requestSpec = new RequestSpecBuilder()
            .setBaseUri("http://localhost")
            .setPort(9999)
            .setAccept(ContentType.JSON)
            .setContentType(ContentType.JSON)
            .log(LogDetail.ALL)
            .build();

    @BeforeAll
    static void setUpAll() {
        given()
                .spec(requestSpec) // указываем, какую спецификацию используем
                .body(new RegistrationDto("vasya", "password", "active")) // передаём в теле объект, который будет преобразован в JSON
                .when() // "когда"
                .post("/api/system/users") // на какой путь, относительно BaseUri отправляем запрос
                .then() // "тогда ожидаем"
                .statusCode(200); // код 200 OK
    }

    @Test
    public void createBlockedUser() {
        RegistrationDto user = new RegistrationDto("dima", "password", "blocked");
        given().spec(requestSpec)
                .body(user)
                .when()
                .post("/api/system/users")
                .then()
                .statusCode(200);

        open("http://localhost:9999");
        $(".input__control[name=login]").doubleClick().sendKeys(Keys.BACK_SPACE);
        $(".input__control[name=login]").setValue(user.getLogin());
        $(".input__control[name=password]").doubleClick().sendKeys(Keys.BACK_SPACE);
        $(".input__control[name=password]").setValue(user.getPassword());
        $("button").click();
        $(".notification__title").shouldHave(text("Ошибка"));
        $(".notification__content")
                .shouldHave(text("Ошибка! Пользователь заблокирован"));
    }

    @Test
    public void userExistsTest() {
        open("http://localhost:9999");
        RegistrationDto user = new RegistrationDto("vasya", "password", "active");
        $(".input__control[name=login]").doubleClick().sendKeys(Keys.BACK_SPACE);
        $(".input__control[name=login]").setValue(user.getLogin());
        $(".input__control[name=password]").doubleClick().sendKeys(Keys.BACK_SPACE);
        $(".input__control[name=password]").setValue(user.getPassword());
        $("button").click();
        $("h2").shouldHave(text("Личный кабинет"));
    }

    @Test
    public void invalidLoginTest() {
        open("http://localhost:9999");
        RegistrationDto user = new RegistrationDto("vas", "password", "active");
        $(".input__control[name=login]").doubleClick().sendKeys(Keys.BACK_SPACE);
        $(".input__control[name=login]").setValue(user.getLogin());
        $(".input__control[name=password]").doubleClick().sendKeys(Keys.BACK_SPACE);
        $(".input__control[name=password]").setValue(user.getPassword());
        $("button").click();
        $(".notification__title").shouldHave(text("Ошибка"));
        $(".notification__content")
                .shouldHave(text("Ошибка! Неверно указан логин или пароль"));
    }

    @Test
    public void invalidPassTest() {
        open("http://localhost:9999");
        RegistrationDto user = new RegistrationDto("vasya", "pass", "active");
        $(".input__control[name=login]").doubleClick().sendKeys(Keys.BACK_SPACE);
        $(".input__control[name=login]").setValue(user.getLogin());
        $(".input__control[name=password]").doubleClick().sendKeys(Keys.BACK_SPACE);
        $(".input__control[name=password]").setValue(user.getPassword());
        $("button").click();
        $(".notification__title").shouldHave(text("Ошибка"));
        $(".notification__content")
                .shouldHave(text("Ошибка! Неверно указан логин или пароль"));
    }

}