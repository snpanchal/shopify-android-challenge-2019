package com.example.shopifyandroid.models;

public class Collection {

    private String title;
    private String imageLink;
    private String bodyHtml;
    private String id;

    public Collection(String title, String imageLink, String bodyHtml, String id) {
        this.title = title;
        this.imageLink = imageLink;
        this.bodyHtml = bodyHtml;
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getImageLink() {
        return imageLink;
    }

    public void setImageLink(String imageLink) {
        this.imageLink = imageLink;
    }

    public String getBodyHtml() {
        return bodyHtml;
    }

    public void setBodyHtml(String bodyHtml) {
        this.bodyHtml = bodyHtml;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
