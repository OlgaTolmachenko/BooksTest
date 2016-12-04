package com.example.tolmachenko.bookstest.model;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by Olga Tolmachenko on 03.12.16.
 */

public class Book extends RealmObject {

    private String title;
    private String infoLink;
    private String thumbnail;

    public Book() {}

    public Book(String title, String infoLink, String thumbnail) {
        this.title = title;
        this.infoLink = infoLink;
        this.thumbnail = thumbnail;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getInfoLink() {
        return infoLink;
    }

    public void setInfoLink(String infoLink) {
        this.infoLink = infoLink;
    }

    public String getThumbnail() {
        return thumbnail;
    }

    public void setThumbnail(String thumbnail) {
        this.thumbnail = thumbnail;
    }
}
