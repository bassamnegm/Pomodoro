package com.example.budegtapp;
import androidx.annotation.ColorInt;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.budegtapp.Model.Data;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.DateFormat;
import java.util.Date;

public class HomeActivity extends AppCompatActivity {
    RecyclerView recyclerView;
    private FirebaseAuth auth;
    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference databaseReference;
    private String key;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        auth=FirebaseAuth.getInstance();
        FirebaseUser firebaseUser=auth.getCurrentUser();
        String id=firebaseUser.getUid();
        firebaseDatabase=FirebaseDatabase.getInstance();
        databaseReference= firebaseDatabase.getReference("Alldata").child(String.valueOf(auth.getUid()));
        // tooolbar
        Toolbar toolbar=findViewById(R.id.toolbar);
        toolbar.setTitle("Pomodoro");
        setSupportActionBar(toolbar);
        /////////
        //Recycler settings
        recyclerView=findViewById(R.id.recy);
        LinearLayoutManager linearLayoutManager=new LinearLayoutManager(this);
        linearLayoutManager.setStackFromEnd(true);
        linearLayoutManager.setReverseLayout(true);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(linearLayoutManager);
        ////////
        FloatingActionButton floatingActionButton=findViewById(R.id.fab);
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                add();

            }
        });
    }
    //add data in firenase and show dialog
    public void add()
    {
        AlertDialog.Builder mBuilder=new AlertDialog.Builder(this);
        LayoutInflater inflater=LayoutInflater.from(this);
        View view=inflater.inflate(
                R.layout.input,null);
        mBuilder.setView(view);
        final AlertDialog dialog=mBuilder.create();
        dialog.show();
        final EditText tit=view.findViewById(R.id.titl);
        final EditText descreption=view.findViewById(R.id.des);
        Button btnsvae =view.findViewById(R.id.save);
        btnsvae.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String txttitle=tit.getText().toString().trim();
                String txtdes=descreption.getText().toString().trim();
                if(TextUtils.isEmpty(txttitle))
                {
                    tit.setError("is field requried");
                    return;
                }
                if(TextUtils.isEmpty(txtdes))
                {
                    descreption.setError("is field requried");
                    return;
                }
                String date= DateFormat.getDateInstance().format(new Date());
                String mid=databaseReference.push().getKey();
                Data data=new Data(txttitle,txtdes,mid,date);
                databaseReference.push().setValue(data);
                dialog.dismiss();

            }
        });

    }
    //return data from firebase and put in RecyclerAdapter
    @Override
    protected void onStart() {
        super.onStart();
        FirebaseRecyclerAdapter<Data,Holder>adapter= new FirebaseRecyclerAdapter<Data, Holder>
                (
                        Data.class,
                        R.layout.item,
                        Holder.class,
                        databaseReference

                ) {
            @Override
            protected void populateViewHolder(Holder holder, Data data, final int i) {
                holder.settitle(data.getTitle());
                holder.setdes(data.getDescreption());
                holder.setdate(data.getDate());
                holder.view.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        Intent intent=new Intent(HomeActivity.this,Count.class);
                        startActivity(intent);
                        key=getRef(i).getKey();
                        databaseReference.child(key).removeValue();
                        
                    }
                });

            }
        };
        recyclerView.setAdapter(adapter);
    }
    public static class Holder extends RecyclerView.ViewHolder{
        View view;

        public Holder(@NonNull View itemView) {
            super(itemView);
            this.view=itemView;
        }
        public void settitle(String title)
        {
            TextView txttitle=view.findViewById(R.id.titleid);
            txttitle.setText(title);
        }
        public void setdes(String des)
        {
            TextView txtdes=view.findViewById(R.id.desid);
            txtdes.setText(des);
        }
        public void setdate(String date)
        {
            TextView txtdate=view.findViewById(R.id.dateid);
            txtdate.setText(date);
        }
    }
///menu
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId())
        {
            case R.id.logout:
                auth.signOut();
                startActivity(new Intent(HomeActivity.this,MainActivity.class));
                finish();
                break;

        }
        return super.onOptionsItemSelected(item);
    }
}