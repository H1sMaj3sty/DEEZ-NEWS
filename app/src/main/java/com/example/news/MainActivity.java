package com.example.news;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.news.Models.Articles;
import com.example.news.Models.Headlines;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import javax.microedition.khronos.egl.EGLDisplay;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {

    RecyclerView recyclerView, recyclerViewCategories;
    SwipeRefreshLayout swipeRefreshLayout;
    EditText etQuery;
    Button btnSearch;
    final String API_KEY = BuildConfig.API_KEY;
    Adapter adapter;
    List<Articles> articles = new ArrayList<>();
    BottomNavigationView bottomNavigationView;
    boolean isId = true;
    String country = getCountry();
    String[] categories = {"all", "business", "entertainment", "general", "health", "science", "sports", "tech"};
    String selectedCategory = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        etQuery = findViewById(R.id.editText);
        btnSearch = findViewById(R.id.button);
        swipeRefreshLayout = findViewById(R.id.swipeRefresh);
        recyclerView = findViewById(R.id.recyclerView);
        recyclerViewCategories = findViewById(R.id.recyclerViewHorizontal);
        bottomNavigationView = findViewById(R.id.bottom_nav_view);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        recyclerViewCategories.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        recyclerViewCategories.setAdapter(new AdapterCategory(this, categories));

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                retreiveJson("",country, selectedCategory,  API_KEY);
            }
        });
        retreiveJson("", country, selectedCategory,  API_KEY);

        etQuery.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
                        @Override
                        public void onRefresh() {
                            retreiveJson(v.getText().toString(),country, selectedCategory,  API_KEY);
                        }
                    });
                    retreiveJson(v.getText().toString(), country, selectedCategory,  API_KEY);
                }
                return false;
            }
        });

        btnSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!etQuery.getText().toString().equals("")) {
                    swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
                        @Override
                        public void onRefresh() {
                            retreiveJson(etQuery.getText().toString(), country, selectedCategory,  API_KEY);
                        }
                    });
                    retreiveJson(etQuery.getText().toString(), country, selectedCategory,  API_KEY);
                } else {
                    swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
                        @Override
                        public void onRefresh() {
                            retreiveJson("",country, selectedCategory,  API_KEY);
                        }
                    });
                    retreiveJson("", country, selectedCategory,  API_KEY);
                }
            }
        });

        bottomNavigationView.setSelectedItemId(R.id.bottom_home);
        bottomNavigationView.setOnNavigationItemSelectedListener(item -> {
            if (item.getItemId() == R.id.bottom_home) {
                return true;
            } else if (item.getItemId() == R.id.bottom_country) {
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_right);
                if (isId == true) {
                    country = "id";
                    swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
                        @Override
                        public void onRefresh() {
                            retreiveJson("",country, selectedCategory,  API_KEY);
                        }
                    });
                    retreiveJson("", country, selectedCategory,  API_KEY);
                    isId = false;
                } else {
                    country = getCountry();
                    swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
                        @Override
                        public void onRefresh() {
                            retreiveJson("",country, selectedCategory,  API_KEY);
                        }
                    });
                    retreiveJson("", country, selectedCategory,  API_KEY);
                    isId = true;
                }
            } else if (item.getItemId() == R.id.profile) {
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_right);
                startActivity(new Intent(getApplicationContext(), Profile.class));
            }
            return false;
        });
    }

    public void retreiveJson(String query,String country, String category, String apiKey) {

        swipeRefreshLayout.setRefreshing(true);
        Call<Headlines> call;
        if (query != "") {
            call = ApiClient.getInstance().getApi().getSpecificData(query, apiKey);
        } else {
            call = ApiClient.getInstance().getApi().getHeadLines(country, category, apiKey);
        }
        call.enqueue(new Callback<Headlines>() {
            @Override
            public void onResponse(Call<Headlines> call, Response<Headlines> response) {
                if (response.isSuccessful() && response.body().getArticles() != null) {
                    swipeRefreshLayout.setRefreshing(false);
                    articles.clear();
                    articles = response.body().getArticles();
                    adapter = new Adapter(MainActivity.this, articles);
                    recyclerView.setAdapter(adapter);
                }
            }

            @Override
            public void onFailure(Call<Headlines> call, Throwable t) {
                swipeRefreshLayout.setRefreshing(false);
                Toast.makeText(MainActivity.this, t.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    public String getCountry() {
        Locale locale = Locale.getDefault();
        String country = locale.getCountry();
        return country.toLowerCase();
    }

    public void onCategoryClicked(String category) {
        selectedCategory = category;
        if (selectedCategory == "all") {
            selectedCategory = "";
        } else if (selectedCategory == "tech") {
            selectedCategory = "technology";
        }
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                retreiveJson("",country, selectedCategory, API_KEY);
            }
        });
        retreiveJson("", country, selectedCategory, API_KEY);

    }
}