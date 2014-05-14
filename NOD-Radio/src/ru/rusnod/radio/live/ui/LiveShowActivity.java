package ru.rusnod.radio.live.ui;

import android.os.Bundle;
import ru.rusnod.radio.R;
import ru.rusnod.radio.common.ui.CustomTitleActivity;

public class LiveShowActivity extends CustomTitleActivity {

    @Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.live_show_screen);
    }
}
