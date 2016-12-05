package com.example.tolmachenko.bookstest.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

import static android.R.attr.id;

/**
 * Created by Olga Tolmachenko on 03.12.16.
 */

public class Book extends RealmObject {

    @PrimaryKey
    private String title;
    private String infoLink;
    private String thumbnail;

    public Book() {}

    public Book(String title, String infoLink, String thumbnail) {
        this.title = title;
        this.infoLink = infoLink;
        this.thumbnail = thumbnail;
    }

    public Book(Parcel in) {
        this.title = in.readString();
        this.infoLink = in.readString();
        this.thumbnail = in.readString();
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
