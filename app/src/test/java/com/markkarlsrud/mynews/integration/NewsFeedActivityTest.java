package com.markkarlsrud.mynews.integration;

import android.os.Bundle;

import com.markkarlsrud.mynews.activities.NewsFeedActivity;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;

import java.lang.reflect.Method;

/**
 * Created by mkarlsru on 7/1/17.
 */

@RunWith(RobolectricTestRunner.class)
@Config(manifest= Config.NONE)
public class NewsFeedActivityTest {
    private NewsFeedActivity activity;

    @Before
    public void init() {
        activity = new NewsFeedActivity();
    }

    @Test
    public void test() throws Exception {
        Method method = activity.getClass().getDeclaredMethod("getFeed");
        method.setAccessible(true);
        method.invoke(activity);
    }
}
