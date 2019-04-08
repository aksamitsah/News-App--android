package com.aksamitsah.newsapp;

import android.content.AsyncTaskLoader;
import android.content.Context;

import java.util.List;

public class NewsLoader extends AsyncTaskLoader<List<NewsData>> {
    /**
     * Query URL
     **/
    private String url;

    public NewsLoader(Context context, String url) {
        super(context);
        this.url = url;
    }

    @Override
    protected void onStartLoading() {
        forceLoad();
    }

    @Override
    public List<NewsData> loadInBackground() {
        if (url == null) {
            return null;
        }
        return QueryUtils.fetchNewsData(url, getContext());
    }
}
