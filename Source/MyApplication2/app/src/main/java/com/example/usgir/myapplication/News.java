package com.example.usgir.myapplication;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import static com.android.volley.toolbox.Volley.newRequestQueue;

public class News extends AppCompatActivity {
    ArrayList<String> stringArrayList, resultlist;
    ArrayList<String> links;
    NewsAdapter arrayAdapter;
    JSONObject Response;
    RecyclerView.LayoutManager layoutManager;
    Toolbar mToolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news);
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);
        setTitle("News");
        getSupportActionBar().setHomeButtonEnabled(false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        links = new ArrayList<>();
        resultlist = new ArrayList<String>();
        resultlist = getIntent().getStringArrayListExtra("Topic");
        stringArrayList = new ArrayList<>();
        final RecyclerView listView = (RecyclerView) findViewById(R.id.newslist);

        RequestQueue requestQueue = newRequestQueue(News.this);

        //Toast.makeText(News.this,s,Toast.LENGTH_LONG).show();
        int a = resultlist.size();
        for (int j = 0; j < a; j++) {
            String s = resultlist.get(j).replace(" ", "%20");
            String uri = "http://content.guardianapis.com/search?q=" + s + "&api-key=3f969c5f-9f3b-4dbb-90a6-1b60d7406e6b";
            JsonObjectRequest objectRequest = new JsonObjectRequest(Request.Method.GET, uri, null, new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {
                    Response = response;
                    stringArrayList.clear();
                    try {
                        for (int i = 0; i < 10; i++) {
                            String s1 = response.getJSONObject("response").getJSONArray("results").getJSONObject(i).getString("webTitle");
                            String ssss = response.getJSONObject("response").getJSONArray("results").getJSONObject(i).getString("webUrl");
                            //Toast.makeText(News.this,"NO Exception buddy",Toast.LENGTH_LONG).show();
                            stringArrayList.add(s1);
                            links.add(ssss);
                            listView.setAdapter(arrayAdapter);arrayAdapter.notifyDataSetChanged();
                            //Toast.makeText(News.this,s1,Toast.LENGTH_LONG).show();
                        }

                    } catch (JSONException e) {
                       // Toast.makeText(News.this, e.toString(), Toast.LENGTH_LONG).show();
                    }
                }
            }, new Response.ErrorListener() {

                @Override
                public void onErrorResponse(VolleyError error) {
                   // Toast.makeText(News.this, error.toString(), Toast.LENGTH_LONG).show();
                }
            });
            requestQueue.add(objectRequest);
        }
        arrayAdapter = new NewsAdapter(this,links,stringArrayList);//this,R.layout.listimageitem,R.id.textView3,stringArrayList
        listView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(getApplicationContext());
        listView.setLayoutManager(layoutManager);
        listView.setAdapter(arrayAdapter);arrayAdapter.notifyDataSetChanged();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                this.finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
