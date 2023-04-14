package com.example.dog_test;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.clearText;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.intent.Intents.intended;
import static androidx.test.espresso.intent.matcher.IntentMatchers.hasComponent;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import android.content.Intent;
import android.view.View;
import android.widget.TextView;
import androidx.test.espresso.UiController;
import androidx.test.espresso.ViewAction;
import androidx.test.espresso.intent.Intents;
import androidx.test.espresso.matcher.ViewMatchers;
import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.rule.ActivityTestRule;
import com.example.dog_test.ui.activity.LoginActivity;
import com.example.dog_test.ui.activity.MainActivity;
import org.hamcrest.Matcher;
import org.hamcrest.Matchers;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(AndroidJUnit4.class)
public class LoginActivityUITest {

    public static ViewAction setTextInTextView(final String value) {

        return new ViewAction() {
            @Override
            public Matcher<View> getConstraints() {
                return Matchers.allOf(ViewMatchers.isDisplayed(), ViewMatchers.isAssignableFrom(TextView.class));
            }

            @Override
            public String getDescription() {
                return "replace text";
            }

            @Override
            public void perform(UiController uiController, View view) {
                ((TextView) view).setText(value);
            }
        };

    }

    @Rule
    public ActivityScenarioRule<LoginActivity> activityActivityScenarioRule =
            new ActivityScenarioRule<>(LoginActivity.class);

    @Rule
    public ActivityTestRule<LoginActivity> activityTestRule =
            new ActivityTestRule<>(LoginActivity.class);

    @Before
    public void setUp() {
        onView(withId(R.id.email)).perform(clearText());
        onView(withId(R.id.password)).perform(clearText());
    }

    @Test
    public void invalidCredentialLoginTest() {
        Intents.init();

        onView(withId(R.id.email)).perform(setTextInTextView("suz@gmail.com"));
        onView(withId(R.id.password)).perform(setTextInTextView("password"));
        onView(withId(R.id.btn_login)).perform(click());

        activityTestRule.launchActivity(new Intent());
        intended(hasComponent(LoginActivity.class.getName()));
        Intents.release();
    }

    @Test
    public void validCredentialLoginTest() {
        Intents.init();

        onView(withId(R.id.email)).perform(setTextInTextView("suzy@gmail.com"));
        onView(withId(R.id.password)).perform(setTextInTextView("password"));
        onView(withId(R.id.btn_login)).perform(click());

        activityTestRule.launchActivity(new Intent());
        intended(hasComponent(MainActivity.class.getName()));
        Intents.release();
    }





}
