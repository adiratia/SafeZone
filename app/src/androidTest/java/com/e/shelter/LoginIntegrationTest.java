package com.e.shelter;

import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.rule.ActivityTestRule;

import androidx.test.espresso.*;

import com.e.shelter.login.LoginActivity;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isClickable;
import static androidx.test.espresso.matcher.ViewMatchers.withHint;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;

@RunWith(AndroidJUnit4.class)

public class LoginIntegrationTest {
    @Rule
    public ActivityTestRule<LoginActivity>login = new ActivityTestRule<>(LoginActivity.class);

    @Test
    public void LoginViewCheck(){
        Espresso.onView(withId(R.id.LoginButton)).check(matches(isClickable()));
        Espresso.onView(withId(R.id.signUpButton)).check(matches(isClickable()));
        Espresso.onView(withId(R.id.emailInput)).check(matches(withHint("Email")));
        Espresso.onView(withId(R.id.passInput)).check(matches(withHint("Password")));

    }


}
