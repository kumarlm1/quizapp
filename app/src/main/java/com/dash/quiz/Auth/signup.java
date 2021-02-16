package com.dash.quiz.Auth;

import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.dash.quiz.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.Timestamp;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public  class signup extends Fragment {
    private static final String TAG = "login process";
    public EditText username,password,repassword;
    public String user,pass,repass,branch,data;
    Button btn;
    public int uploaded;
    View root;
    public boolean isfound = false;
    RadioGroup radioGroup;
    RadioButton radioButton;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        //if(container != null){container.removeAllViews();}
       root =inflater.inflate(R.layout.signup,container,false);
        username=root.findViewById(R.id.username);
        password=root.findViewById(R.id.password);
        repassword=root.findViewById(R.id.password_repeat);
        radioGroup=root.findViewById(R.id.branch_group);
        btn=root.findViewById(R.id.signup_button);

        username.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if(b == false) {
                    String user_name = username.getText().toString();
                    Log.d(TAG, "afterTextChanged: =========>");
                    if (!user_name.isEmpty() && check_branch()) {
                        int id=radioGroup.getCheckedRadioButtonId();
                        radioButton=radioGroup.findViewById(id);
                        branch=radioButton.getText().toString().trim();
                       data=user_name;
                        check_user(branch,user_name,new Return_callback() {
                            @Override
                            public void location(boolean found) {
                                Log.d(TAG, "location: " + found);
                            }
                        });
                    }


                }
             }
         });

         btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                user = username.getText().toString();
                pass = password.getText().toString();
                repass = repassword.getText().toString();
                if(pass.equals(repass) && !pass.isEmpty() && !user.isEmpty() && check_branch()) {
                    int id=radioGroup.getCheckedRadioButtonId();
                    radioButton=radioGroup.findViewById(id);
                    branch=radioButton.getText().toString().trim();
                    data=user;
                    check_user(branch,user,new Return_callback() {
                        @Override
                        public void location(boolean found) {
                            if(!found)add_user(branch, user, convert_map(user, pass), new User_callback() {
                                @Override
                                public void added_user(boolean added) {
                                     if(added){
                                         Fragment fragment=new Login();
                                         FragmentTransaction fragmentTransaction=getFragmentManager().beginTransaction();
                                         fragmentTransaction.replace(R.id.frag,fragment);
                                         fragmentTransaction.addToBackStack(null);
                                         fragmentTransaction.commit();
                                     }
                                }
                            });
                        }
                    });

                }
                else{
                    Log.d(TAG, "onClick: enter correct password in both field");
                }
            }
        });

         return root;
    }

    private boolean check_branch(){
        int id=radioGroup.getCheckedRadioButtonId();
        if(id >= 0) return true;
        else {
            Toast.makeText(getContext(),"select branch",Toast.LENGTH_LONG).show();
            return false;}
    }


    public void check_user(String branch,String data,Return_callback return_callback){
        FirebaseFirestore db=FirebaseFirestore.getInstance();
        DocumentReference docRef = db.collection(branch).document(data);
        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        Log.d(TAG, "DocumentSnapshot data: " + document.getData());
                        username.getText().clear();
                        username.setHint("Username already found !");
                        username.setHintTextColor(Color.RED);
                        return_callback.location(true);

                    } else {
                        Log.d(TAG, "No such document");
                        return_callback.location(false);
                    }
                } else {
                    Log.d(TAG, "get failed with ", task.getException());
                      return_callback.location(false);
                }

            }
        });


    }

    private Map<String,Object> convert_map(String user,String pass){

        Map<String, Object> docData = new HashMap<>();
        docData.put("name",user);
        docData.put("password",pass);
        docData.put("joined", new Timestamp(new Date()));
        docData.put("classes",new ArrayList<String>());
        return docData;
    }


    private void add_user(String branch,String user,Map<String,Object> data,User_callback user_callback){
        FirebaseFirestore db=FirebaseFirestore.getInstance();
        db.collection(branch)
                .document(user)
                .set(data)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                       user_callback.added_user(true);
                        Toast.makeText(getContext(), "added successfully",Toast.LENGTH_LONG).show();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        user_callback.added_user(false);
                    }
                });



    }

    interface Return_callback {
            void location(boolean found);

    }
    interface User_callback{
         void added_user(boolean added);
    }


}
