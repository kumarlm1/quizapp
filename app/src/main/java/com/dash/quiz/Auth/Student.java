package com.dash.quiz.Auth;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Adapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.dash.quiz.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreSettings;

import java.lang.reflect.Array;
import java.util.ArrayList;

public class Student extends AppCompatActivity {
    private static final String TAG ="student activity" ;
    static String class_nam;
    String name,branch;
   ArrayList<String> classes=new ArrayList<>();;
   EditText class_name;
   Button add_class;
   FirebaseFirestore db=FirebaseFirestore.getInstance();
   RecyclerView recyclerView;
   static View.OnClickListener onClickListener;
   public static ArrayList<MyData> data=new ArrayList<>();
   RecyclerView.Adapter adapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.student);
        name=getIntent().getStringExtra("name");
        classes=getIntent().getStringArrayListExtra("classes");
        branch=getIntent().getStringExtra("branch");

        onClickListener=new card_view_click_listener();
        class_name=findViewById(R.id.add_class_text);
        add_class=findViewById(R.id.add_class);
        recyclerView=findViewById(R.id.recycler_view);

        RecyclerView.LayoutManager layoutManager=new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(layoutManager);

        FirebaseFirestoreSettings settings = new FirebaseFirestoreSettings.Builder()
                .setPersistenceEnabled(false)
                .build();
        db.setFirestoreSettings(settings);

       // Log.d(TAG, "onCreate: "+classes.get(0));


        for(int i=0;i < classes.size();i++){
            data.add(new MyData(classes.get(i)));
        }


         adapter=new recycler_adaptor(data);
        recyclerView.setAdapter(adapter);


         add_class.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View view) {
                 String clas=class_name.getText().toString();
                 common_class.check_user("Subject",clas,name, new common_class.Check_callback() {
                             @Override
                             public void check(boolean found, DocumentSnapshot documentSnapshot) {
                                 if(found && classes.contains(clas)){
                                     Toast.makeText(Student.this,"Already in Class",Toast.LENGTH_LONG).show();
                                 }
                                 else if(found && !classes.contains(clas)){
                                     add_class(branch, name, clas, new User_callback() {
                                         @Override
                                         public void added_class(boolean added) {
                                             if(added) {
                                                 update_data(branch,name);
                                                 Log.d("==========>", "added_class: ");

                                             }
                                         }

                                         @Override
                                         public void students(boolean success, ArrayList<String> students) {

                                         }
                                     });
                                 }
                                 else {Toast.makeText(Student.this,"No class found",Toast.LENGTH_LONG).show();}
                             }
                         });
                 }
         });



    }


    public void add_class(String branch,String user,String class_name,User_callback user_callback){
        classes.add(class_name);

        db.collection(branch)
                .document(user)
                .update("classes",classes)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        user_callback.added_class(true);
                        Toast.makeText(Student.this, "added successfully",Toast.LENGTH_LONG).show();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        user_callback.added_class(false);
                    }
                });

        get_students("Subject", user, class_name, new User_callback() {
            @Override
            public void added_class(boolean added) {

            }

            @Override
            public void students(boolean success, ArrayList<String> studen) {
                if(success){
                    ArrayList<String> stud=new ArrayList<>();
                    if(!studen.isEmpty()){stud.addAll(studen);}
                    stud.add(user);
                db.collection("Subject")
                        .document(class_name)
                        .update("Student",stud)
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                user_callback.added_class(true);
                                Toast.makeText(Student.this, "added successfully",Toast.LENGTH_LONG).show();
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                user_callback.added_class(false);
                            }
                        });
            }
            }
        });







    }
    interface User_callback{
        void added_class(boolean added);
        void students(boolean success,ArrayList<String> students);


    }

    public void get_students(String branch,String user,String class_name,User_callback user_callback){
        db.collection(branch)
                .document(class_name)
                .get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        Log.d(TAG, "onSuccess:======> "+documentSnapshot.get("Student"));
                        user_callback.students(true, (ArrayList<String>) documentSnapshot.get("Student"));
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        user_callback.students(false,null);
                    }
                });



    }
    public class card_view_click_listener implements View.OnClickListener{

        @Override
        public void onClick(View view) {
            RelativeLayout relativeLayout= (RelativeLayout)view.getParent();
            int position =recyclerView.getChildPosition(relativeLayout);
            Log.d(TAG, "onClick: "+position+data.get(position).get_subject());
            get_data_and_update_recycler_view(data.get(position).get_subject());


        }
    }

    public void update_data(String branch,String user){
        get_classes(branch, user, new Classes_callback() {
                    @Override
                    public void classes(boolean success, ArrayList<String> classe) {
                        if(success){classes=classe;
                              ArrayList<MyData> datas=new ArrayList<>();
                            for(int i=0;i < classes.size();i++){
                                datas.add(new MyData(classes.get(i)));
                            }
                           adapter=new recycler_adaptor(datas);
                            recyclerView.setAdapter(adapter);
                        }
                    }
                }
        );


    }



    public void get_classes(String branch,String user,Classes_callback user_callback){
        db.collection(branch)
                .document(user)
                .get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        user_callback.classes(true, (ArrayList<String>) documentSnapshot.get("classes"));
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        user_callback.classes(false,null);
                    }
                });



    }
    interface Classes_callback{
        void classes(boolean success,ArrayList<String> classe);
    }
    public  void get_data_and_update_recycler_view(String class_name){
        ArrayList<String> c=new ArrayList<>();
        c.add("social");
        c.add("fdsfdsf");
        ArrayList<MyData> data=new ArrayList<>();

        for(int i=0;i < c.size();i++){
            data.add(new MyData(c.get(i)));
        }

        adapter=new recycler_adaptor(data);
        recyclerView.setAdapter(adapter);


    }



}
