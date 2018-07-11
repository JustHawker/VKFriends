package com.example.vkfriends;

import android.os.SystemClock;
import android.support.design.widget.TextInputLayout;
import android.support.test.espresso.matcher.ViewMatchers;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import constants.UserErrorConstants;

import static android.support.test.espresso.Espresso.closeSoftKeyboard;
import static android.support.test.espresso.Espresso.onData;
import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.Espresso.pressBack;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.typeText;
import static android.support.test.espresso.matcher.ViewMatchers.isClickable;
import static android.support.test.espresso.matcher.ViewMatchers.withEffectiveVisibility;
import static android.support.test.espresso.matcher.ViewMatchers.Visibility;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.assertion.ViewAssertions.matches;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.CoreMatchers.anything;
/**
 * Instrumented test, which will execute on an Android device.
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
@RunWith(AndroidJUnit4.class)
public class AndroidUITest {

    private static Matcher withError(final String expected) {
        return new TypeSafeMatcher() {

            @Override
            protected boolean matchesSafely(Object item) {
                if (item instanceof TextInputLayout) {
                    return ((TextInputLayout)item).getError().toString().equals(expected);
                }
                return false;
            }

            @Override
            public void describeTo(Description description) {
                description.appendText("Not found error message [" + expected + "]");
            }
        };
    }
    @Rule
    public ActivityTestRule<MainActivity> mainActivityActivityTestRule =
            new ActivityTestRule<>(MainActivity.class);
    @Test
    public void noMutualFriendsTest() {
        onView(withId(R.id.InputScreenName_1)).perform(typeText("1"));
        closeSoftKeyboard();
        onView(withId(R.id.InputScreenName_2)).perform(typeText("1"));
        closeSoftKeyboard();
        onView(withId(R.id.button)).perform(click());
        SystemClock.sleep(5000);
        onView(withId(R.id.noMutualText)).check(matches(withEffectiveVisibility(ViewMatchers.Visibility.VISIBLE)));
    }

    @Test
    public void userNotExistTest()  {
        onView(withId(R.id.InputScreenName_1)).perform(typeText("htrhtr"));
        closeSoftKeyboard();
        onView(withId(R.id.InputScreenName_2)).perform(typeText("htrhtr"));
        closeSoftKeyboard();
        onView(withId(R.id.button)).perform(click());
        SystemClock.sleep(5000);
        onView(withId(R.id.userInputLayout_1)).check(matches(withError(UserErrorConstants.USER_NOT_EXST)));
    }
    @Test
    public void emptyStringTest()  {
        onView(withId(R.id.button)).perform(click());
        onView(withId(R.id.userInputLayout_1)).check(matches(withError("Пустая строка недопустима.")));
        onView(withId(R.id.userInputLayout_2)).check(matches(withError("Пустая строка недопустима.")));
    }
    @Test
    public void deletedUserTest() {
        onView(withId(R.id.InputScreenName_1)).perform(typeText("id138273487"));
        closeSoftKeyboard();
        onView(withId(R.id.InputScreenName_2)).perform(typeText("id138273487"));
        closeSoftKeyboard();
        onView(withId(R.id.button)).perform(click());
        SystemClock.sleep(5000);
        onView(withId(R.id.userInputLayout_1)).check(matches(withError(UserErrorConstants.USER_DELETED_OR_BLOCKED)));
        onView(withId(R.id.userInputLayout_2)).check(matches(withError(UserErrorConstants.USER_DELETED_OR_BLOCKED)));
    }
    @Test
    public void oneSpacedUserTest() {
        onView(withId(R.id.InputScreenName_1)).perform(typeText("dayanayad"));
        closeSoftKeyboard();
        onView(withId(R.id.InputScreenName_2)).perform(typeText("dayanayad "));
        closeSoftKeyboard();
        onView(withId(R.id.button)).perform(click());
        onView(withId(R.id.userInputLayout_2)).check(matches(withError("Пробелы недопустимы")));
    }

    @Test
    public void mutualFriendsExistTest() {
        onView(withId(R.id.InputScreenName_1)).perform(typeText("dayanayad"));
        closeSoftKeyboard();
        onView(withId(R.id.InputScreenName_2)).perform(typeText("dayanayad"));
        closeSoftKeyboard();
        onView(withId(R.id.button)).perform(click());
        SystemClock.sleep(5000);
        onView(withId(R.id.noMutualText)).check(matches(withEffectiveVisibility(Visibility.GONE)));
    }
    @Test
    public void searchButtonClickTest() {
        onView(withId(R.id.InputScreenName_1)).perform(typeText("dayanayad"));
        closeSoftKeyboard();
        onView(withId(R.id.InputScreenName_2)).perform(typeText("id265824820"));
        closeSoftKeyboard();
        onView(withId(R.id.button)).perform(click());
        SystemClock.sleep(5000);
        pressBack();
        onView(withId(R.id.button)).check(matches(isClickable()));
    }
    @Test
    public void searchButtonBlockTest() {
        onView(withId(R.id.InputScreenName_1)).perform(typeText("dayanayad"));
        closeSoftKeyboard();
        onView(withId(R.id.InputScreenName_2)).perform(typeText("id265824820"));
        closeSoftKeyboard();
        onView(withId(R.id.button)).perform(click());
        onView(withId(R.id.button)).perform(click());
        onView(withId(R.id.button)).perform(click());
        SystemClock.sleep(5000);
        pressBack();
        onView(withId(R.id.button)).check(matches(isClickable()));
    }
    @Test
    public void clickOnDeletedFriend() {
        onView(withId(R.id.InputScreenName_1)).perform(typeText("m1nta"));
        closeSoftKeyboard();
        onView(withId(R.id.InputScreenName_2)).perform(typeText("m1nta"));
        closeSoftKeyboard();
        onView(withId(R.id.button)).perform(click());
        SystemClock.sleep(5000);
        onData(anything()).inAdapterView(withId(R.id.list)).atPosition(0).perform(click());
        SystemClock.sleep(5000);
        onView(withId(R.id.FriendsCount)).check(matches(withText("")));
        onView(withId(R.id.FollowersCount)).check(matches(withText("")));
    }
    @Test
    public void clickOnActiveFriend() {
        onView(withId(R.id.InputScreenName_1)).perform(typeText("m1nta"));
        closeSoftKeyboard();
        onView(withId(R.id.InputScreenName_2)).perform(typeText("m1nta"));
        closeSoftKeyboard();
        onView(withId(R.id.button)).perform(click());
        SystemClock.sleep(5000);
        onData(anything()).inAdapterView(withId(R.id.list)).atPosition(1).perform(click());
        SystemClock.sleep(5000);
        onView(withId(R.id.UserName)).check(matches(withText("Глеб Петров")));
        onView(withId(R.id.FriendsCount)).check(matches(withText("195 друзей")));
        onView(withId(R.id.FollowersCount)).check(matches(withText("41 подписчиков")));
    }
}
