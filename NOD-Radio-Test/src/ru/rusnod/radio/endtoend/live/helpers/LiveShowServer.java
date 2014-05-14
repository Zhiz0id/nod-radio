package ru.rusnod.radio.endtoend.live.helpers;

import android.content.Context;
import ru.rusnod.radio.helpers.LiveStreamServer;
import ru.rusnod.radio.helpers.LiveStreamServer;

import java.io.IOException;

public class LiveShowServer extends LiveStreamServer {
    public static final String SHOW_URL = DIRECT_URL;
    private boolean translationActivated = true;

    public LiveShowServer(Context context) throws IOException {
        super(context);
    }

    @Override
    protected Response serveUri(String uri) {
        if (translationActivated) {
            return super.serveUri(uri);
        }
        return notFound();
    }

    private Response notFound() {
        return new Response(HTTP_NOTFOUND, MIME_PLAINTEXT, "");
    }

    public void activateTranslation() {
        translationActivated = true;
    }

    public void suppressTranslation() {
        translationActivated = false;
    }
}
