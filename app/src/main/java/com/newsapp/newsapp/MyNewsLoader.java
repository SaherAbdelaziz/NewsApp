package com.newsapp.newsapp;

import android.content.Context;
import java.util.List;

/**
 * Created by SaherOs on 2/23/2018.
 */

class MyNewsLoader extends android.content.AsyncTaskLoader<List<MyNews>> {
    private String mUrl;

    public MyNewsLoader(Context context, String url) {
        super(context);
        mUrl = url;
    }

    @Override
    protected void onStartLoading() {
        forceLoad();
    }


    @Override
    public List<MyNews> loadInBackground() {
        if (mUrl.length() < 1 || mUrl == null) {
            return null;
        }

        return Utils_query.fetchNewsData(mUrl);

    }
}
