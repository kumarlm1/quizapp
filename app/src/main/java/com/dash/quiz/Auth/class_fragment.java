package com.dash.quiz.Auth;

import android.app.PendingIntent;
import android.content.Intent;
import android.content.IntentSender;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.NumberPicker;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.dash.quiz.MainActivity;
import com.dash.quiz.R;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.credentials.Credential;
import com.google.android.gms.auth.api.credentials.CredentialPickerConfig;
import com.google.android.gms.auth.api.credentials.Credentials;
import com.google.android.gms.auth.api.credentials.CredentialsClient;
import com.google.android.gms.auth.api.credentials.HintRequest;
import com.google.android.gms.auth.api.credentials.IdentityProviders;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class class_fragment extends AppCompatActivity {
    private static final String TAG = "sscscsacss";
    ImageButton img;
    Button btn,choose,upload;
    TextView txt;
    EditText file;
    int i=0,j=0;
    LinearLayout ll;
    String topic="topic",subtopic="subtopic",name,paths;
    ArrayList<Integer> edittext=new ArrayList<>();
    ArrayList<Integer> textview=new ArrayList<>();
    FirebaseDatabase database=FirebaseDatabase.getInstance();
    // Uri indicates, where the image will be picked from
    private Uri filePath;
    // request code
    private final int PICK_IMAGE_REQUEST = 22;
    FirebaseStorage storage=FirebaseStorage.getInstance();
    StorageReference reference=storage.getReference();
    Integer KEY=100;
    FirebaseDatabase firebaseDatabase=FirebaseDatabase.getInstance();
    Spinner spinner,spinner_txt;


    @Nullable
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
     setContentView(R.layout.class_fragment);

        name=getIntent().getStringExtra("name");
        img =findViewById(R.id.addl);
        txt=findViewById(R.id.class_name);
        txt.setText(name);
        btn=findViewById(R.id.show);
        choose=findViewById(R.id.choose);
        upload=findViewById(R.id.upload);
        file=findViewById(R.id.filename);
        spinner=findViewById(R.id.spinner);
        spinner_txt=findViewById(R.id.spinner_txt);
        String[] courses={"dfbbd","dfbf"};

        set_data(new List_data() {
            @Override
            public void getted_data(String[] data) {
                ArrayAdapter adapter=new ArrayAdapter(class_fragment.this, android.R.layout.simple_spinner_item,data);
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

                spinner.setAdapter(adapter);
            }
        });






        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                Log.d(TAG, "onItemSelected: "+position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });



        choose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SelectImage();
            }
        });
        upload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                 uploadImage();

            }
        });








        img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ll =findViewById(R.id.dynamic_linear);
                EditText ed = new EditText(class_fragment.this);
                TextView tv = new TextView(class_fragment.this);
                LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.WRAP_CONTENT);
                ed.setLayoutParams(lp);
                tv.setLayoutParams(lp);
                if(i < 1) {
                    tv.setText(topic);
                }
                else
                    tv.setText(subtopic);

                ed.setEms(20);
                ed.setId(1+i);
                edittext.add(1+i);
                tv.setId(100+i);
                textview.add(100+i);

                ll.addView(tv);
                ll.addView(ed);
                i++;
            }
        });
        btn.setOnClickListener(new View.OnClickListener() {
            private static final String TAG ="uibusbxusaxui" ;

            @Override
            public void onClick(View view) {
                String data="";
                for(Integer s : edittext){
                     EditText ed=ll.findViewById(s);
                    Log.d(TAG, "onClick: added");
                    if(ed.getText().toString().isEmpty()){
                        Toast.makeText(getApplicationContext(),"Enter correct path ",Toast.LENGTH_LONG).show();
                        break;
                    }

                     data=data+ed.getText().toString()+"/";
                }
                //txt.setText(data);
              //  View ed=ll.findViewById(j+1);
               // ll.removeView(ed);
               // j++;
            }
        });
    }
    private void SelectImage()
    {

        // Defining Implicit Intent to mobile gallery
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(
                Intent.createChooser(
                        intent,
                        "Select Image from here..."),
                PICK_IMAGE_REQUEST);
    }
    @Override
    protected void onActivityResult(int requestCode,
                                    int resultCode,
                                    Intent data)
    {

        super.onActivityResult(requestCode,
                resultCode,
                data);
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK ) {
            // Get the Uri of data
           filePath = data.getData();
        }
        else{
            Log.d(TAG, "onActivityResult: ======================>");
        }
    }
    private void uploadImage()
    {
        String data="";
        for(Integer s : edittext){
            EditText ed=ll.findViewById(s);
            Log.d(TAG, "onClick: added");
            if(ed.getText().toString().isEmpty()){
                Toast.makeText(getApplicationContext(),"Enter correct path ",Toast.LENGTH_LONG).show();
                break;
            }

            data=data+ed.getText().toString()+"/";
        }
        paths=name+"/"+data+"/";


        txt.setText(paths);
        insertdata(paths,"fsfsff");




        String filename=file.getText().toString();
        if(filename.isEmpty()){
            Toast.makeText(getApplicationContext(),"enter file name",Toast.LENGTH_LONG).show();
        }
        if (filePath != null && !filename.isEmpty())  {
            Log.d(TAG, "uploadImage: "+filePath);
            StorageReference ref = reference.child(
                            "images/"
                                    +filename.toString());

            DatabaseReference doc=database.getReference(name+"/"+data+"/link");
            doc.setValue(filename+".txt");

            // adding listeners on upload
            // or failure of image
            ref.putFile(filePath)
                    .addOnSuccessListener(
                            new OnSuccessListener<UploadTask.TaskSnapshot>() {

                                @Override
                                public void onSuccess(
                                        UploadTask.TaskSnapshot taskSnapshot)
                                {

                                    // Image uploaded successfully
                                    // Dismiss dialo
                                    Toast
                                            .makeText(class_fragment.this,
                                                    "Image Uploaded!!",
                                                    Toast.LENGTH_SHORT)
                                            .show();
                                }
                            })

                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e)
                        {

                            // Error, Image not uploaded

                            Toast
                                    .makeText(class_fragment.this,
                                            "Failed " + e.getMessage(),
                                            Toast.LENGTH_SHORT)
                                    .show();
                        }
                    });

        }
    }
    public void insertdata(String path,String value){
        DatabaseReference def=firebaseDatabase.getReference(path);
        def.setValue("link:"+value);

    }
    public void set_data(List_data list){
        ArrayList<String> title=new ArrayList<>();
        DatabaseReference databaseReference=firebaseDatabase.getReference(name);

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot){
                for (DataSnapshot child: snapshot.getChildren()) {
                    if(child.hasChildren()){
                        title.add(child.getKey());
                        System.out.println(child.getKey());
                        set_data1(child.getKey());

                    }
                }
                String[] list_data=new String[title.size()];
                list_data=title.toArray(list_data);
                txt.setText("");
                list.getted_data(list_data);

            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


    }
    public void set_data1(String path){
        ArrayList<String> title=new ArrayList<>();
        DatabaseReference databaseReference=firebaseDatabase.getReference(name+"/"+path);

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot){
                for (DataSnapshot child: snapshot.getChildren()) {
                        title.add(child.getKey());
                        System.out.println("child"+child.getKey());

                }
                String[] list_data=new String[title.size()];
                list_data=title.toArray(list_data);
                txt.setText("");

            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


    }
    interface List_data{
         void getted_data(String[] data);
    }
}
