package me.overlight.powertools.WebHooks;/*
 * Decompiled with CFR 0.152.
 */

import org.bukkit.Color;

import java.util.ArrayList;
import java.util.List;

public class EmbedObject {
    private String title;
    private String description;
    private String url;
    private Color color;
    private Footer footer;
    private Thumbnail thumbnail;
    private Image image;
    private Author author;
    private final List<Field> fields = new ArrayList<>();

    public String getTitle() {
        return this.title;
    }

    public String getDescription() {
        return this.description;
    }

    public String getUrl() {
        return this.url;
    }

    public Color getColor() {
        return this.color;
    }

    public Footer getFooter() {
        return this.footer;
    }

    public Thumbnail getThumbnail() {
        return this.thumbnail;
    }

    public Image getImage() {
        return this.image;
    }

    public Author getAuthor() {
        return this.author;
    }

    public List<Field> getFields() {
        return this.fields;
    }

    public EmbedObject setTitle(String title) {
        this.title = title;
        return this;
    }

    public EmbedObject setDescription(String description) {
        this.description = description;
        return this;
    }

    public EmbedObject setUrl(String url) {
        this.url = url;
        return this;
    }

    public EmbedObject setColor(Color color) {
        this.color = color;
        return this;
    }

    public EmbedObject setFooter(String text, String icon) {
        this.footer = new Footer(text, icon);
        return this;
    }

    public EmbedObject setThumbnail(String url) {
        this.thumbnail = new Thumbnail(url);
        return this;
    }

    public EmbedObject setImage(String url) {
        this.image = new Image(url);
        return this;
    }

    public EmbedObject setAuthor(String name, String url, String icon) {
        this.author = new Author(name, url, icon);
        return this;
    }

    public EmbedObject addField(String name, String value, boolean inline) {
        this.fields.add(new Field(name, value, inline));
        return this;
    }

    private class Field {
        private final String name;
        private final String value;
        private final boolean inline;

        private Field(String name, String value, boolean inline) {
            this.name = name;
            this.value = value;
            this.inline = inline;
        }

        private String getName() {
            return this.name;
        }

        private String getValue() {
            return this.value;
        }

        private boolean isInline() {
            return this.inline;
        }

        public /* synthetic */ String access$800(Field x0) {
            return x0.getName();
        }

        public /* synthetic */ String access$900(Field x0) {
            return x0.getValue();
        }

        public /* synthetic */ boolean access$1000(Field x0) {
            return x0.isInline();
        }
    }

    private class Author {
        private final String name;
        private final String url;
        private final String iconUrl;

        private Author(String name, String url, String iconUrl) {
            this.name = name;
            this.url = url;
            this.iconUrl = iconUrl;
        }

        private String getName() {
            return this.name;
        }

        private String getUrl() {
            return this.url;
        }

        private String getIconUrl() {
            return this.iconUrl;
        }

        public /* synthetic */ String access$500(Author x0) {
            return x0.getName();
        }

        public /* synthetic */ String access$600(Author x0) {
            return x0.getUrl();
        }

        public /* synthetic */ String access$700(Author x0) {
            return x0.getIconUrl();
        }
    }

    private class Image {
        private final String url;

        private Image(String url) {
            this.url = url;
        }

        private String getUrl() {
            return this.url;
        }

        public /* synthetic */ String access$300(Image x0) {
            return x0.getUrl();
        }
    }

    private class Thumbnail {
        private final String url;

        private Thumbnail(String url) {
            this.url = url;
        }

        private String getUrl() {
            return this.url;
        }

        public /* synthetic */ String access$400(Thumbnail x0) {
            return x0.getUrl();
        }
    }

    private class Footer {
        private final String text;
        private final String iconUrl;

        private Footer(String text, String iconUrl) {
            this.text = text;
            this.iconUrl = iconUrl;
        }

        private String getText() {
            return this.text;
        }

        private String getIconUrl() {
            return this.iconUrl;
        }

        public /* synthetic */ String access$100(Footer x0) {
            return x0.getText();
        }

        public /* synthetic */ String access$200(Footer x0) {
            return x0.getIconUrl();
        }
    }
}
