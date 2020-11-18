 package com.example.budegtapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

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

 public class MainActivity extends AppCompatActivity {
     EditText email;
     EditText pass;
     TextView sginup;
     Button btnsgin;
     FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        auth=FirebaseAuth.getInstance();
        if(auth.getCurrentUser()!=null)
        {
            startActivity(new Intent(getApplicationContext(),HomeActivity.class));
            finish();
        }
        sginup=findViewById(R.id.sginup);
        btnsgin=findViewById(R.id.btnsginin);
        email=findViewById(R.id.email);
        pass=findViewById(R.id.password);
        String udata="to Sgin up";
        SpannableString content = new SpannableString(udata);
        content.setSpan(new UnderlineSpan(), 0, udata.length(), 0);
        sginup.setText(content);
        sginup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this,Register.class));
                finish();
            }
        });
    btnsgin.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            String toenail=email.getText().toString().trim();
            String tapas=pass.getText().toString().trim();
            if(TextUtils.isEmpty(toenail))
            {
                email.setError("is field requried");
                return;
            }
            if(TextUtils.isEmpty(tapas))
            {
                pass.setError("is field requried");
                return;
            }
            //sign in
            auth.signInWithEmailAndPassword(toenail,tapas).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (task.isSuccessful()) {

                        startActivity(new Intent(MainActivity.this,HomeActivity.class));
                        finish();


                        Toast.makeText(MainActivity.this, "Log in pass.",
                                Toast.LENGTH_SHORT).show();
                    } else {
                        // If sign in fails, display a message to the user.

                        Toast.makeText(MainActivity.this, "Log in failed.",
                                Toast.LENGTH_SHORT).show();

                    }
                }
            });
        }
    }
    );
    }

}