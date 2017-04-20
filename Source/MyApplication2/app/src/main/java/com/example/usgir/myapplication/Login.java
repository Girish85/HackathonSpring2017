package com.example.usgir.myapplication;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class Login extends AppCompatActivity {
    FirebaseAuth author;
    FirebaseAuth.AuthStateListener listener;
    FirebaseUser user;
    int i = 0;
    String emailid, Pwd;
    EditText editText1;
    EditText editText2;
    boolean emailer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle("Editor++");
        setContentView(R.layout.activity_login);
        author = FirebaseAuth.getInstance();
        TextView Text20 = (TextView) findViewById(R.id.Text20);
        TextView textView = (TextView) findViewById(R.id.textView);
        Typeface tf = Typeface.createFromAsset(getAssets(), "fonts/AvenirLTStd-Heavy.otf");
        Text20.setTypeface(tf);
        textView.setTypeface(tf);
        if (utilities.isConnectionAvailable(getApplicationContext())) {
            listener = new FirebaseAuth.AuthStateListener() {
                @Override
                public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {

                }
            };
            author.addAuthStateListener(listener);
        } else {
            Toast.makeText(this, "Please check internet connection", Toast.LENGTH_SHORT).show();
        }
    }

    void onsignup(View view) {
        Intent intent = new Intent(this, signup.class);
        startActivity(intent);
    }

    void login(View view) {
        editText1 = (EditText) findViewById(R.id.email);
        Typeface tf = Typeface.createFromAsset(getAssets(), "fonts/AvenirLTStd-Book.otf");
        emailid = editText1.getText().toString();
        editText1.setTypeface(tf);
        editText2 = (EditText) findViewById(R.id.password);
        editText2.setTypeface(tf);
        Pwd = editText2.getText().toString();

        if (!emailid.isEmpty() && emailid.length() > 0) {

            if (!Pwd.isEmpty() && Pwd.length() > 0) {
                if (utilities.isConnectionAvailable(this)) {
                    userver();
                }else {
                    Toast.makeText(this,"Please check internet connection",Toast.LENGTH_SHORT).show();
                }

            } else {
                editText2.setError("Password should not be Empty");
            }
        } else {
            editText1.setError("Email should not be Empty");
        }

    }

    void userver() {

        author.signInWithEmailAndPassword(emailid, Pwd).addOnSuccessListener(new OnSuccessListener<AuthResult>() {
            @Override
            public void onSuccess(AuthResult authResult) {
                verify();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(Login.this, "Invalid id/password", Toast.LENGTH_LONG).show();
                Intent intent = new Intent(Login.this, Noteselection.class);
                String em = emailid.replace(".", "@");
                intent.putExtra("email", em);
                startActivity(intent);
            }
        });

    }

    void verify() {
        user = author.getCurrentUser();
        boolverify();
    }

    void boolverify() {
        if (user.isEmailVerified() == true) {
            i = 0;
            Intent intent = new Intent(Login.this, Noteselection.class);
            String em = emailid.replace(".", "@");
            intent.putExtra("email", em);
            startActivity(intent);
            //Toast.makeText(getApplicationContext(),"Verification done",Toast.LENGTH_LONG).show();
        }
        if (user.isEmailVerified() == false) {
            AlertDialog.Builder b = new AlertDialog.Builder(Login.this);
            b.setCancelable(true);
            b.setTitle("Email Verification Required");
            b.setMessage("Signin successful but \n Email Verification is undone\n plzz verify your email by \nclicking on the link sent ");
            b.show();
            //Toast.makeText(getApplicationContext(),"Verification Incomplete\n you need to verify your email",Toast.LENGTH_LONG).show();
        }
    }

}
