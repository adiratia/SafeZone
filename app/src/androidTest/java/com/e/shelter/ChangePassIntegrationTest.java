package com.e.shelter;

import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.rule.ActivityTestRule;

import androidx.test.espresso.*;

import com.e.shelter.settings.ChangePassActivity;

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

public class ChangePassIntegrationTest {
    @Rule
    public ActivityTestRule<ChangePassActivity>changepass = new ActivityTestRule<>(ChangePassActivity.class);
    @Test
    public void ChangePassCheck(){
        Espresso.onView(withId(R.id.changepass_button)).check(matches(isClickable()));
        Espresso.onView(withId(R.id.oldpass)).check(matches(withHint("Old password")));
        Espresso.onView(withId(R.id.newpass)).check(matches(withHint("New password")));
        Espresso.onView(withId(R.id.newpass2)).check(matches(withHint("New password again")));
    }

}
