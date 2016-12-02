package com.example.tolmachenko.bookstest.model;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Olga Tolmachenko on 02.12.16.
 */

public class AccessInfo {

    @SerializedName("country")
    private String country;
    @SerializedName("viewability")
    private String viewability;
    @SerializedName("embeddable")
    private boolean embeddable;
    @SerializedName("publicDomain")
    private boolean publicDomain;
    @SerializedName("textToSpeechPermission")
    private String textToSpeechPermission;
    @SerializedName("epub")
    private Epub epub;
    @SerializedName("pdf")
    private Pdf pdf;
    @SerializedName("webReaderLink")
    private String webReaderLink;
    @SerializedName("accessViewStatus")
    private String accessViewStatus;
    @SerializedName("quoteSharingAllowed")
    private boolean quoteSharingAllowed;

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getViewability() {
        return viewability;
    }

    public void setViewability(String viewability) {
        this.viewability = viewability;
    }

    public boolean isEmbeddable() {
        return embeddable;
    }

    public void setEmbeddable(boolean embeddable) {
        this.embeddable = embeddable;
    }

    public boolean isPublicDomain() {
        return publicDomain;
    }

    public void setPublicDomain(boolean publicDomain) {
        this.publicDomain = publicDomain;
    }

    public String getTextToSpeechPermission() {
        return textToSpeechPermission;
    }

    public void setTextToSpeechPermission(String textToSpeechPermission) {
        this.textToSpeechPermission = textToSpeechPermission;
    }

    public Epub getEpub() {
        return epub;
    }

    public void setEpub(Epub epub) {
        this.epub = epub;
    }

    public Pdf getPdf() {
        return pdf;
    }

    public void setPdf(Pdf pdf) {
        this.pdf = pdf;
    }

    public String getWebReaderLink() {
        return webReaderLink;
    }

    public void setWebReaderLink(String webReaderLink) {
        this.webReaderLink = webReaderLink;
    }

    public String getAccessViewStatus() {
        return accessViewStatus;
    }

    public void setAccessViewStatus(String accessViewStatus) {
        this.accessViewStatus = accessViewStatus;
    }

    public boolean isQuoteSharingAllowed() {
        return quoteSharingAllowed;
    }

    public void setQuoteSharingAllowed(boolean quoteSharingAllowed) {
        this.quoteSharingAllowed = quoteSharingAllowed;
    }
}
