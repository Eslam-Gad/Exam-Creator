package com.EX.examcreator.Activities;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.EX.examcreator.Adapters.coursesControllAdapter;
import com.EX.examcreator.Models.CousesModel;
import com.EX.examcreator.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import java.util.ArrayList;
import java.util.List;

public class CoursesControll extends AppCompatActivity implements coursesControllAdapter.CallbackHandular {

    ProgressBar progressBar;
    private RecyclerView recyclerView;
    FrameLayout root;
    private FloatingActionButton floatingActionButton;

    TextView emptymark;
    TextView connmark;

    List<CousesModel> courcesTitle;

    DatabaseReference databaseCources;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_courses_controll);

        floatingActionButton = findViewById(R.id.addSubject);

        progressBar = (ProgressBar) findViewById(R.id.pb_loading_indicator);
        emptymark = (TextView) findViewById(R.id.emptyMark);

        connmark = (TextView)findViewById(R.id.connectMark);
        root = (FrameLayout) findViewById(R.id.root);
        databaseCources = FirebaseDatabase.getInstance().getReference("cources");

        recyclerView = (RecyclerView) findViewById(R.id.rv);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        courcesTitle = new ArrayList<>();

        /* enable back buutun */
        ActionBar actionBar = this.getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
    }

    @Override
    protected void onStart() {
        super.onStart();


        if(checkConncectivity()) {

            databaseCources.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                    courcesTitle.clear();

                    for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                        CousesModel cousesModel = dataSnapshot1.getValue(CousesModel.class);
                        String dr_id = FirebaseAuth.getInstance().getCurrentUser().getUid();

                        if (dr_id.equals(cousesModel.getDr_id()))
                            courcesTitle.add(cousesModel);
                    }

                    coursesControllAdapter coursesControllAdapter = new coursesControllAdapter(courcesTitle, CoursesControll.this);

                    recyclerView.setAdapter(coursesControllAdapter);

                    progressBar.setVisibility(View.GONE);
                    connmark.setVisibility(View.GONE);

                    if (courcesTitle.isEmpty()) {
                        emptymark.setVisibility(View.VISIBLE);
                    }

                }


                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
        }else {
            progressBar.setVisibility(View.GONE);
        }
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

    @SuppressLint("RestrictedApi")
    public boolean checkConncectivity(){
        ConnectivityManager ConnectionManager=(ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo=ConnectionManager.getActiveNetworkInfo();
        if(networkInfo != null && networkInfo.isConnected()==true ){
            return true;
        }else
        {
            connmark.setVisibility(View.VISIBLE);
            floatingActionButton.setVisibility(View.INVISIBLE);
            return false;
        }
    }

    public void addNewSubject(View view) {

        AlertDialog.Builder builder = new AlertDialog.Builder(CoursesControll.this);
        View mview = getLayoutInflater().inflate(R.layout.course_dialog, null);
        final EditText sub_name = (EditText) mview.findViewById(R.id.sub_name);
        final EditText sub_code = (EditText) mview.findViewById(R.id.sub_code);

        final Button save = (Button) mview.findViewById(R.id.save);

        builder.setView(mview);

        final AlertDialog alertDialog = builder.create();
        alertDialog.show();

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                    addNewCource(sub_name, sub_code ,alertDialog );
                    emptymark.setVisibility(View.GONE);

            }
        });
    }

    private void addNewCource(EditText sub_name, EditText sub_code ,final AlertDialog alertDialog) {
        if (!sub_name.getText().toString().isEmpty()&&! sub_code.getText().toString().isEmpty()&&checkConncectivity()) {
            alertDialog.cancel();
            final ProgressDialog dialog = ProgressDialog.show(CoursesControll.this, "",
                    "Loading. Please wait...", true);
            String id = databaseCources.push().getKey();
            String dr_id = FirebaseAuth.getInstance().getCurrentUser().getUid();

            CousesModel cousesModel = new CousesModel(sub_name.getText().toString(), dr_id, id , sub_code.getText().toString());

            databaseCources.child(id).setValue(cousesModel)

                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                dialog.dismiss();
                            } else {
                                Snackbar.make(root, task.getException().getMessage(), Snackbar.LENGTH_LONG).show();
                            }
                        }
                    });
        } else {
            alertDialog.cancel();
            Toast.makeText(this, "pls: All fields required", Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void addChHandular(int pos) {
        Intent intent = new Intent(CoursesControll.this , AddChapter.class);
        intent.putExtra("course_id" , courcesTitle.get(pos).getId());
        intent.putExtra("course_name" , courcesTitle.get(pos).getCoursesName());
        startActivity(intent);
    }

    @Override
    public void editHandular(final int pos) {

        AlertDialog.Builder builder = new AlertDialog.Builder(CoursesControll.this);
        View mview = getLayoutInflater().inflate(R.layout.course_dialog, null);
        final EditText sub_name = (EditText) mview.findViewById(R.id.sub_name);
        final EditText sub_code = (EditText) mview.findViewById(R.id.sub_code);
        sub_name.setText(courcesTitle.get(pos).getCoursesName());
        sub_code.setText(courcesTitle.get(pos).getCourse_code());
        final TextView textView = (TextView) mview.findViewById(R.id.name);
        textView.setText("Edit Course Name");

        final Button save = (Button) mview.findViewById(R.id.save);

        builder.setView(mview);

        final AlertDialog alertDialog = builder.create();
        alertDialog.show();

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (!sub_name.getText().toString().isEmpty()&&!sub_code.getText().toString().isEmpty()) {
                    alertDialog.cancel();
                    final ProgressDialog dialog = ProgressDialog.show(CoursesControll.this, "",
                            "Loading. Please wait...", true);

                    String name = sub_name.getText().toString().trim();
                    String dr_id = courcesTitle.get(pos).getDr_id();
                    String id = courcesTitle.get(pos).getId();
                    String code =sub_code.getText().toString().trim();

                    if(updateCourseName(name, id, dr_id , code))
                    dialog.dismiss();
                    else{
                        Toast.makeText(CoursesControll.this, "error", Toast.LENGTH_LONG).show();
                    }

                } else {
                    alertDialog.cancel();
                    Toast.makeText(CoursesControll.this, "pls: All fields required", Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    @Override
    public void delHandular(int pos) {
        AlertDialog diaBox = AskOption(pos);
        diaBox.show();
    }

    private boolean updateCourseName(String name, String id, String dr_id , String code) {

            DatabaseReference databaseReference1 = FirebaseDatabase.getInstance().getReference("cources").child(id);

            CousesModel cousesModel = new CousesModel(name, dr_id, id , code);

            databaseReference1.setValue(cousesModel);


        return true;
    }

    private AlertDialog AskOption(final int pos)
    {
        final AlertDialog myQuittingDialogBox = new AlertDialog.Builder(this)
                // set message, title, and icon
                .setTitle("Delete")
                .setMessage("Do you want to Delete " +courcesTitle.get(pos).getCoursesName()+ " course ?" +
                        "\nyou can't return back"+
                        "\nwill delete all related chapters and questions !")
                .setIcon(R.drawable.ic_delete_black_24dp)
                .setPositiveButton("Delete", new DialogInterface.OnClickListener() {

                    public void onClick(DialogInterface dialog, int whichButton) {

                            //your deleting code
                            dialog.dismiss();
                            final ProgressDialog deldialog = ProgressDialog.show(CoursesControll.this, "",
                                    "Loading. Please wait...", true);

                            String id = courcesTitle.get(pos).getId();

                            DatabaseReference databaseReferencedelete = FirebaseDatabase.getInstance().getReference("cources").child(id);
                            DatabaseReference chappterReferencedelete = FirebaseDatabase.getInstance().getReference("chapters").child(id);
                            DatabaseReference questionReferencedelete = FirebaseDatabase.getInstance().getReference("questions").child(id);

                            chappterReferencedelete.removeValue();
                            questionReferencedelete.removeValue();
                            databaseReferencedelete.removeValue()
                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if (task.isSuccessful()) {
                                                deldialog.dismiss();
                                            } else {
                                                Snackbar.make(root, task.getException().getMessage(), Snackbar.LENGTH_LONG).show();
                                            }

                                        }
                                    });
                        }
                })
                .setNegativeButton("cancel", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {

                        dialog.dismiss();
                    }
                })
                .create();

        return myQuittingDialogBox;
    }
}

