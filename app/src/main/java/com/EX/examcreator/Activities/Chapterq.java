package com.EX.examcreator.Activities;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.viewpager.widget.ViewPager;
import com.EX.examcreator.Adapters.ViewPagerAdapter;
import com.EX.examcreator.Models.Question;
import com.EX.examcreator.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import java.util.ArrayList;
import java.util.List;

public class Chapterq extends AppCompatActivity {

    DatabaseReference databaseQuestions;

    TextView ques_num;

    ViewPager viewPager;
    ViewPagerAdapter viewPagerAdapter;
    Toolbar toolbar;

    private String courseName;
    private String chapter_id;
    String course_id;
    private String chapterName;
    private  String courseCode;
    private  String chapterNumber;

    List<Question> questionsList;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chapterq);

        ques_num = (TextView)findViewById(R.id.num_ques);

        viewPager = (ViewPager)findViewById(R.id.viewPager);

        toolbar = (Toolbar) findViewById(R.id.tooBar1);
        setSupportActionBar(toolbar);

        courseName = getIntent().getStringExtra("courseName");
        chapterName = getIntent().getStringExtra("chapterName");
        chapter_id = getIntent().getStringExtra("chapterId");
        course_id = getIntent().getStringExtra("courseId");
        courseCode = getIntent().getStringExtra("courseCode");
        chapterNumber = getIntent().getStringExtra("chapterNum");

        setTitle(courseName+" - "+courseCode);
        toolbar.setSubtitle("chapter "+chapterNumber+" : "+chapterName);
        toolbar.setTitleTextColor(Color.parseColor("#00000f"));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        databaseQuestions = FirebaseDatabase.getInstance().getReference("questions").child(course_id).child(chapter_id);
        questionsList = new ArrayList<>();
    }


    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.home_icon , menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
        }
        else if (item.getItemId() == R.id.home_ic) {

            Intent intent = new Intent(Chapterq.this , HomeActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onStart() {
        super.onStart();


        databaseQuestions.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                questionsList.clear();
                for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                    Question question = dataSnapshot1.getValue(Question.class);
                    questionsList.add(question);
                }

                viewPagerAdapter = new ViewPagerAdapter(getSupportFragmentManager());
                viewPagerAdapter.setQuestions((ArrayList<Question>) questionsList);
                viewPagerAdapter.setChapterId(chapter_id);
                viewPagerAdapter.setCourseId(course_id);
                viewPagerAdapter.setChapterName(chapterName);
                viewPagerAdapter.setCourseName(courseName);
                viewPager.setAdapter(viewPagerAdapter);

                ques_num.setText("Total: "+questionsList.size());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


    }

    public void addques(View view) {

        Intent intent = new Intent(Chapterq.this , addq.class);
        intent.putExtra("chap_ID" , chapter_id);
        intent.putExtra("course_ID" , course_id);
        startActivity(intent);
    }
}
