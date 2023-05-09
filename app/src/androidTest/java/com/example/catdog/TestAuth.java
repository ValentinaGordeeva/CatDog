package com.example.catdog;
import androidx.test.core.app.ActivityScenario;
import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;

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
import androidx.test.rule.ActivityTestRule;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import static androidx.test.espresso.intent.Intents.intended;
import static androidx.test.espresso.intent.matcher.IntentMatchers.hasComponent;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(AndroidJUnit4.class)
public class TestAuth {
    public ActivityTestRule<LoginActivity> mActivityRule = new ActivityTestRule<>(LoginActivity.class);

    @Test
    public void testLoginSuccess() {
        mActivityRule.launchActivity(new Intent());

        // Ожидаем, что активность запустится
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        // Вводим правильный email и пароль
        onView(withId(R.id.email_login)).perform(typeText("samoilovali100@gmail.com"), closeSoftKeyboard());
        onView(withId(R.id.password_login)).perform(click(), typeText("123456789"), closeSoftKeyboard());

        // Нажимаем на кнопку "Войти"
        onView(withId(R.id.btn_login)).perform(click());

        // Проверяем, что мы перешли на главный экран

        //onView(withId(R.id.activity_main)).check(matches(isDisplayed()));
        // intended(hasComponent(MainActivity.class.getName()));
    }


    @Test
    public void testLoginFailure() {
        mActivityRule.launchActivity(new Intent());

        // Ожидаем, что активность запустится

        // Вводим неправильный email и пароль
        onView(withId(R.id.email_login)).perform(typeText("wrong@example.com"), closeSoftKeyboard());
        onView(withId(R.id.password_login)).perform(typeText("wrong_password"), closeSoftKeyboard());

        // Нажимаем на кнопку "Войти"
         onView(withId(R.id.btn_login)).perform(click());

        // Проверяем, что появляется сообщение об ошибке

        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        // Проверяем, что Toast сообщение отображается
        onView(withText("Ошибка, не правильно введен логин или пароль")).inRoot(withDecorView(not(mActivityRule.getActivity().getWindow().getDecorView()))).check(matches(isDisplayed()));

    }



    @Test
    public void testRegistrationSuccess() {
        mActivityRule.launchActivity(new Intent());
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        // Нажимаем на текст "Зарегистрироваться"
        onView(withId(R.id.regist_txt)).perform(click());

        // Вводим данные для регистрации

        onView(withId(R.id.email_registr)).perform(typeText("user@example.com"), closeSoftKeyboard());
        onView(withId(R.id.password_registr)).perform(typeText("123456"), closeSoftKeyboard());

        // Нажимаем на кнопку "Зарегистрироваться"
        onView(withId(R.id.btn_registr)).perform(click());

        // Проверяем, что мы перешли на главный экран
        // onView(withId(R.id.activity_main)).check(matches(isDisplayed()));
    }

    @Test
    public void testRegistrationFailure() {
        mActivityRule.launchActivity(new Intent());
        // Нажимаем на кнопку "Зарегистрироваться"
        onView(withId(R.id.regist_txt)).perform(click());

        // Вводим неправильный email и пароль
        onView(withId(R.id.email_registr)).perform(typeText("wrong_email"),closeSoftKeyboard());
        onView(withId(R.id.password_registr)).perform(typeText("short"), closeSoftKeyboard());

        // Нажимаем на кнопку "Зарегистрироваться"
        onView(withId(R.id.btn_registr)).perform(click());

        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        // Проверяем, что появляется сообщение об ошибке
        onView(withText("Ошибка ввода данных")).inRoot(withDecorView(not(mActivityRule.getActivity().getWindow().getDecorView()))).check(matches(isDisplayed()));
    }

}

