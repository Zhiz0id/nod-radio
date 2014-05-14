package ru.rusnod.radio.util;

import android.content.Context;
import android.content.Intent;

public class FeedbackEmail {
    private static final String ADDRESS = "nod@you-ra.info";
    private AppInfo appInfo;
    private Context context;
    private String messageText = "";

    public FeedbackEmail(Context context, AppInfo appInfo) {
        this.context = context;
        this.appInfo = appInfo;
    }

    public void openInEditor() {
        Intent intent = createEmailIntent(ADDRESS, composeSubject());
        context.startActivity(Intent.createChooser(intent, null));
    }

    private Intent createEmailIntent(String address, String subject) {
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("message/rfc822");
        intent.putExtra(Intent.EXTRA_EMAIL, new String[] {address} );
        intent.putExtra(Intent.EXTRA_SUBJECT, subject);
        intent.putExtra(Intent.EXTRA_TEXT, composeMessageText());
        return intent;
    }

    private String composeMessageText() {
        return messageText + messageFooter();
    }

    private String messageFooter() {
        return String.format("\n\n---------------------\nAndroid version: %s", appInfo.getAndroidVersion());
    }

    private String composeSubject() {
        return String.format("%s %s: Feedback", appInfo.getLabel(), appInfo.getVersion());
    }

    public FeedbackEmail setText(String text) {
        messageText = text;
        return this;
    }
}
