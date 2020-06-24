package com.EX.examcreator.Upload_Pdf;


import android.app.DownloadManager;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.provider.OpenableColumns;
import android.text.TextUtils;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

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
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static android.os.Environment.DIRECTORY_DOWNLOADS;

public class Upload_Pdf_Activity extends AppCompatActivity {
    EditText editTextPDfName;
    Button buttonUPLOAd;

    StorageReference storageReference;
    DatabaseReference databaseReference;

    ListView listViewPdf;
    List<UploadPdf> uploadPdfList;
    final FirebaseAuth firebaseAuth=FirebaseAuth.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload__pdf_);

        editTextPDfName=(EditText) findViewById(R.id.editTextPDF);
        buttonUPLOAd=(Button) findViewById(R.id.buttonUploadPDF);

        storageReference= FirebaseStorage.getInstance().getReference();
        databaseReference= FirebaseDatabase.getInstance().getReference("examAndroid");

        buttonUPLOAd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name=editTextPDfName.getText().toString().trim();
                if (!TextUtils.isEmpty(name)) {
                    selectPDfFile();
                }else {
                    Toast.makeText(Upload_Pdf_Activity.this, "you Should Enter Name", Toast.LENGTH_SHORT).show();
                }

//        if (ContextCompat.checkSelfPermission(Pdf_Activity.this, Manifest.permission.READ_EXTERNAL_STORAGE)== PackageManager.PERMISSION_GRANTED){
//            selectPDfFile();
//        }else {
//            ActivityCompat.requestPermissions(Pdf_Activity.this,new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},9);
//        }



            }


        });
        /*----------------------------------------------------------------------------------------------*/
        listViewPdf=findViewById(R.id.myListViewPDF);
        uploadPdfList=new ArrayList<>();


        viewAllFiles();

        listViewPdf.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                final UploadPdf uploadPdf=uploadPdfList.get(position);

                StorageReference storageReference=FirebaseStorage.getInstance()
                        .getReference().child("exams/"+uploadPdf.getId());


                storageReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        String uri2=uri.toString();
                        downlood(Upload_Pdf_Activity.this,uploadPdf.getId(),".pdf", DIRECTORY_DOWNLOADS,uri2);
                    }


                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(Upload_Pdf_Activity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });

                // Open item in Adobe Pdf App
              /*  Intent intent=new Intent(Intent.ACTION_VIEW);
               intent.setData(Uri.parse(uploadPdf.getUrl()));
               intent.setDataAndType(Uri.parse(uploadPdf.getUrl()), "application/pdf");
                    startActivity(intent);

               */

            }
        });


        /*-------------------Delete item When Long click on Item---------------------------------------*/
        listViewPdf.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {

                AlertDialog diaBox = delFav(position);
                diaBox.show();

                return true;
            }
        });



        /*----------------------------------------------------------------------------------------------*/

        /* enable back buutun */
        ActionBar actionBar = this.getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

    private void downlood(Context context, String filename, String fileExtention, String destinationDirectory, String uri) {
        DownloadManager downloadManager=(DownloadManager) context.getSystemService(Context.DOWNLOAD_SERVICE);
        Uri uri1=Uri.parse(uri);
        DownloadManager.Request request=new DownloadManager.Request(uri1);
        request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
        request.setDestinationInExternalFilesDir(context,destinationDirectory,filename+fileExtention);
        downloadManager.enqueue(request);
    }

    private AlertDialog delFav(final int position) {
        final AlertDialog myQuittingDialogBox = new AlertDialog.Builder(Upload_Pdf_Activity.this)
                .setTitle("Delete")
                .setMessage("Do you want to Delete this Pdf File from Database ?\n"+
                        "you can't return back!!")
                .setIcon(R.drawable.ic_delete_black_24dp)
                .setPositiveButton("Delete", new DialogInterface.OnClickListener() {

                    public void onClick(DialogInterface dialog, int whichButton) {
                        UploadPdf uploadPdf=uploadPdfList.get(position);
                        DatabaseReference reference=FirebaseDatabase.getInstance().getReference("examAndroid").child(uploadPdf.getId());
                        reference.removeValue();

                        StorageReference storage=FirebaseStorage.getInstance().getReference().child("exams/"+uploadPdf.getId());
                        storage.delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                Toast.makeText(Upload_Pdf_Activity.this, "Delete Successfully", Toast.LENGTH_SHORT).show();
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(Upload_Pdf_Activity.this, "Failed Delete", Toast.LENGTH_SHORT).show();

                            }
                        });

                        //   Toast.makeText(Pdf_Activity.this, "Delete Successfully", Toast.LENGTH_SHORT).show();


                    }})
                .setNegativeButton("cancel", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {

                        dialog.dismiss();
                    }
                })
                .create();
        return myQuittingDialogBox;
    }




    private void viewAllFiles() {
        databaseReference= FirebaseDatabase.getInstance().getReference("examAndroid");
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                uploadPdfList.clear();

                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()){
                    UploadPdf uploadPdf=postSnapshot.getValue(UploadPdf.class);
                    if (uploadPdf.getDr_id().equals(firebaseAuth.getCurrentUser().getUid()) ) {
                        uploadPdfList.add(uploadPdf);
                    }
                }
                String[] uploads=new String[uploadPdfList.size()];
                for (int i=0;i<uploads.length;i++){

                        uploads[i] = uploadPdfList.get(i).getExam_Name();

                }
                ArrayAdapter<String> adapter=new ArrayAdapter<String>(getApplicationContext(),android.R.layout.simple_list_item_1,uploads){

                    @Override
                    public View getView(int position, View convertView, ViewGroup parent) {
                        View view=super.getView(position, convertView, parent);
                        TextView textView=view.findViewById(android.R.id.text1);
                        textView.setTextColor(Color.BLACK);

                        return view;
                    }
                };

                listViewPdf.setAdapter(adapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    /*----------------------------------------------------------------------------------------------*/


    private void selectPDfFile() {

        Intent intent=new Intent();
        intent.setType("application/pdf");
        intent.setAction(Intent.ACTION_GET_CONTENT);

        startActivityForResult(Intent.createChooser(intent,"Select PDF File"),86);
        //  startActivityForResult(Intent.createChooser(intent,"Open File"),1);

    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable final Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 86 && resultCode == RESULT_OK && data != null && data.getData() != null) {

            Cursor cursor=getContentResolver().query(data.getData(),null,null,null,null);
            int sizeindex=cursor.getColumnIndex(OpenableColumns.SIZE);
            cursor.moveToFirst();
            long fileSize=cursor.getLong(sizeindex);
            if ((fileSize/(1000*1000)) <= 5){
                uploadPdfFile(data.getData());
            }else {

                Toast.makeText(Upload_Pdf_Activity.this, "The PDF is Large 5 MB", Toast.LENGTH_SHORT).show();

            }


        }

    }

    private void uploadPdfFile(final Uri data) {

        String tag="xcxc";
        SimpleDateFormat currentDate = new SimpleDateFormat("dd/MM/yyyy");
        Date todayDate = new Date();
        final String thisDate = currentDate.format(todayDate);
        Log.d(tag,thisDate);

        /*-----------------------------------------------------------------------------*/

        final ProgressDialog progressDialog=new ProgressDialog(this);
//        progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
//        progressDialog.setProgress(0);
        progressDialog.setTitle("Uploading...");
        progressDialog.show();

        final String id=databaseReference.push().getKey();
        //  final FirebaseAuth firebaseAuth=FirebaseAuth.getInstance();
        final StorageReference reference=storageReference.child("exams/"+id);
       // final StorageReference reference=storageReference.child("exams/"+editTextPDfName.getText().toString());

        //StorageReference reference=storageReference.child("uploads/"+System.currentTimeMillis()+".pdf");

//        File file=new File(data.getPath());
//        Log.v("LOG", "FILE SIZE "+file.length());


        final UploadTask uploadTask = reference.putFile(data);

        uploadTask.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                String uriTask=taskSnapshot.getStorage().getDownloadUrl().toString();
                long fileSize=taskSnapshot.getMetadata().getSizeBytes();
                String size=String.valueOf(fileSize);
                Log.d("vcv", size);
                //  String id=databaseReference.push().getKey();
                // while (!uriTask.isComplete());
                //   Uri uri=uriTask.getResult();
              //  if (fileSize <= 5242880) {
                    //    progressDialog.show();
                    UploadPdf uploadPdf = new UploadPdf(id, editTextPDfName.getText().toString()+"\n"+thisDate, uriTask,firebaseAuth.getCurrentUser().getUid());

                    databaseReference.child(id).setValue(uploadPdf);
                    Toast.makeText(Upload_Pdf_Activity.this, "Uploaded Successfully", Toast.LENGTH_SHORT).show();
                    editTextPDfName.setText("");

                    progressDialog.dismiss();
//                }else{
//                    reference.delete();
//                    progressDialog.dismiss();
//                }




            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {


            }
        }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onProgress(@NonNull UploadTask.TaskSnapshot taskSnapshot) {
                double progress=(100.0*taskSnapshot.getBytesTransferred())/taskSnapshot.getTotalByteCount();
                progressDialog.setMessage("Uploaded: "+(int)progress+"%");

            }
        });
    }

    /*-----------------------------------------------------------------------------------------------*/

}