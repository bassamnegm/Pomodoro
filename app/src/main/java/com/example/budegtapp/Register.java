package com.example.budegtapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.TextUtils;
import android.text.style.UnderlineSpan;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class Register extends AppCompatActivity {
    EditText email;
    EditText pass;
    TextView sginin;
    Button btnsgup;
    private FirebaseAuth auth;
    private ProgressDialog mDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        auth=FirebaseAuth.getInstance();
        mDialog=new ProgressDialog(this);
        setContentView(R.layout.activity_register);
        sginin=findViewById(R.id.sginin);
        btnsgup=findViewById(R.id.sginup);
        email=findViewById(R.id.email);
        pass=findViewById(R.id.password);
        ///make underline
        String udata="to Sgin in";
        SpannableString content = new SpannableString(udata);
        content.setSpan(new UnderlineSpan(), 0, udata.length(), 0);
        sginin.setText(content);
        //////
        sginin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent( Register.this,MainActivity.class));
                finish();
            }
        });
        
    btnsgup.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            String txtemail=email.getText().toString().trim();
            String txtpass=pass.getText().toString().trim();
            if(TextUtils.isEmpty(txtemail))
            {
                email.setError("is field requried");
                return;
            }
            if(TextUtils.isEmpty(txtpass))
            {
                pass.setError("is field requried");
                return;
            }
            mDialog.setMessage("processing.... ");
            mDialog.show();
            //sgin up
            auth.createUserWithEmailAndPassword(txtemail,txtpass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (task.isSuccessful()) {
                        mDialog.dismiss();
                        startActivity(new Intent(Register.this,HomeActivity.class));
                        finish();


                        Toast.makeText(Register.this, "Register pass.",
                                Toast.LENGTH_SHORT).show();
                    } else {
                        // If sign in fails, display a message to the user.

                        Toast.makeText(Register.this, "Register failed.",
                                Toast.LENGTH_SHORT).show();

                    }
                }
            });

        }
    });
    }

}