package ru.rusnod.radio.podcasts.ui;

import android.app.AlertDialog;
import android.content.Context;
import ru.rusnod.radio.R;
import ru.rusnod.radio.podcasts.core.ErrorListener;

class DialogErrorDisplayer implements ErrorListener {
    private Context context;

    public DialogErrorDisplayer(Context context) {
        this.context = context;
    }

    @Override
    public void onError(String errorMessage) {
        new AlertDialog.Builder(context)
                .setTitle(R.string.error_title)
                .setMessage(errorMessage)
                .show();
    }
}
