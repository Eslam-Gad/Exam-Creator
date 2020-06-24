package com.EX.examcreator.Activities;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NavUtils;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.EX.examcreator.Adapters.ChosenCourseAdapter;
import com.EX.examcreator.Models.CousesModel;
import com.EX.examcreator.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class CreateExam_ChosenCourse  extends AppCompatActivity implements ChosenCourseAdapter.ListItemClickListener {
    private ChosenCourseAdapter mAdapter;
    private RecyclerView recyclerView;
    TextView connmark;
    ProgressBar progressBar;
    DatabaseReference databaseCources;

    List<CousesModel> courcesTitle;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_exam__chosen_course);

        connmark = (TextView)findViewById(R.id.connectMark);
        progressBar = (ProgressBar) findViewById(R.id.pb_loading_indicator);

        databaseCources = FirebaseDatabase.getInstance().getReference("cources");
        courcesTitle = new ArrayList<>();

        /* enable back buutun */
        ActionBar actionBar = this.getSupportActionBar();
        if(actionBar!=null){
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        recyclerView = findViewById(R.id.RV_Chosen_Course);

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

                    ChosenCourseAdapter coursesAdapter = new ChosenCourseAdapter(courcesTitle, CreateExam_ChosenCourse.this);

                    recyclerView.setAdapter(coursesAdapter);
                    recyclerView.setHasFixedSize(true);
                    recyclerView.setItemAnimator(new DefaultItemAnimator());
                    GridLayoutManager gridLayoutManager = new GridLayoutManager(CreateExam_ChosenCourse.this,1);
                    gridLayoutManager.setOrientation(RecyclerView.VERTICAL);
                    recyclerView.setLayoutManager(gridLayoutManager);

                    progressBar.setVisibility(View.GONE);
                    connmark.setVisibility(View.GONE);

                }


                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });

        }else {
            progressBar.setVisibility(View.GONE);
        }
    }


    public boolean checkConncectivity(){
        ConnectivityManager ConnectionManager=(ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo=ConnectionManager.getActiveNetworkInfo();
        if(networkInfo != null && networkInfo.isConnected()==true ){
            return true;
        }else
        {
            connmark.setVisibility(View.VISIBLE);
            return false;
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == android.R.id.home)
        {
            NavUtils.navigateUpFromSameTask(this);
        }
        return super.onOptionsItemSelected(item);
    }


    @Override
    public void onClick(int position) {
        Intent intent=new Intent(this,CreateExam_ChosenChapters.class);
        intent.putExtra("courseObject" , courcesTitle.get(position));
        startActivity(intent);
    }
}
