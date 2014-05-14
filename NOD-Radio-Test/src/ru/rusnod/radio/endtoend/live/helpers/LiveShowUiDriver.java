package ru.rusnod.radio.endtoend.live.helpers;

import android.app.Activity;
import android.app.Instrumentation;
import com.robotium.solo.Solo;

import static junit.framework.Assert.assertTrue;

public class LiveShowUiDriver extends Solo {
    public LiveShowUiDriver(Instrumentation instrumentation, Activity activity) {
        super(instrumentation, activity);
    }

    public void togglePlayback() {
        clickOnImageButton(0);
    }

    public void showsTranslationStatus(String statusText) {
        assertTrue(waitForText(statusText));
    }

}
