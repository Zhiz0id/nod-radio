package ru.rusnod.radio.endtoend.podcasts.helpers;

import ru.rusnod.radio.helpers.ResponsiveHttpServer;
import ru.rusnod.radio.helpers.ResponsiveHttpServer;

import java.io.IOException;

public class TestRssServer extends ResponsiveHttpServer {

    public TestRssServer() throws IOException {
        super();
    }

    public void respondSuccessWith(String body) {
        respondSuccessWith(body, MIME_XML);
    }

    public void respondNotFoundError() {
        respondWith(new Response(HTTP_NOTFOUND, MIME_HTML, ""));
    }

    public void hasReceivedRequestForRss() throws InterruptedException {
        hasReceivedRequest("/rss");
    }

    public void hasReceivedRequestForUrl(String url) throws InterruptedException {
        hasReceivedRequest(url);
    }

}
