package com.EX.examcreator.Activities;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.EX.examcreator.Adapters.chapterAdapter;
import com.EX.examcreator.Models.Chapter;
import com.EX.examcreator.Models.ChapterModel;
import com.EX.examcreator.Models.CousesModel;
import com.EX.examcreator.Models.Subject;
import com.EX.examcreator.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import java.util.ArrayList;
import java.util.Collections;


public class Cources extends AppCompatActivity {

    ProgressBar progressBar;
    private RecyclerView recyclerView;
    FrameLayout root;
    TextView connectMark;
    DatabaseReference databaseCources;
    ArrayList<Subject> subjects;
    ArrayList<Chapter> chapters;
    chapterAdapter chapterAdapter;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cources);

        progressBar = (ProgressBar)findViewById(R.id.pb_loading_indicator);

        connectMark = (TextView)findViewById(R.id.connectMark);

        root = (FrameLayout)findViewById(R.id.root);
        databaseCources = FirebaseDatabase.getInstance().getReference("cources");



        /* enable back buutun */
        ActionBar actionBar = this.getSupportActionBar();
        if(actionBar!=null){
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        recyclerView = (RecyclerView)findViewById(R.id.rv);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        subjects = new ArrayList<>();
        chapters = new ArrayList<>();


        //-------------------------------------------------------------------------------------------------------//

    }

    public boolean checkConncectivity(){
        ConnectivityManager ConnectionManager=(ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo=ConnectionManager.getActiveNetworkInfo();
        if(networkInfo != null && networkInfo.isConnected()==true ){
            return true;
        }else
        {
            connectMark.setVisibility(View.VISIBLE);

            return false;
        }
    }

    @Override
    protected void onStart() {
        super.onStart();

        if(checkConncectivity()) {

            databaseCources.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                    progressBar.setVisibility(View.INVISIBLE);

                    subjects.clear();

                    for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {

                        final CousesModel cousesModel = dataSnapshot1.getValue(CousesModel.class);
                        final String courseName = cousesModel.getCoursesName();


                        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("chapters")
                                .child(cousesModel.getId());
                        databaseReference.addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                                chapters = new ArrayList<>();

                                for (DataSnapshot dataSnapshotchapters : dataSnapshot.getChildren()) {
                                    ChapterModel chapterModel = dataSnapshotchapters.getValue(ChapterModel.class);

                                    chapters.add(new Chapter(chapterModel.getName() ,chapterModel.getId() , courseName
                                            ,cousesModel.getId(), cousesModel.getCourse_code() , chapterModel.getNumber()));
                                }


                                String dr_id = FirebaseAuth.getInstance().getCurrentUser().getUid();

                                Collections.sort(chapters);

                                if (dr_id.equals(cousesModel.getDr_id()))
                                    subjects.add(new Subject(courseName, chapters));

                                chapterAdapter = new chapterAdapter(subjects);

                                recyclerView.setAdapter(chapterAdapter);

                                progressBar.setVisibility(View.GONE);
                                connectMark.setVisibility(View.GONE);

                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {

                            }
                        });

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
        if(item.getItemId() == android.R.id.home)
        {
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

    public void openQuestion(View view) {
        Intent intent   = new Intent(Cources.this, Chapterq.class);
        startActivity(intent);
    }
}
