package com.example.tolmachenko.bookstest.model;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Olga Tolmachenko on 02.12.16.
 */

public class SearchInfo {
    @SerializedName("textSnippet")
    private String textSnippet;

    public String getTextSnippet() {
        return textSnippet;
    }

    public void setTextSnippet(String textSnippet) {
        this.textSnippet = textSnippet;
    }
}
