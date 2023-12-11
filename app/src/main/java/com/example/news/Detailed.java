package com.example.news;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

public class Detailed extends AppCompatActivity {

    TextView titleId, sourceId, timeId, descId;
    ImageView imageView;
    WebView webView;
    ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detailed);

        titleId = findViewById(R.id.titleId);
        sourceId = findViewById(R.id.sourceId);
        timeId = findViewById(R.id.dateId);
        descId = findViewById(R.id.descId);
        imageView = findViewById(R.id.image);
        webView = findViewById(R.id.webView);
        progressBar = findViewById(R.id.loaderWebView);
        progressBar.setVisibility(View.VISIBLE);

        Intent intent = getIntent();
        String title = intent.getStringExtra("title");
        String source = intent.getStringExtra("source");
        String date = intent.getStringExtra("date");
        String desc = intent.getStringExtra("desc");
        String imageUrl = intent.getStringExtra("imageUrl");
        String url = intent.getStringExtra("url");

        titleId.setText(title);
        sourceId.setText(source);
        timeId.setText(date);
        descId.setText(desc);

        Picasso.with(Detailed.this).load(imageUrl).into(imageView);
        webView.getSettings().setDomStorageEnabled(true);
        webView.getSettings().setJavaScriptEnabled(true);
        webView.getSettings().setLoadsImagesAutomatically(true);
        webView.setScrollBarStyle(View.SCROLLBARS_INSIDE_OVERLAY);
        webView.setWebViewClient(new WebViewClient());
        webView.loadUrl(url);
        if(webView.isShown()){
            progressBar.setVisibility(View.INVISIBLE);
        }

    }
}