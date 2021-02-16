package com.dash.quiz.Auth;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.dash.quiz.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreSettings;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class Login extends Fragment {
    private static final String TAG = "login process";
    public EditText username,password;
    public String user,pass,branch;
    Button login,create;
    RadioGroup radioGroup;
    RadioButton radioButton;
    ProgressBar progressBar;
    FirebaseFirestore db=FirebaseFirestore.getInstance();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root=inflater.inflate(R.layout.login_class,container,false);
        username=root.findViewById(R.id.username);
        password=root.findViewById(R.id.password);
        radioGroup=root.findViewById(R.id.branch_group);
        login=root.findViewById(R.id.login_button);
        create=root.findViewById(R.id.create_account);
        progressBar=root.findViewById(R.id.progress_login);
        progressBar.setVisibility(View.INVISIBLE);

        FirebaseFirestoreSettings settings = new FirebaseFirestoreSettings.Builder()
                .setPersistenceEnabled(false)
                .build();
        db.setFirestoreSettings(settings);

        username.setText("kumar");
        password.setText("a");
        create.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Fragment newFragment = new signup();
                FragmentTransaction transaction = getFragmentManager().beginTransaction();
                transaction.replace(R.id.frag,newFragment);
                transaction.addToBackStack(null);
                transaction.commit();

            }
        });

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                progressBar.setVisibility(View.VISIBLE);
                user=username.getText().toString();
                pass=password.getText().toString();
                call_methods("Staff",user,pass);
            }
        });


        return root;
    }
    private void call_methods(String bran,String username,String password)
    { if(check_branch()) {
        int id=radioGroup.getCheckedRadioButtonId();
        radioButton=radioGroup.findViewById(id);
        branch=radioButton.getText().toString().trim();
        if (check_null()) {
            check_user(branch, username, password, new Return_callback() {
                @Override
                public void location(boolean found, DocumentSnapshot documentSnapshot) {
                    if (found) {
                        ArrayList<String> arrayList=new ArrayList<>();
                        arrayList= (ArrayList<String>) documentSnapshot.get("classes");
                        Log.d(TAG, "location: " + found);
                        Log.d(TAG, "location: data"+documentSnapshot.get("classes"));
                        progressBar.setVisibility(View.INVISIBLE);
                        Intent intent = new Intent(getContext(), logged_interface.class);
                        intent.putExtra("name", documentSnapshot.get("name").toString());
                        intent.putExtra("joined", documentSnapshot.get("joined").toString());
                        intent.putExtra("branch",branch);
                        intent.putExtra("classes",arrayList);
                        startActivity(intent);
                    }
                }
            });
        }
    }
    }

    private boolean check_branch(){
        int id=radioGroup.getCheckedRadioButtonId();
        if(id >= 0) return true;
        else {
            Toast.makeText(getContext(),"select branch",Toast.LENGTH_LONG).show();
            return false;}
    }

    private boolean check_null(){
        if(user.isEmpty() | pass.isEmpty()){return false;}
        return true;
    }
    private void check_user(String branch,String data,String pass,Return_callback return_callback){

        DocumentReference docRef = db.collection(branch).document(data);
        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        Log.d(TAG, "DocumentSnapshot data: " + document.getData());
                        //.d(TAG, "DocumentSnapshot data: " + document.get("password"));
                        if(document.get("password").equals(pass))
                        return_callback.location(true,document);
                        else{
                            password.getText().clear();
                            password.setHint("enter correct password !");
                            password.setHintTextColor(Color.RED);
                            return_callback.location(false,null);
                        }

                    } else {
                        Log.d(TAG, "No such document");
                        username.getText().clear();
                        username.setHint("Username not found !");
                        username.setHintTextColor(Color.RED);
                        return_callback.location(false,null);
                    }
                } else {
                    Log.d(TAG, "get failed with ", task.getException());
                    return_callback.location(false,null);
                }

            }
        });


    }
    interface Return_callback{
        public void location(boolean found,DocumentSnapshot documentSnapshot);


    }


}
