package com.markkarlsrud.mynews.activities;

import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import com.markkarlsrud.mynews.R;
import com.markkarlsrud.mynews.fragments.ArticleFragment;
import com.markkarlsrud.mynews.model.Article;
import com.markkarlsrud.mynews.model.NewsApiResponse;

import org.json.JSONObject;

import static org.apache.commons.lang3.StringEscapeUtils.unescapeJava;

public class NewsFeedActivity extends AppCompatActivity implements ArticleFragment.OnFragmentInteractionListener {

    private static final String BASE_URL = "https://newsapi.org";
    private static final String PATH ="/v1/articles";
    private static final String API_KEY = "07470b7a30394d0983d08463d2fe5e75";
    private static final Gson gson = new Gson();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news_feed);
        getFeed();
    }

    private void getFeed() {
        final JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, BASE_URL + PATH +
                "?apiKey=" + API_KEY + "&source=techcrunch", null, new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject response) {
                Log.v("NewFeedActivity", response.toString());
                addArticles(response.toString());
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.v("NewFeedActivity", error.getLocalizedMessage());
            }
        });

        Volley.newRequestQueue(this).add(request);
    }

    private void addArticles(String json) {
        LinearLayout scrollView = (LinearLayout) findViewById(R.id.news_scroll_layout);
        NewsApiResponse response = gson.fromJson(json, NewsApiResponse.class);
        for (final Article article : response.getArticles()) {
            Button button = new Button(this);
            button.setText(article.getTitle());
            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    ArticleFragment articleFragment = ArticleFragment.newInstance(article);
                    FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                    transaction.add(R.id.news_scroll_layout, articleFragment);
                    transaction.commit();
                }
            });
            scrollView.addView(button);
        }
    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }

    public void addDrawableFromUrl(String url, final ImageView imageView) {
        ImageRequest request = new ImageRequest(unescapeJava(url), new Response.Listener<Bitmap>() {
            @Override
            public void onResponse(Bitmap response) {
                imageView.setImageBitmap(response);
            }
        }, 0, 0, ImageView.ScaleType.CENTER, Bitmap.Config.ALPHA_8, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
            }
        });
        Volley.newRequestQueue(this).add(request);
    }
}
