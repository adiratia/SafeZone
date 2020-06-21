package com.e.shelter;

import androidx.test.espresso.Espresso;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.rule.ActivityTestRule;

import com.e.shelter.settings.EditUserActivity;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isClickable;
import static androidx.test.espresso.matcher.ViewMatchers.withHint;
import static androidx.test.espresso.matcher.ViewMatchers.withId;

@RunWith(AndroidJUnit4.class)

public class EditUserIntegrationTest {
    @Rule
    public ActivityTestRule<EditUserActivity> signup = new ActivityTestRule<>(EditUserActivity.class);

    @Test
    public void EditUserViewCheck(){
        Espresso.onView(withId(R.id.updateButton)).check(matches(isClickable()));
    }

}
