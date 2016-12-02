package com.example.tolmachenko.bookstest.model;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Olga Tolmachenko on 02.12.16.
 */

public class SaleInfo {
    @SerializedName("country")
    private String country;
    @SerializedName("saleability")
    private String saleability;
    @SerializedName("isEbook")
    private boolean isEbook;

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getSaleability() {
        return saleability;
    }

    public void setSaleability(String saleability) {
        this.saleability = saleability;
    }

    public boolean isEbook() {
        return isEbook;
    }

    public void setEbook(boolean ebook) {
        isEbook = ebook;
    }
}
