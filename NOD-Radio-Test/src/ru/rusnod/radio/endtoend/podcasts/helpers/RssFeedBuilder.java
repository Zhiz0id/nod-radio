package ru.rusnod.radio.endtoend.podcasts.helpers;

public class RssFeedBuilder {
    private String items = "";

    public static RssFeedBuilder rssFeed() {
        return new RssFeedBuilder();
    }

    public static RssItemBuilder rssItem() {
        return new RssItemBuilder();
    }

    public String done() {
        return "<?xml version=\"1.0\" encoding=\"UTF-8\"?>" +
                "<rss xmlns:itunes=\"http://www.itunes.com/dtds/podcast-1.0.dtd\">" +
                "<channel>" +
                items +
                "</channel>" +
                "</rss>";
    }

    public RssFeedBuilder item(RssItemBuilder itemBuilder) {
        items += "<item>" + itemBuilder.done() + "</item>";
        return this;
    }

    public static class RssItemBuilder {
        private String content = "";
        public String done() {
            return content;
        }

        public RssItemBuilder title(String value) {
            return addTag("title", value);
        }


        public RssItemBuilder pubDate(String value) {
            return addTag("pubDate", value);
        }

        public RssItemBuilder description(String value) {
            return addTag("description", value);
        }

        public RssItemBuilder summary(String value) {
            return addTag("itunes:summary", value);
        }

        private RssItemBuilder addTag(String tag, String value) {
            content += String.format("<%1$s>%2$s</%1$s>", tag, value);
            return this;
        }

        public RssItemBuilder thumbnailUrl(String thumbnailUrl) {
            return description("&lt;p&gt;&lt;img src=\"" +
                    thumbnailUrl + "\" alt=\"\" /&gt;&lt;/p&gt;");
        }
    }
}
