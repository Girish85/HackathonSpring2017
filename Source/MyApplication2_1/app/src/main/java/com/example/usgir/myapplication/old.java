package com.example.usgir.myapplication;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;
import android.widget.ViewSwitcher;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import static com.android.volley.toolbox.Volley.newRequestQueue;

public class old extends AppCompatActivity {
    EditText editText;
    FloatingActionButton fab;
    FirebaseDatabase database;
    DatabaseReference reference;
    DatabaseReference children;
    String title,keyer;
    String data;
    String indata;
    ArrayList<String> res;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_old);
        res = new ArrayList<>();
        editText = (EditText)findViewById(R.id.editText2);
        editText.setEnabled(false);
        database = FirebaseDatabase.getInstance();
        Intent intent = getIntent();
        title = intent.getStringExtra("title");
        keyer = intent.getStringExtra("Notes");
        reference = database.getReference(keyer);
        fab = (FloatingActionButton)findViewById(R.id.floatingActionButton3);
        children = reference.child(title);
        children.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                data = dataSnapshot.getValue().toString();
                editText.setText(data);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
    void click(View view)
    {
        editText.setEnabled(true);
        fab.setVisibility(View.VISIBLE);
    }
    void save(View view)
    {
        indata = editText.getText().toString();
        children.setValue(indata);
        editText.setEnabled(false);
        final String s2 = indata.replace(" ","%20");
        RequestQueue requestQueue2 = newRequestQueue(this);
        String uri2 = "https://api.aylien.com/api/v1/concepts?text="+s2;
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET,uri2,null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    String ss =response.toString();
                    JSONObject jsonObject = new JSONObject(ss.trim());
                    Iterator<?> keys = jsonObject.keys();
                    while (keys.hasNext())
                    {
                        String sn = (String)keys.next();
                        if (sn.equals("concepts"))
                        {
                            String sss = response.getJSONObject(sn).toString();
                            JSONObject o1 = new JSONObject(sss.trim());
                            Iterator<?> key = o1.keys();
                            while (key.hasNext())
                            {
                                String g1 = (String)key.next();
                                String result = response.getJSONObject(sn).getJSONObject(g1).getJSONArray("surfaceForms").getJSONObject(0).getString("string");
                                //textView.append(result+","+"\n");
                                Toast.makeText(getApplicationContext(),result,Toast.LENGTH_LONG).show();
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
        }){

            @Override
            public Map<String, String> getHeaders(){
                Map<String, String>  params = new HashMap<String, String>();
                params.put("X-AYLIEN-TextAPI-Application-Key","f8feffd2dcb39908d7d0ab83a8a1d41b");
                params.put("X-AYLIEN-TextAPI-Application-ID","1d7dd179");
                return params;
            }
        };
        requestQueue2.add(request);
    }
    void shower1()
    {
        if (res.isEmpty()!=true) {
            DialogInterface.OnClickListener listener = new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    Intent intent = new Intent(old.this, News.class);
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
}
