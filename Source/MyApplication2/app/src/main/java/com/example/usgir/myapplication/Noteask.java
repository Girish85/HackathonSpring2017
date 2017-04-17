package com.example.usgir.myapplication;

import android.app.ActivityOptions;
import android.app.Application;
import android.content.Context;
import android.content.Intent;
//import android.support.multidex.MultiDex;
import android.support.multidex.MultiDex;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.transition.Explode;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.ViewSwitcher;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class Noteask extends AppCompatActivity {

    ViewSwitcher viewSwitcher;
    Button create,edit;
    FirebaseDatabase database;
    DatabaseReference reference;
    DatabaseReference children;
    ListView listViewl;
    String key;
    ArrayAdapter arrayAdapter;
    ArrayList<String> oldnotes;
    String title;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_noteask);
      //  MultiDex.install(this);
        viewSwitcher = (ViewSwitcher)findViewById(R.id.switcher);
        listViewl = (ListView)findViewById(R.id.list);
        oldnotes = new ArrayList<String>();
        arrayAdapter = new ArrayAdapter(Noteask.this,R.layout.listnoteitem,R.id.test,oldnotes);
        key = getIntent().getStringExtra("email");
        database = FirebaseDatabase.getInstance();
        reference = database.getReference();
        children = database.getReference(key);
        children.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                title = dataSnapshot.getKey();
                oldnotes.add(title);
                arrayAdapter.notifyDataSetChanged();
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        listViewl.setAdapter(arrayAdapter);
        listViewl.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String s = oldnotes.get(position);
                Intent intent = new Intent(getApplicationContext(),old.class);
                intent.putExtra("title",s);
                intent.putExtra("Notes",key);
                startActivity(intent);
            }
        });

    }


    void create(View view)
    {
       // getWindow().requestFeature(Window.FEATURE_CONTENT_TRANSITIONS);
       // getWindow().setExitTransition(new Explode());
        Intent intent = new Intent(this,MainActivity.class);
        intent.putExtra("Notes",key);
        startActivity(intent);
    }
    void old(View view)
    {
        //oldnotes.add("Notes on Android");
        //oldnotes.add("Notes on Machine Learning");
       // arrayAdapter.notifyDataSetChanged();
        viewSwitcher.showNext();

    }

}
