package com.newsapp.newsapp;

import android.app.LoaderManager;
import android.content.Context;
import android.content.Intent;
import android.content.Loader;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.view.View;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements SearchView.OnQueryTextListener, LoaderManager.LoaderCallbacks<List<MyNews>> {


    private static final int NEWS_LOADER_ID = 1;
    public static final String LOG_TAG = MainActivity.class.getName();
    private static final String NEWS_REQUEST_URL = "http://content.guardianapis.com/search?q=debates&api-key=test&show-tags=contributor";

    AdapterMyNews adapter;
    RecyclerView.LayoutManager layoutManager;
    RecyclerView recyclerView;
    TextView mEmptyStateTextView;
    View mLoadingIndicator;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        mLoadingIndicator = findViewById(R.id.loadingIndicator);
        mEmptyStateTextView = (TextView) findViewById(R.id.emptyView);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        adapter = new AdapterMyNews(new ArrayList<MyNews>(), new AdapterMyNews.OnItemClickListener() {
            @Override
            public void onItemClick(MyNews myNews) {
                String url = myNews.getMyUrl();
                Intent i = new Intent(Intent.ACTION_VIEW);
                i.setData(Uri.parse(url));
                startActivity(i);
            }
        });

        recyclerView.setAdapter(adapter);

        ConnectivityManager connMgr = (ConnectivityManager)
                getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();

        if (networkInfo != null && networkInfo.isConnected()) {
            LoaderManager loaderManager = getLoaderManager();
            loaderManager.initLoader(NEWS_LOADER_ID, null, this);
        } else {
            mLoadingIndicator.setVisibility(View.GONE);
            recyclerView.setVisibility(View.GONE);
            mEmptyStateTextView.setVisibility(View.VISIBLE);
            mEmptyStateTextView.setText(R.string.no_internet_available);
        }
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        return false;
    }

    @Override
    public Loader<List<MyNews>> onCreateLoader(int i, Bundle bundle) {
        mLoadingIndicator.setVisibility(View.VISIBLE);
        recyclerView.setVisibility(View.GONE);
        Uri baseUri = Uri.parse(NEWS_REQUEST_URL);
        return new MyNewsLoader(this, baseUri.toString());
    }

    @Override
    public void onLoadFinished(Loader<List<MyNews>> loader, List<MyNews> myNews) {
        mLoadingIndicator.setVisibility(View.GONE);
        mEmptyStateTextView.setVisibility(View.VISIBLE);
        mEmptyStateTextView.setText(R.string.no_news);

        adapter.clear();

        if (myNews != null && !myNews.isEmpty()) {
            recyclerView.setVisibility(View.VISIBLE);
            mEmptyStateTextView.setVisibility(View.GONE);
            adapter.addAll(myNews);
        }
    }

    @Override
    public void onLoaderReset(Loader<List<MyNews>> loader) {
        adapter.clear();
    }

}
