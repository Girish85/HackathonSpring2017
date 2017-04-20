package com.example.usgir.myapplication;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.ViewSwitcher;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;


public class Noteselection extends AppCompatActivity {

    Button createNote, editnote;
    FirebaseDatabase database;
    DatabaseReference reference;
    DatabaseReference children;
    ListView listViewl;
    ViewSwitcher viewSwitcher;
    String key;
    ArrayAdapter arrayAdapter;
    ArrayList<String> oldnotes;
    String title;
    Toolbar mToolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_noteselection);
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);
        setTitle(getResources().getString(R.string.app_name));
        getSupportActionBar().setHomeButtonEnabled(false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        //  MultiDex.install(this);

        viewSwitcher = (ViewSwitcher) findViewById(R.id.switcher);
        listViewl = (ListView) findViewById(R.id.list);
        oldnotes = new ArrayList<String>();
        Typeface tf = Typeface.createFromAsset(getAssets(), "fonts/Avenir Roman.otf");
        createNote = (Button) findViewById(R.id.createnew);
        editnote = (Button) findViewById(R.id.editnote);
        createNote.setTypeface(tf);
        editnote.setTypeface(tf);
        arrayAdapter = new ArrayAdapter(this, R.layout.listnoteitem, R.id.test, oldnotes);
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
                Intent intent = new Intent(getApplicationContext(), OldNote.class);
                intent.putExtra("title", s);
                intent.putExtra("Notes", key);
                startActivity(intent);
            }
        });

    }


    void create(View view) {
        // getWindow().requestFeature(Window.FEATURE_CONTENT_TRANSITIONS);
        // getWindow().setExitTransition(new Explode());
        Intent intent = new Intent(this, MainActivity.class);
        intent.putExtra("Notes", key);
        startActivity(intent);
    }

    void old(View view) {
        //oldnotes.add("Notes on Android");
        //oldnotes.add("Notes on Machine Learning");
        // arrayAdapter.notifyDataSetChanged();
        viewSwitcher.showNext();

    }

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
