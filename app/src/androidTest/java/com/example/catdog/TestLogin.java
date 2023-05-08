package com.example.catdog;

import androidx.test.core.app.ActivityScenario;
import androidx.test.ext.junit.rules.ActivityScenarioRule;
import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.closeSoftKeyboard;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.RootMatchers.withDecorView;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;
import static org.junit.Assert.*;
import org.junit.Test;

public class TestLogin {
    public ActivityScenarioRule<LoginActivity> activityRule = new ActivityScenarioRule<>(LoginActivity.class);

    @Test
    public void testLoginSuccess() {
       activityRule.getScenario();
        // Вводим правильный email и пароль

            onView(withId(R.id.email_login)).perform(typeText("samoilovali100@gmail.com"));
            onView(withId(R.id.password_login)).perform(typeText("123456789"), closeSoftKeyboard());

            // Нажимаем на кнопку "Войти"
            onView(withId(R.id.btn_login)).perform(click());

            // Проверяем, что мы перешли на главный экран
            onView(withId(R.id.activity_main)).check(matches(isDisplayed()));
        }

    /*
    @Test
    public void testLoginFailure() {
        // Вводим неправильный email и пароль
        onView(withId(R.id.email_login)).perform(typeText("wrong@example.com"));
        onView(withId(R.id.password_login)).perform(typeText("wrong_password"), closeSoftKeyboard());

        // Нажимаем на кнопку "Войти"
        onView(withId(R.id.activity_main)).perform(click());

        // Проверяем, что появляется сообщение об ошибке
        testToastMessage();

    }
    public void testToastMessage() {
        // Вызываем метод, который вызывает Toast сообщение
        LoginActivity loginActivity = new LoginActivity();

        // Вызываем метод showMyToast через созданный экземпляр класса
        loginActivity.showMyToast();

        // Проверяем, что Toast сообщение отображается
        onView(withText(R.string.my_toast_text)).inRoot(withDecorView(not(is(loginActivity.getWindow().getDecorView())))).check(matches(isDisplayed()));
    }

    @Test
    public void testRegistrationSuccess() {
        // Нажимаем на текст "Зарегистрироваться"
        onView(withId(R.id.regist_txt)).perform(click());

        // Вводим данные для регистрации

        onView(withId(R.id.email_registr)).perform(typeText("user@example.com"));
        onView(withId(R.id.password_registr)).perform(typeText("123456"), closeSoftKeyboard());

        // Нажимаем на кнопку "Зарегистрироваться"
        onView(withId(R.id.btn_registr)).perform(click());

        // Проверяем, что мы перешли на главный экран
        onView(withId(R.id.activity_main)).check(matches(isDisplayed()));
    }

    @Test
    public void testRegistrationFailure() {
        // Нажимаем на кнопку "Зарегистрироваться"
        onView(withId(R.id.regist_txt)).perform(click());

        // Вводим неправильный email и пароль
        onView(withId(R.id.email_registr)).perform(typeText("wrong_email"));
        onView(withId(R.id.password_registr)).perform(typeText("short"), closeSoftKeyboard());

        // Нажимаем на кнопку "Зарегистрироваться"
        onView(withId(R.id.btn_registr)).perform(click());

        // Проверяем, что появляется сообщение об ошибке
        testToastMessageRegist();
    }
    public void testToastMessageRegist() {
        // Вызываем метод, который вызывает Toast сообщение
        RegistrerActivity registActivity = new RegistrerActivity();

        // Вызываем метод showMyToast через созданный экземпляр класса
        registActivity.showMyToast();

        // Проверяем, что Toast сообщение отображается
        onView(withText(R.string.my_toast_text)).inRoot(withDecorView(not(is(registActivity.getWindow().getDecorView())))).check(matches(isDisplayed()));
    }

     */
}
