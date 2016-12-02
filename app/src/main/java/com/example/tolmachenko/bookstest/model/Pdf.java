package com.example.tolmachenko.bookstest.model;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Olga Tolmachenko on 02.12.16.
 */

public class Pdf {
    @SerializedName("isAvailable")
    private boolean isAvailable;

    public boolean isAvailable() {
        return isAvailable;
    }

    public void setAvailable(boolean available) {
        isAvailable = available;
    }
}
