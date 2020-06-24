package com.EX.examcreator.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.EX.examcreator.Adapters.ChosenChapterAdapter;
import com.EX.examcreator.Models.ChapterModel;
import com.EX.examcreator.Models.CousesModel;
import com.EX.examcreator.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import es.dmoral.toasty.Toasty;


public class CreateExam_ChosenChapters extends AppCompatActivity implements ChosenChapterAdapter.ListItemClickListener {


    private RecyclerView recyclerView;
    Button next;


    DatabaseReference chapterReference;
    CousesModel cousesModel;
    List<ChapterModel> chapterModels;
    StringBuilder s;

    ArrayList<ChapterModel> chapterModelsChecked = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_exam__chosen_chapters);


        cousesModel = getIntent().getParcelableExtra("courseObject");
        chapterReference = FirebaseDatabase.getInstance().getReference("chapters").child(cousesModel.getId());
        chapterModels = new ArrayList<>();


        recyclerView = findViewById(R.id.RV_Chosen_Chapters);

        next=findViewById(R.id.button_next);
        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(!chapterModelsChecked.isEmpty()) {
                    Intent intent = new Intent(CreateExam_ChosenChapters.this, CreateExam_DetermineQuestions.class);
                    Bundle bundle = new Bundle();
                    bundle.putParcelableArrayList("checkedChapters", chapterModelsChecked);
                    bundle.putParcelable("Course_choosing", cousesModel);
                    intent.putExtras(bundle);
                    startActivity(intent);
                }else {
                    Toasty.info(CreateExam_ChosenChapters.this, "Choose any chapter first !!", Toast.LENGTH_LONG, true).show();
                }
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();

        chapterReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                chapterModels.clear();
                for(DataSnapshot chaptersnapshot : dataSnapshot.getChildren()){
                    ChapterModel chapterModel = chaptersnapshot.getValue(ChapterModel.class);
                    chapterModels.add(chapterModel);
                }

                Collections.sort(chapterModels);

                ChosenChapterAdapter mAdapter = new ChosenChapterAdapter(chapterModels , CreateExam_ChosenChapters.this);

                recyclerView.setAdapter(mAdapter);
                recyclerView.setAdapter(mAdapter);
                recyclerView.setHasFixedSize(true);
                recyclerView.setItemAnimator(new DefaultItemAnimator());
                GridLayoutManager gridLayoutManager = new GridLayoutManager(CreateExam_ChosenChapters.this,1);
                gridLayoutManager.setOrientation(RecyclerView.VERTICAL);
                recyclerView.setLayoutManager(gridLayoutManager);

                if(chapterModels.isEmpty()){
                    Toast.makeText(CreateExam_ChosenChapters.this, "No chapters added yet !!", Toast.LENGTH_SHORT).show();
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }


    @Override
    public void onClick( View view, int position) {

        CheckBox checkBox = (CheckBox)view;

        if(checkBox.isChecked()){
            chapterModelsChecked.add(chapterModels.get(position));
        }else if(!checkBox.isChecked()){
            chapterModelsChecked.remove(chapterModels.get(position));
        }
    }
}
