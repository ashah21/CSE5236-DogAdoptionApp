package com.example.dog_test;

import static androidx.test.platform.app.InstrumentationRegistry.getInstrumentation;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import org.junit.Test;
import org.junit.runner.RunWith;
import static org.junit.Assert.*;
import androidx.test.rule.ActivityTestRule;
import com.example.dog_test.ui.activity.MainActivity;
import com.example.dog_test.ui.fragment.HomeFragment;
import com.google.android.material.navigation.NavigationView;

@RunWith(AndroidJUnit4.class)
public class MainActivityUITest extends ActivityTestRule<MainActivity> {

    private final MainActivity mainActivity;

    private final HomeFragment homeFragment;

    private final NavigationView navigationView;

    private final DrawerLayout slideMenu;

    public MainActivityUITest() {
        super(MainActivity.class);

        launchActivity(getActivityIntent());
        mainActivity = getActivity();
        homeFragment = mainActivity.getHomeFragmentForTest();
        slideMenu = mainActivity.findViewById(R.id.slide_menu);
        navigationView = mainActivity.findViewById(R.id.navigationView);

        getInstrumentation().waitForIdleSync();
    }

    @Override
    protected void beforeActivityLaunched() {
        super.beforeActivityLaunched();
    }


    @Test
    public void testPreconditions() {
        assertNotNull(mainActivity);
        assertNotNull(homeFragment);
        assertNotNull(navigationView);
        assertNotNull(slideMenu);
    }

    protected void afterActivityFinished() {
        super.afterActivityFinished();
        if (!getActivity().isFinishing()) {
            mainActivity.finish();
        }
    }
}
