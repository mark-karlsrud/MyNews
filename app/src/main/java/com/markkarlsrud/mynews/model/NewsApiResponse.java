package com.markkarlsrud.mynews.model;

/**
 * Created by mkarlsru on 12/22/16.
 */
public class NewsApiResponse {
    private final String status;
    private final String source;
    private final String sortBy;
    private final Article[] articles;

    public NewsApiResponse(String status, String source, String sortBy, Article[] articles) {
        this.status = status;
        this.source = source;
        this.sortBy = sortBy;
        this.articles = articles;
    }

    public String getStatus() {
        return status;
    }

    public String getSource() {
        return source;
    }

    public String getSortBy() {
        return sortBy;
    }

    public Article[] getArticles() {
        return articles;
    }
}
