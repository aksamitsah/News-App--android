package com.aksamitsah.newsapp;

import android.app.LoaderManager;
import android.content.Context;
import android.content.Intent;
import android.content.Loader;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class NewsActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<List<NewsData>> {

    private static final int NEWS_LOADER_ID = 1;
    private static final String GUARDIANS_REQUEST_URL = "http://content.guardianapis.com/search?";
    private static final String GUARDIANS_GAMES_URL = "https://www.theguardian.com/games";
    private NewsAdapter newsAdapter;
    private ListView newsListView;
    private TextView emptyStateTextView;
    private View loadingIndicator;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news);

        emptyStateTextView = findViewById(R.id.empty_state);
        loadingIndicator = findViewById(R.id.loading_indicator);
        newsListView = findViewById(R.id.list);

        newsAdapter = new NewsAdapter(this, new ArrayList<NewsData>());

        newsListView.setAdapter(newsAdapter);

        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = null;
        if (cm != null) {
            networkInfo = cm.getActiveNetworkInfo();
        }
        if (networkInfo != null && networkInfo.isConnected()) {
            LoaderManager loaderManager = getLoaderManager();
            loaderManager.initLoader(NEWS_LOADER_ID, null, this);
        } else {
            loadingIndicator.setVisibility(View.GONE);
            emptyStateTextView.setText(getString(R.string.no_internet_connection));
            emptyStateTextView.setVisibility(View.VISIBLE);
        }

        newsListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                NewsData currentNewsData = newsAdapter.getItem(position);
                if (currentNewsData != null) {
                    String webUrl = currentNewsData.getWebUrl();
                    Intent webIntent = new Intent(Intent.ACTION_VIEW);
                    if (webUrl != null) {
                        webIntent.setData(Uri.parse(webUrl));
                    } else {
                        webIntent.setData(Uri.parse(GUARDIANS_GAMES_URL));
                    }
                    startActivity(webIntent);
                }
            }
        });
    }

    @Override
    public Loader<List<NewsData>> onCreateLoader(int i, Bundle bundle) {

        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);

        String minNews = sharedPreferences.getString(getString(R.string.settings_min_news_key), getString(R.string.settings_min_news_default));
        String orderBy = sharedPreferences.getString(getString(R.string.settings_order_by_key), getString(R.string.settings_order_by_default));
        String section = sharedPreferences.getString(getString(R.string.settings_section_news_key), getString(R.string.settings_section_news_default));

        Uri baseUri = Uri.parse(GUARDIANS_REQUEST_URL);
        Uri.Builder uriBuilder = baseUri.buildUpon();

        uriBuilder.appendQueryParameter("api-key", "test");
        uriBuilder.appendQueryParameter("show-tags", "contributor");
        uriBuilder.appendQueryParameter("show-fields", "thumbnail");
        uriBuilder.appendQueryParameter("page-size", minNews);
        uriBuilder.appendQueryParameter("order-by", orderBy);

        if (!section.equals(getString(R.string.settings_section_news_default))) {
            uriBuilder.appendQueryParameter("section", section);
        }

        return new NewsLoader(this, uriBuilder.toString());
    }

    @Override
    public void onLoadFinished(Loader<List<NewsData>> loader, List<NewsData> newsData) {

        loadingIndicator.setVisibility(View.GONE);
        emptyStateTextView.setText(getString(R.string.no_news_found));
        newsListView.setEmptyView(emptyStateTextView);
        newsAdapter.clear();
        if (newsData != null && !newsData.isEmpty()) {
            newsAdapter.addAll(newsData);
        }

    }

    @Override
    public void onLoaderReset(Loader<List<NewsData>> loader) {
        newsAdapter.clear();
    }

    @Override
    // This method initialize the contents of the Activity's options menu.
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the Options Menu we specified in XML
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    // This method is called whenever an item in the options menu is selected.
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            Intent settingsIntent = new Intent(this, SettingsActivity.class);
            startActivity(settingsIntent);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
