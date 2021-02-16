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
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
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
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.ArrayList;
import java.util.UUID;

public class class_fragment extends AppCompatActivity {
    private static final String TAG = "sscscsacss";
    ImageButton img;
    Button btn,choose,upload;
    TextView txt;
    EditText file;
    int i=0,j=0;
    LinearLayout ll;
    String topic="topic",subtopic="subtopic",name;
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

        choose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SelectImage();
            }
        });
        upload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
              // uploadImage();
                HintRequest hintRequest=new HintRequest.Builder()
                        .setHintPickerConfig(new CredentialPickerConfig.Builder().build())
                        .setPhoneNumberIdentifierSupported(true)
                        .build();

                GoogleApiClient apiClient=new GoogleApiClient.Builder(getApplicationContext())
                        .addApi(Auth.CREDENTIALS_API)
                        .build();

                PendingIntent intent1= Credentials.getClient(getApplicationContext()).getHintPickerIntent(hintRequest);

                try {
                    startIntentSenderForResult(intent1.getIntentSender(),KEY,null,0,0,0);
                } catch (IntentSender.SendIntentException e) {
                    e.printStackTrace();
                }
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
                DatabaseReference doc=database.getReference(name+"/"+data+"/link");
                doc.setValue("ibsiubcsiubcsiuabciusb");
                txt.setText(data);
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
        if (requestCode == KEY && resultCode == RESULT_OK ) {
              Credential credential=data.getParcelableExtra(Credential.EXTRA_KEY);
            Toast.makeText(getApplicationContext(),credential.getId().toString(),Toast.LENGTH_LONG).show();
            // Get the Uri of data
           // filePath = data.getData();
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

        txt.setText(data);




        String filename=file.getText().toString();
        if(filename.isEmpty()){
            Toast.makeText(getApplicationContext(),"",Toast.LENGTH_LONG).show();
        }
        if (filePath != null && !filename.isEmpty())  {

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
}
