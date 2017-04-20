package com.example.usgir.myapplication;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;
import android.widget.ViewSwitcher;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class index extends AppCompatActivity {
    FirebaseAuth author;
    FirebaseAuth.AuthStateListener listener;
    FirebaseUser user;
    int i=0;
    String emailid , Pwd;
    EditText editText1;
    EditText editText2;
    boolean emailer;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_index);
        author = FirebaseAuth.getInstance();
        listener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {

            }
        };
        author.addAuthStateListener(listener);
    }
    void onsignup(View view)
    {
        Intent intent = new Intent(this,signup.class);
        startActivity(intent);
    }
    void login(View view){
        editText1 = (EditText)findViewById(R.id.editText96);
        emailid = editText1.getText().toString();
        editText2 = (EditText)findViewById(R.id.editText97);
        Pwd = editText2.getText().toString();
        userver();
    }
    void userver()
    {

        author.signInWithEmailAndPassword(emailid, Pwd).addOnSuccessListener(new OnSuccessListener<AuthResult>() {
            @Override
            public void onSuccess(AuthResult authResult) {
                verify();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
              Toast.makeText(index.this,"Invalid id/password",Toast.LENGTH_LONG).show();
            }
        });

    }
    void verify()
    {
        user = author.getCurrentUser();
        boolverify();
    }
    void boolverify()
    {
        if (user.isEmailVerified()==true)
        {
            i=0;
            Intent intent = new Intent(index.this,Noteask.class);
            String em = emailid.replace(".","@");
            intent.putExtra("email",em);
            startActivity(intent);
            //Toast.makeText(getApplicationContext(),"Verification done",Toast.LENGTH_LONG).show();
        }
        if (user.isEmailVerified()==false)
        {
            AlertDialog.Builder b = new AlertDialog.Builder(index.this);
            b.setCancelable(true);
            b.setTitle("Email Verification Required");
            b.setMessage("Signin successful but \n Email Verification is undone\n plzz verify your email by \nclicking on the link sent ");
            b.show();
            //Toast.makeText(getApplicationContext(),"Verification Incomplete\n you need to verify your email",Toast.LENGTH_LONG).show();
        }
    }

}
