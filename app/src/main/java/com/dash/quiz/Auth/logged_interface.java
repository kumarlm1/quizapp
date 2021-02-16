package com.dash.quiz.Auth;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.dash.quiz.R;

import java.util.ArrayList;

public class logged_interface extends AppCompatActivity {
    TextView txt;
    ArrayList<String> classes;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.logged_interface);
        txt=findViewById(R.id.txt);
        String name=getIntent().getStringExtra("name");
        String branch=getIntent().getStringExtra("branch");
        classes=getIntent().getStringArrayListExtra("classes");
        txt.setText(classes.toString());

        if(branch.equals("Student")) {


        Intent intent = new Intent(logged_interface.this,Student.class);
        intent.putExtra("name",name);
        intent.putExtra("branch",branch);
        intent.putExtra("classes",classes);
        startActivity(intent);
        }
        if(branch.equals("Staff")) {
            Intent intent = new Intent(logged_interface.this,Staff.class);
            intent.putExtra("name",name);
            intent.putExtra("branch",branch);
            intent.putExtra("classes",classes);
            startActivity(intent);
        }









    }


    @Override
    public void onBackPressed() {

    }
}
