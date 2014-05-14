package ru.rusnod.radio.podcasts.download;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import ru.rusnod.radio.R;
import ru.rusnod.radio.util.AppInfo;
import ru.rusnod.radio.util.FeedbackEmail;

public class FakeDownloaderActivity extends Activity {
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.download_unsupported_screen);
    }
    
    public void sendFeedback(View v) {
        new FeedbackEmail(this, new AppInfo(this))
                .setText(getString(R.string.implement_download_request))
                .openInEditor();
    }
}