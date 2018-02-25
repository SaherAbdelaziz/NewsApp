package com.newsapp.newsapp;

import android.text.TextUtils;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by SaherOs on 2/23/2018.
 */

class Utils_query {public static final String LOG_TAG = Utils_query.class.getSimpleName();

    private Utils_query() {
    }

    public static ArrayList<MyNews> extractResultFromJson(String newsJSON) {
        if (TextUtils.isEmpty(newsJSON)) {
            return null;
        }

        ArrayList<MyNews> myNews = new ArrayList<>();
        try {
            JSONObject reader = new JSONObject(newsJSON);
            JSONObject response = reader.getJSONObject("response");
            JSONArray resultArray = response.getJSONArray("results");
            for (int i = 0; i < resultArray.length(); i++) {
                JSONObject news = resultArray.getJSONObject(i);
                String title = news.getString("webTitle");
                String type = news.getString("type");
                String date = news.getString("webPublicationDate");
                String section = news.getString("sectionName");
                String url = news.getString("webUrl");
                List<String> author = new ArrayList<>();
                if (news.has("tags")) {
                    JSONArray detailStags = news.getJSONArray("tags");
                    if (detailStags.length() == 0) {
                        author.add("no authors found..");
                    }
                    for (int j = 0; j < detailStags.length(); j++) {
                        JSONObject authorName = detailStags.getJSONObject(j);
                        String firstName = authorName.getString("webTitle");
                        if (firstName != null) {
                            author.add(firstName);
                        }
                    }
                } else {
                    author.add("no authors found..");
                }


                String[] authorsArray = author.toArray(new String[author.size()]);
                MyNews n = new MyNews(title, type, date, section, url, authorsArray);
                myNews.add(n);
            }
        } catch (JSONException e) {
            Log.e("QueryUtils", "Problem parsing the news JSON results", e);
        }

        return myNews;
    }


    public static List<MyNews> fetchNewsData(String requestUrl) {
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }


        URL url = createUrl(requestUrl);

        String jsonResponse = null;
        try {
            jsonResponse = makeHttpRequest(url);
        } catch (IOException e) {
            Log.e(LOG_TAG, "Error closing input stream", e);
        }

        return extractResultFromJson(jsonResponse);
    }

    private static URL createUrl(String stringUrl) {
        URL url = null;
        try {
            url = new URL(stringUrl);
        } catch (MalformedURLException e) {
            Log.e(LOG_TAG, "Error with creating URL ", e);
        }
        return url;
    }

    private static String makeHttpRequest(URL url) throws IOException {
        String jsonResponse = "";

        if (url == null) {
            return jsonResponse;
        }

        HttpURLConnection urlConnection = null;
        InputStream inputStream = null;
        try {
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setReadTimeout(10000);
            urlConnection.setConnectTimeout(15000);
            urlConnection.setRequestMethod("GET");
            urlConnection.connect();


            if (urlConnection.getResponseCode() == 200) {
                inputStream = urlConnection.getInputStream();
                jsonResponse = readFromStream(inputStream);
            } else {
                Log.e(LOG_TAG, "Error response code: " + urlConnection.getResponseCode());
            }
        } catch (IOException e) {
            Log.e(LOG_TAG, "Problem retrieving the news JSON results.", e);
        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
            if (inputStream != null) {
                inputStream.close();
            }
        }
        return jsonResponse;
    }

    private static String readFromStream(InputStream inputStream) throws IOException {
        StringBuilder output = new StringBuilder();
        if (inputStream != null) {
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream, Charset.forName("UTF-8"));
            BufferedReader reader = new BufferedReader(inputStreamReader);
            String line = reader.readLine();
            while (line != null) {
                output.append(line);
                line = reader.readLine();
            }
        }
        return output.toString();
    }

}
