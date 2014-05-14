package ru.rusnod.radio.home_screen;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import ru.rusnod.radio.R;
import ru.rusnod.radio.common.ui.TrackedActivity;
import ru.rusnod.radio.live.ui.LiveShowActivity;
import ru.rusnod.radio.podcasts.main.PodcastsApp;

public class HomeScreenActivity extends TrackedActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home_screen);
        initDashboardItems();
    }

    private void initDashboardItems() {
/*        onButtonClick(R.id.home_btn_main_show, new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PodcastsApp.openScreen(HomeScreenActivity.this,
                        getString(R.string.main_show_title), "main-show");
            }
        });
        onButtonClick(R.id.home_btn_after_show, new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PodcastsApp.openScreen(HomeScreenActivity.this,
                        getString(R.string.after_show_title), "after-show");
            }
        });*/
        onButtonClick(R.id.home_btn_live_show, new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openScreen(LiveShowActivity.class);
            }
        });
        onButtonClick(R.id.home_btn_about, new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openScreen(AboutAppActivity.class);
            }
        });
    }

    private void openScreen(Class<? extends Activity> cls) {
        startActivity(new Intent(this, cls));
    }

    private void onButtonClick(int id, View.OnClickListener listener) {
        findViewById(id).setOnClickListener(listener);
    }
}
