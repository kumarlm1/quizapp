package com.dash.quiz.Auth;

import android.graphics.Color;
import android.util.Log;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class common_class {

    static void check_user(String branch, String data, String pass, Check_callback check_callback) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        DocumentReference docRef = db.collection(branch).document(data);
        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            private static final String TAG = "common_class";

            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        Log.d(TAG, "DocumentSnapshot data: " + document.getData());
                        check_callback.check(true, document);

                    } else {
                        Log.d(TAG, "No such document");
                        check_callback.check(false, null);
                    }
                } else {
                    Log.d(TAG, "get failed with ", task.getException());
                    check_callback.check(false, null);
                }

            }
        });
    }

    static interface Check_callback {
        public void check(boolean found, DocumentSnapshot documentSnapshot);


    }

    static void get_staff(String class_name,Check_callback check_callback) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        DocumentReference docRef = db.collection("Subject").document(class_name);
        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            private static final String TAG = "common_class";

            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        Log.d(TAG, "DocumentSnapshot data: " + document.getData());
                        check_callback.check(true, document);

                    } else {
                        Log.d(TAG, "No such document");
                        check_callback.check(false, null);
                    }
                } else {
                    Log.d(TAG, "get failed with ", task.getException());
                    check_callback.check(false, null);
                }

            }
        });


    }
}

















