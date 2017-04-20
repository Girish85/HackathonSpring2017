package com.example.usgir.myapplication;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import android.widget.ViewSwitcher;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import static com.android.volley.toolbox.Volley.newRequestQueue;

public class MainActivity extends AppCompatActivity {
    EditText editText, texter;
    FirebaseDatabase database;
    DatabaseReference reference, chilren;
    String title;
    //String res = "computers";
    ArrayList<String> res;
    ViewSwitcher viewSwitcher;
    Button button;
    Toolbar mToolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);
        setTitle(getResources().getString(R.string.app_name));
        getSupportActionBar().setHomeButtonEnabled(false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        res = new ArrayList<>();
        editText = (EditText) findViewById(R.id.editText3);
        texter = (EditText) findViewById(R.id.editText);
        String keysdata = getIntent().getStringExtra("Notes");
        database = FirebaseDatabase.getInstance();
        reference = database.getReference(keysdata);
        viewSwitcher = (ViewSwitcher) findViewById(R.id.sw1);
        button = (Button) findViewById(R.id.imageButton);
    }

    void click1(View view) {
        title = texter.getText().toString();
       viewSwitcher.showNext();

    }

    void clicker(View view) {
        final String s = editText.getText().toString();
        editText.setEnabled(false);
        chilren = reference.child(title);
        chilren.setValue(s);
        button.setVisibility(View.VISIBLE);
        final String s2 = s.replace(" ", "%20");
        RequestQueue requestQueue2 = newRequestQueue(this);
        String uri2 = "https://api.aylien.com/api/v1/concepts?text=" + s2;
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, uri2, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    String ss = response.toString();
                    JSONObject jsonObject = new JSONObject(ss.trim());
                    Iterator<?> keys = jsonObject.keys();
                    while (keys.hasNext()) {
                        String sn = (String) keys.next();
                        if (sn.equals("concepts")) {
                            String sss = response.getJSONObject(sn).toString();
                            JSONObject o1 = new JSONObject(sss.trim());
                            Iterator<?> key = o1.keys();
                            while (key.hasNext()) {
                                String g1 = (String) key.next();
                                String result = response.getJSONObject(sn).getJSONObject(g1).getJSONArray("surfaceForms").getJSONObject(0).getString("string");
                                //textView.append(result+","+"\n");
                                res.add(result);
                                shower1();

                            }
                        }
                    }
                    // Toast.makeText(MainActivity.this,ss,Toast.LENGTH_LONG).show();
                } catch (JSONException e) {
                    e.printStackTrace();
                    //res = "computerScience";
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                //Toast.makeText(MainActivity.this,error.toString(),Toast.LENGTH_LONG).show();
            }
        }) {

            @Override
            public Map<String, String> getHeaders() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("X-AYLIEN-TextAPI-Application-Key", "f8feffd2dcb39908d7d0ab83a8a1d41b");
                params.put("X-AYLIEN-TextAPI-Application-ID", "1d7dd179");
                return params;
            }
        };
        requestQueue2.add(request);
    }

    void enabler(View view) {
        editText.setEnabled(true);
    }

    void shower1() {
        if (res.isEmpty() != true) {
            DialogInterface.OnClickListener listener = new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    Intent intent = new Intent(MainActivity.this, News.class);
                    intent.putExtra("Topic", res);
                    startActivity(intent);
                }
            };
            //text.setText(s);
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setCancelable(true);
            builder.setTitle("Updates on your Notes");
            builder.setMessage("News you may be interested on");
            builder.setIcon(R.drawable.news);
            builder.setPositiveButton("GO", (DialogInterface.OnClickListener) listener);
            builder.setIconAttribute(android.R.attr.alertDialogIcon);
            builder.show();
        }
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
