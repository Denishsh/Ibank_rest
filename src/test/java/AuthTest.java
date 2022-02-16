import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.Keys;

import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.open;

public class AuthTest {

    @BeforeAll
    static void setUpAll() {
        DataGenerator.sendRequest(DataGenerator.Registration.getRegisteredUser());
    }

    @Test
    public void createBlockedUser() {
        DataGenerator.sendRequest(DataGenerator.Registration.getUser("blocked"));
        DataGenerator.RegistrationDto user = DataGenerator.Registration.getRegisteredUser();

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
        DataGenerator.sendRequest(DataGenerator.Registration.getUser("active"));
        DataGenerator.RegistrationDto user = DataGenerator.Registration.getRegisteredUser();
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
        DataGenerator.sendRequest(DataGenerator.Registration.getUser("active"));
        DataGenerator.RegistrationDto user = DataGenerator.Registration.getRegisteredUser();
        $(".input__control[name=login]").doubleClick().sendKeys(Keys.BACK_SPACE);
        $(".input__control[name=login]").setValue(user.getLogin() + "/");
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
        DataGenerator.sendRequest(DataGenerator.Registration.getUser("active"));
        DataGenerator.RegistrationDto user = DataGenerator.Registration.getRegisteredUser();
        $(".input__control[name=login]").doubleClick().sendKeys(Keys.BACK_SPACE);
        $(".input__control[name=login]").setValue(user.getLogin());
        $(".input__control[name=password]").doubleClick().sendKeys(Keys.BACK_SPACE);
        $(".input__control[name=password]").setValue(user.getPassword() + "/");
        $("button").click();
        $(".notification__title").shouldHave(text("Ошибка"));
        $(".notification__content")
                .shouldHave(text("Ошибка! Неверно указан логин или пароль"));
    }

}