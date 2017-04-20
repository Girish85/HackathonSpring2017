package com.example.usgir.myapplication;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class signup extends AppCompatActivity {
    public FirebaseAuth mauth;
    public FirebaseAuth.AuthStateListener mauthStateListener;
    EditText emailId;
    FirebaseUser user;
    FirebaseDatabase database;
    DatabaseReference reference;
    EditText pwd;
    Button signupbtn;
    ImageButton userid;
    ImageButton pawrd;
    String emailid, pawd;
    int i = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        database = FirebaseDatabase.getInstance();
        reference = database.getReference();
        mauth = FirebaseAuth.getInstance();
        Typeface tf = Typeface.createFromAsset(getAssets(), "fonts/Avenir Roman.otf");
        EditText lastName = (EditText) findViewById(R.id.lastName);
        EditText firstname = (EditText) findViewById(R.id.firstname);
        signupbtn = (Button)findViewById(R.id.signupbtn);
        signupbtn.setTypeface(tf);
        lastName.setTypeface(tf);
        firstname.setTypeface(tf);
        mauthStateListener = new FirebaseAuth.AuthStateListener() {

            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();
                if (firebaseUser != null) {
                    //Toast.makeText(getApplicationContext(),"Signed in",Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(getApplicationContext(), "Signed out", Toast.LENGTH_LONG).show();
                }
            }
        };
        mauth.addAuthStateListener(mauthStateListener);
        emailId = (EditText) findViewById(R.id.emailid);
        emailId.setTypeface(tf);
        //code = (EditText)findViewById(R.id.editText5);
        pwd = (EditText) findViewById(R.id.password);
        pwd.setTypeface(tf);
        //userid = (ImageButton)findViewById(R.id.imageButton42);
        //passcode=(ImageButton)findViewById(R.id.imageButton3);
        pawrd = (ImageButton) findViewById(R.id.imageButton44);
    }

    void onPassword(View view) {
        emailid = emailId.getText().toString();
        pawd = pwd.getText().toString();
        mauth.createUserWithEmailAndPassword(emailid, pawd).addOnSuccessListener(new OnSuccessListener<AuthResult>() {
            @Override
            public void onSuccess(AuthResult authResult) {
                String keyer = emailid.replace(".", "@");
                reference.child(keyer).setValue(" ");
                AlertDialog.Builder b = new AlertDialog.Builder(signup.this);
                b.setCancelable(true);
                b.setTitle("Signup Successful");
                b.setMessage("You have Successfully Signed Up ,\n You can access this application now");
                b.show();
                mailverify();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                 Toast.makeText(getApplicationContext(),"Failed",Toast.LENGTH_LONG).show();
            }
        });
    }

    void mailverify() {
        user = mauth.getCurrentUser();
        verifier();
    }

    void verifier() {
        if (user != null) {
            user.sendEmailVerification().addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void aVoid) {
                    Toast.makeText(getApplicationContext(), "Plzz verify your email by clicking on the link sent to your email", Toast.LENGTH_LONG).show();
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    i++;
                    if (i < 2) {
                        verifier();
                    } else
                        Toast.makeText(getApplicationContext(), "couldn't send email verification", Toast.LENGTH_LONG).show();
                }
            });
        }
        Intent i = new Intent(signup.this, Login.class);
        startActivity(i);
    }
}
