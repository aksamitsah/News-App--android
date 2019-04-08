package com.aksamitsah.newsapp;

class NewsData {
    private String title;
    private String section;
    private String webPublicationDateAndTime;
    private String webUrl;
    private String thumbnail;
    private String authors;

    NewsData(String title, String section, String webPublicationDateAndTime, String webUrl, String thumbnail, String authors) {
        this.title = title;
        this.section = section;
        this.webPublicationDateAndTime = webPublicationDateAndTime;
        this.webUrl = webUrl;
        this.thumbnail = thumbnail;
        this.authors = authors;
    }

    String getTitle() {
        return title;
    }

    String getSection() {
        return section;
    }

    String getWebPublicationDateAndTime() {
        return webPublicationDateAndTime;
    }

    String getWebUrl() {
        return webUrl;
    }

    String getThumbnail() {
        return thumbnail;
    }

    String getAuthors() {
        return authors;
    }
}
