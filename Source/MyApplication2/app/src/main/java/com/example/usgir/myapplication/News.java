package com.example.usgir.myapplication;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import static com.android.volley.toolbox.Volley.newRequestQueue;

public class News extends AppCompatActivity {
    ArrayList<String> stringArrayList,resultlist;
    ArrayList<String> links;
    ArrayAdapter arrayAdapter;
    JSONObject r;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news);
        links = new ArrayList<>();
        resultlist = new ArrayList<String>();
        resultlist = getIntent().getStringArrayListExtra("Topic");
        stringArrayList = new ArrayList<>();
        ListView listView = (ListView)findViewById(R.id.list);
        arrayAdapter = new ArrayAdapter(this,R.layout.listimageitem,R.id.textView3,stringArrayList);
        RequestQueue requestQueue = newRequestQueue(News.this);
        //Toast.makeText(News.this,s,Toast.LENGTH_LONG).show();
        int a =  resultlist.size();
        for (int j = 0;j<a;j++) {
            String s = resultlist.get(j).replace(" ", "%20");
            String uri = "http://content.guardianapis.com/search?q=" + s + "&api-key=3f969c5f-9f3b-4dbb-90a6-1b60d7406e6b";
            JsonObjectRequest objectRequest = new JsonObjectRequest(Request.Method.GET, uri, null, new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {
                    r = response;
                    try {
                        for (int i = 0; i < 10; i++) {
                            String s1 = response.getJSONObject("response").getJSONArray("results").getJSONObject(i).getString("webTitle");
                            String ssss = response.getJSONObject("response").getJSONArray("results").getJSONObject(i).getString("webUrl");
                            //Toast.makeText(News.this,"NO Exception buddy",Toast.LENGTH_LONG).show();
                            stringArrayList.add(s1);
                            links.add(ssss);
                            //Toast.makeText(News.this,s1,Toast.LENGTH_LONG).show();
                        }
                        arrayAdapter.notifyDataSetChanged();
                    } catch (JSONException e) {
                        Toast.makeText(News.this, e.toString(), Toast.LENGTH_LONG).show();
                    }
                }
            }, new Response.ErrorListener() {

                @Override
                public void onErrorResponse(VolleyError error) {
                    Toast.makeText(News.this, error.toString(), Toast.LENGTH_LONG).show();
                }
            });
            requestQueue.add(objectRequest);
        }
        listView.setAdapter(arrayAdapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    //Toast.makeText(News.this,r.getJSONObject("response").getJSONArray("results").getJSONObject(position).getString("webTitle"),Toast.LENGTH_LONG).show();
                    Intent intent = new Intent(News.this,Web.class);
                    intent.putExtra("uri",links.get(position));
                    startActivity(intent);

            }
        });
    }
}
