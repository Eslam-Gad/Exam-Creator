package com.EX.examcreator.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.preference.PreferenceManager;

import android.app.AlertDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import com.EX.examcreator.Models.ImageProfile;
import com.EX.examcreator.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.util.ArrayList;

import dmax.dialog.SpotsDialog;

public class ProfileActivity extends AppCompatActivity {

    ImageView imgUniversity,imgFaculty;
    FirebaseAuth firebaseAuth;
    StorageReference storageUniv,storageFaculty;
    DatabaseReference referenceUniv,referenceFaculty;

    Uri imageUri;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);


        /* enable back buutun */
        ActionBar actionBar = this.getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        /*------------------------------------------*/

        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);

        String university = sharedPreferences.getString("univ_key", null);
        final String faculty = sharedPreferences.getString("faculty_key", null);
        String department = sharedPreferences.getString("dept_key", null);
        String dr_name = sharedPreferences.getString("signature_key", null);
        TextView txt_University=findViewById(R.id.universityidprofile);
        txt_University.setText("University "+university);

        TextView txt_Faculty=findViewById(R.id.facultyidprofile);
        txt_Faculty.setText("Faculty of "+faculty);

        TextView txt_Depart=findViewById(R.id.departidprofile);
        txt_Depart.setText("Department: "+department);

        TextView txt_DrName=findViewById(R.id.doctoridprofile);
        txt_DrName.setText(dr_name);
        /*------------------------------------------*/
        imgUniversity=(ImageView)findViewById(R.id.img_university);
        imgFaculty=(ImageView)findViewById(R.id.img_faculty);

        firebaseAuth= FirebaseAuth.getInstance();
        storageUniv= FirebaseStorage.getInstance().getReference();
        referenceUniv= FirebaseDatabase.getInstance().getReference("ImageUniversity");

        storageFaculty= FirebaseStorage.getInstance().getReference();
        referenceFaculty= FirebaseDatabase.getInstance().getReference("ImageFaculty");

        imgUniversity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI);
                startActivityForResult(intent,100);

            }
        });

        imgFaculty.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI);
                startActivityForResult(intent,150);

            }
        });
        /*----------------------------read Image University from Firebase---------------------------------------------------*/
        final ArrayList<ImageProfile> list;
        DatabaseReference referenceUniversity= FirebaseDatabase.getInstance().getReference().child("ImageUniversity");
        list=new ArrayList<ImageProfile>();
        referenceUniversity.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                list.clear();
                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()){
                    ImageProfile imageProfile=postSnapshot.getValue(ImageProfile.class);
                    if (imageProfile.getIdDr_Profile().equals(firebaseAuth.getCurrentUser().getUid()) ) {
                        list.add(imageProfile);

                    }
                    for (int i=0;i<list.size();i++) {
                        Picasso.get().load(list.get(i).getUrl()).into(imgUniversity);
                    }
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(ProfileActivity.this, databaseError.getMessage(), Toast.LENGTH_SHORT).show();

            }
        });
        /*---------------------read Image faculty from Firebase-----------------------*/
        final ArrayList<ImageProfile> list2;
        DatabaseReference referenceFaculty= FirebaseDatabase.getInstance().getReference().child("ImageFaculty");
        list2=new ArrayList<ImageProfile>();
        referenceFaculty.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                list2.clear();
                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()){
                    ImageProfile imageProfile2=postSnapshot.getValue(ImageProfile.class);
                    if (imageProfile2.getIdDr_Profile().equals(firebaseAuth.getCurrentUser().getUid()) ) {
                        list2.add(imageProfile2);

                    }
                    for (int i=0;i<list2.size();i++) {
                        Picasso.get().load(list2.get(i).getUrl()).into(imgFaculty);
                    }
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(ProfileActivity.this, databaseError.getMessage(), Toast.LENGTH_SHORT).show();


            }
        });
        /*-----------------------------------------------------------------------------*/



    }
    public void university(final Uri uriUniv) {
        final AlertDialog dialog = new SpotsDialog(ProfileActivity.this);
        dialog.setTitle("Upload Image");
        final String idUniv = referenceUniv.push().getKey();
        final StorageReference reference = storageUniv.child("ImageUniversity/" + firebaseAuth.getCurrentUser().getUid());
        final UploadTask uploadTask = reference.putFile(uriUniv);
        dialog.show();
        uploadTask.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {

            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                reference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        ImageProfile imageProfile = new ImageProfile(idUniv, firebaseAuth.getCurrentUser().getUid(), uri.toString());
                        referenceUniv.child(firebaseAuth.getCurrentUser().getUid()).setValue(imageProfile);
                        dialog.dismiss();
                        Toast.makeText(getApplicationContext(), "Save Successfully", Toast.LENGTH_SHORT).show();

                    }
                });

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                dialog.dismiss();
                Toast.makeText(ProfileActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();

            }
        });

    }


    public void faculty(final Uri uriFaculty) {
        final AlertDialog dialog = new SpotsDialog(ProfileActivity.this);
        dialog.setTitle("Upload Image");
        final String idfaculty=referenceFaculty.push().getKey();
        final StorageReference reference=storageFaculty.child("ImageFaculty/"+firebaseAuth.getCurrentUser().getUid());
        final UploadTask uploadTask = reference.putFile(uriFaculty);
        dialog.show();
        uploadTask.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                reference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        ImageProfile imageProfile = new ImageProfile(idfaculty, firebaseAuth.getCurrentUser().getUid(),uri.toString());
                        referenceFaculty.child(firebaseAuth.getCurrentUser().getUid()).setValue(imageProfile);
                        dialog.dismiss();
                        Toast.makeText(getApplicationContext(), "Save Successfully", Toast.LENGTH_SHORT).show();
                    }
                });

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                dialog.dismiss();
                Toast.makeText(ProfileActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode,  Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && requestCode == 100) {

            try {
                Bitmap bitmap= MediaStore.Images.Media.getBitmap(getContentResolver(),data.getData());
            //   imgUniversity.setImageBitmap(bitmap);
                imageUri=data.getData();
                university(imageUri);
            }catch (IOException e){
                e.printStackTrace();

            }
        }else if  (resultCode == RESULT_OK && requestCode == 150){
            try {
                Bitmap bitmap= MediaStore.Images.Media.getBitmap(getContentResolver(),data.getData());
                imageUri=data.getData();
                faculty(imageUri);
            }catch (IOException e){
                e.printStackTrace();

            }
        }

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
        }
        return super.onOptionsItemSelected(item);
    }
}
